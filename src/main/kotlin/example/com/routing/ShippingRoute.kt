package example.com.routing

import example.com.mappers.hasRole
import example.com.model.AddShipping
import example.com.model.ProductResponse
import example.com.model.ShopResponse
import example.com.model.UpdateShipping
import example.com.plugins.RoleManagement
import example.com.repository.shipping.ShippingRepository
import example.com.utils.getLongParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.shippingRoute() {
    val repository by inject<ShippingRepository>()
    authenticate("auth-jwt") {
        route("shipping") {
            post {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val params = call.receiveNullable<AddShipping>()
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        println("Received parameters: $params")
                        println("User ID: $userId")
                        if (params == null) {
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing Query Params"
                            )
                            return@post
                        }
                        val result = repository.addShipping(userId!!, params)
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
                        message = ShopResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
            get {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val orderId = call.request.queryParameters["orderId"]
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()

                        if (orderId.isNullOrEmpty()) {
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing Query Params"
                            )
                            return@get
                        }

                        val result = repository.getShipping(userId!!, orderId)
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
                        message = ShopResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
            route("/{orderId}") {
                put {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val orderId = call.getLongParameter("orderId")
                            val shippingAddress = call.request.queryParameters["shippingAddress"]
                            val shipCity = call.request.queryParameters["shipCity"]
                            val shipPhone = call.request.queryParameters["shipPhone"]?.toIntOrNull()
                            val shipName = call.request.queryParameters["shipName"]
                            val shipEmail = call.request.queryParameters["shipEmail"]
                            val shipCountry = call.request.queryParameters["shipCountry"]
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()


                            val currentOrder = repository.getShipping(userId!!, orderId).data
                            val shipped = currentOrder.data
                            val data = UpdateShipping(
                                orderId = orderId,
                                shipAddress = shippingAddress ?: shipped?.shipAddress,
                                shipCity = shipCity ?: shipped?.shipCity,
                                shipPhone = shipPhone ?: shipped?.shipPhone,
                                shipName = shipName ?: shipped?.shipName,
                                shipEmail = shipEmail ?: shipped?.shipEmail,
                                shipCountry = shipCountry ?: shipped?.shipCountry
                            )

                            val result = repository.updateShipping(userId, data)

                            call.respond(
                                status = result.code,
                                message = result.data
                            )

                        } catch (badRequestError: BadRequestException) {
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = "Bad request"
                            )
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
                            message = "You do not have permission to perform this action"
                        )
                    }
                }


                delete {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val orderId = call.parameters["orderId"]


                            val result = repository.deleteShipping(userId!!, orderId!!)
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
                            message = ShopResponse(
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
