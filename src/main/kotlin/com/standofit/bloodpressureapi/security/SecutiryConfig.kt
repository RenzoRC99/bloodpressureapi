package com.standofit.bloodpressureapi.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Date
import javax.crypto.SecretKey

@Configuration
class SecurityConfig {

    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val tokenPrefix = "Bearer "
    private val headerString = "Authorization"

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val username = System.getenv("JWT_USERNAME") ?: "defaultUser"
        val password = System.getenv("JWT_PASSWORD") ?: "defaultPass"

        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .inMemoryAuthentication()
            .withUser(username)
            .password(passwordEncoder().encode(password))
            .roles("ADMIN")
            .and()
            .and()
            .build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        http.addFilterBefore(
            JWTAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter::class.java
        )

        http.cors { it.configurationSource(corsConfigurationSource()) }

        return http.build()
    }

    inner class JWTAuthenticationFilter : OncePerRequestFilter() {
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
        ) {
            val header = request.getHeader(headerString)
            if (header == null || !header.startsWith(tokenPrefix)) {
                filterChain.doFilter(request, response)
                return
            }

            try {
                val token = header.replace(tokenPrefix, "")
                val claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .body
                val username = claims.subject
                if (username != null) {
                    val auth = UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        listOf(SimpleGrantedAuthority("ROLE_ADMIN"))
                    )
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (_: Exception) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }

            filterChain.doFilter(request, response)
        }
    }

    // =================== TOKENS ===================

    fun generateAccessToken(username: String): String {
        val expiration = Date(System.currentTimeMillis() + 15 * 60 * 1000) // 15 min
        return Jwts.builder()
            .setSubject(username)
            .claim("type", "ACCESS")
            .setExpiration(expiration)
            .signWith(secretKey)
            .compact()
    }

    fun generateRefreshToken(username: String): String {
        val expiration = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000) // 7 días
        return Jwts.builder()
            .setSubject(username)
            .claim("type", "REFRESH")
            .setExpiration(expiration)
            .signWith(secretKey)
            .compact()
    }

    fun isValid(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun isRefreshToken(token: String): Boolean {
        val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        return claims["type"] == "REFRESH"
    }

    fun extractUsername(token: String): String {
        val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        return claims.subject
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "https://tuapp.com")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
