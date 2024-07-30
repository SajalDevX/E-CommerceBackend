package example.com.routing

import example.com.mappers.hasRole
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.cart.CartRepository
import example.com.utils.PagingData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
fun Routing.cartRoute() {
    val repository by inject<CartRepository>()
    authenticate("auth-jwt") {
        route("cart") {
            route("/{productId}") {
                post {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val productId = call.parameters["productId"]
                            if (productId == null || userId == null) {
                                call.respond(
                                    status = HttpStatusCode.BadRequest,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@post
                            }
                            val qty = call.request.queryParameters["qty"]?.toIntOrNull() ?: 1
                            val result = repository.addToCart(userId, productId, qty)
                            call.respond(
                                status = result.code,
                                message = result.data
                            )
                        } catch (e: Exception) {
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
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val productId = call.parameters["productId"]
                            val qty = call.request.queryParameters["qty"]?.toIntOrNull() ?: 1
                            if (productId == null) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@put
                            }
                            val result = repository.updateCartQuantity(userId!!, productId, qty)
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
                delete {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val productId = call.parameters["productId"]
                            val qty = call.request.queryParameters["qty"]?.toIntOrNull() ?: 1
                            if (productId == null) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@delete
                            }
                            val result = repository.removeCartItem(userId!!, qty, productId)
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
            get {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                        val params = PagingData(limit, offset)

                        val result = repository.getCartItems(userId!!, params)
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
            route("/all") {
                delete {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val result = repository.deleteAllFromCart(userId!!)
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
}
