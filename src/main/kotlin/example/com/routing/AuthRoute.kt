package example.com.routing


import example.com.repository.auth.AuthRepository
import example.com.model.AuthResponse
import example.com.model.SignInParams
import example.com.model.SignUpParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.authRouting(repository: AuthRepository) {

    route(path = "/signup") {
        post {
            val params = call.receiveNullable<SignUpParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid Credentials"
                    )
                )
                return@post
            }

            val result = repository.signUp(params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }

    route(path = "/login") {
        post {
            val params = call.receiveNullable<SignInParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid Credentials"
                    )
                )
                return@post
            }
            val result = repository.signIn(params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}