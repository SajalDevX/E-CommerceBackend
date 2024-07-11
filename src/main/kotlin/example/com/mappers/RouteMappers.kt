package example.com.mappers

import example.com.plugins.RoleManagement
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun ApplicationCall.hasRole(vararg requiredRoles: RoleManagement): Boolean {
    val principal = authentication.principal<JWTPrincipal>()
    val role = principal?.payload?.getClaim("role")?.asString()
    return role != null && requiredRoles.any { it.role == role }
}
