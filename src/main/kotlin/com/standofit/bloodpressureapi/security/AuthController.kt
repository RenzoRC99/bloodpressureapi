package com.standofit.bloodpressureapi.security

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val securityConfig: SecurityConfig) {

    data class LoginRequest(val username: String, val password: String)
    data class LoginResponse(val token: String)

    private val envUsername = System.getenv("JWT_USERNAME") ?: "defaultUser"
    private val envPassword = System.getenv("JWT_PASSWORD") ?: "defaultPass"

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return if (request.username == envUsername && request.password == envPassword) {
            val token = securityConfig.generateToken(request.username)
            ResponseEntity.ok(LoginResponse(token))
        } else {
            ResponseEntity.status(401).build()
        }
    }
}
