package example.com.routing

import example.com.mappers.hasRole
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.wishlist.WishlistRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Routing.wishlistRoute() {
    val repository by inject<WishlistRepository>()
    val log = LoggerFactory.getLogger("WishlistRoute")

    authenticate("auth-jwt") {
        route("wishlist") {
            post {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val productId = call.request.queryParameters["productId"]
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        if (productId.isNullOrEmpty()) {
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = ProductResponse(
                                    success = false,
                                    message = "Product ID is missing"
                                )
                            )
                            return@post
                        }

                        val result = repository.addToWishlist(userId = userId!!, productId = productId)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        log.error("BadRequestException: ${badRequestError.message}")
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ProductResponse(
                                success = false,
                                message = "Bad request: ${badRequestError.message}"
                            )
                        )
                    } catch (anyError: Throwable) {
                        log.error("Unexpected error: ${anyError.message}", anyError)
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
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()

                        val result = repository.getWishlist(userId = userId!!)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        log.error("BadRequestException: ${badRequestError.message}")
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ProductResponse(
                                success = false,
                                message = "Bad request: ${badRequestError.message}"
                            )
                        )
                    } catch (anyError: Throwable) {
                        log.error("Unexpected error: ${anyError.message}", anyError)
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
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        val productId = call.request.queryParameters["productId"]

                        if (productId.isNullOrEmpty()) {
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = ProductResponse(
                                    success = false,
                                    message = "Product ID is missing"
                                )
                            )
                            return@delete
                        }

                        val result = repository.deleteFromWishlist(userId = userId!!, productId)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        log.error("BadRequestException: ${badRequestError.message}")
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ProductResponse(
                                success = false,
                                message = "Bad request: ${badRequestError.message}"
                            )
                        )
                    } catch (anyError: Throwable) {
                        log.error("Unexpected error: ${anyError.message}", anyError)
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
