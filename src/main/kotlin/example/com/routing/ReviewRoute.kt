package example.com.routing

import example.com.mappers.hasRole
import example.com.model.AddReview
import example.com.model.EditReview
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.review.ReviewRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.reviewRoute() {
    val repository by inject<ReviewRepository>()
    authenticate("auth-jwt") {
        route("review"){
            post {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val params = call.receiveNullable<AddReview>()
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()

                        if(params==null){
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing params"
                            )
                            return@post
                        }
                        val result = repository.addReview(params,userId!!)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        return@post
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = ProductResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = ProductResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
            put {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val params = call.receiveNullable<EditReview>()
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        if(params==null){
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing params"
                            )
                            return@put
                        }
                        val result = repository.editReview(params,userId!!)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        return@put
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = ProductResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = ProductResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
            get {
                if (call.hasRole(RoleManagement.CUSTOMER, RoleManagement.SELLER, RoleManagement.ADMIN)) {
                    try {
                        val productId = call.request.queryParameters["productId"]

                        if(productId==null){
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing params"
                            )
                            return@get
                        }
                        val result = repository.getAllReview(productId = productId)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        return@get
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = ProductResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = ProductResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
            delete {
                if (call.hasRole(RoleManagement.CUSTOMER, RoleManagement.SELLER, RoleManagement.ADMIN)) {
                    try {
                        val productId = call.request.queryParameters["productId"]
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        if(productId==null){
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing params"
                            )
                            return@delete
                        }
                        val result = repository.deleteReview(userId!!,productId)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        return@delete
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = ProductResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = ProductResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
        }
    }
}