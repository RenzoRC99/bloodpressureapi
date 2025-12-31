package com.standofit.bloodpressureapi.security

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/auth")
class AuthController(
    private val securityConfig: SecurityConfig
) {

    data class LoginRequest(val username: String, val password: String)

    data class TokenResponse(
        val accessToken: String,
        val refreshToken: String,
        val expiresIn: Long
    )

    private val envUsername = System.getenv("JWT_USERNAME") ?: "defaultUser"
    private val envPassword = System.getenv("JWT_PASSWORD") ?: "defaultPass"

    // =====================
    // LOGIN
    // =====================
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {
        return if (request.username == envUsername && request.password == envPassword) {

            val accessToken = securityConfig.generateAccessToken(request.username)
            val refreshToken = securityConfig.generateRefreshToken(request.username)

            ResponseEntity.ok(
                TokenResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    expiresIn = 900 // 15 min
                )
            )
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    // =====================
    // REFRESH
    // =====================
    @PostMapping("/refresh")
    fun refresh(request: HttpServletRequest): ResponseEntity<TokenResponse> {

        val authHeader = request.getHeader("Authorization")
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val refreshToken = authHeader.substring(7)

        if (!securityConfig.isValid(refreshToken) ||
            !securityConfig.isRefreshToken(refreshToken)
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val username = securityConfig.extractUsername(refreshToken)

        val newAccessToken = securityConfig.generateAccessToken(username)

        return ResponseEntity.ok(
            TokenResponse(
                accessToken = newAccessToken,
                refreshToken = refreshToken, // no rotamos por ahora
                expiresIn = 900
            )
        )
    }
}
