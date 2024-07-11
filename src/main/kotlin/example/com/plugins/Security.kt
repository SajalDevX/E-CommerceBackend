package example.com.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

val algorithm = Algorithm.HMAC256("secret")

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ktor app"
            verifier(
                JWT
                    .require(algorithm)
                    .withAudience("ktor-audience")
                    .withIssuer("ktor-issuer")
                    .build()
            )
            validate { credential ->
                val role = credential.payload.getClaim("role").asString()
                val userId = credential.payload.getClaim("userId").asString()
                val email = credential.payload.getClaim("email").asString()

                if (role != null && userId != null && email != null &&
                    RoleManagement.entries.map { it.role }.contains(role)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

fun generateToken(userId: String, email: String, role: String): String {
    return JWT.create()
        .withAudience("ktor-audience")
        .withIssuer("ktor-issuer")
        .withClaim("userId", userId)
        .withClaim("email", email)
        .withClaim("role", role)
        .sign(algorithm)
}

enum class RoleManagement(val role: String) {
    SUPER_ADMIN("super_admin"),
    ADMIN("admin"),
    SELLER("seller"),
    CUSTOMER("customer")
}
