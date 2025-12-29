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
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
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
            .csrf { it.disable() } // Desactivar CSRF para APIs stateless
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        http.addFilterBefore(
            JWTAuthenticationFilter(),
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter::class.java
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
                    // ✅ Ahora asigna ROLE_ADMIN, coherente con AuthenticationManager
                    val auth: Authentication = UsernamePasswordAuthenticationToken(
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

    // Función para generar un JWT válido
    fun generateToken(username: String): String {
        val expiration = Date(System.currentTimeMillis() + 1000 * 60 * 60) // 1 hora
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(expiration)
            .signWith(secretKey)
            .compact()
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
