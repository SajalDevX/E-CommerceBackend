package example.com.utils

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class JwtTokenBody(val userId: String, val email: String, val userType: String) : Principal