package example.com.routing

import example.com.mappers.OrderStatus
import example.com.mappers.hasRole
import example.com.model.AddOrder
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.orders.OrderRepository
import example.com.utils.PagingData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.ordersRoute() {
    val repository by inject<OrderRepository>()

    authenticate("auth-jwt") {
        route("order") {
            post {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val params = call.receiveNullable<AddOrder>()
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        if (params == null) {
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = ProductResponse(
                                    success = false,
                                    message = "Some parameters are missing"
                                )
                            )
                            return@post
                        }
                        val result = repository.createOrder(userId!!, params)
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
            get {
                if (call.hasRole(RoleManagement.CUSTOMER)) {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                        val params = PagingData(
                            limit, offset
                        )
                        val result = repository.getOrders(userId!!, params)
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
            route("/payment") {
                put {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val orderId = call.request.queryParameters["orderId"]

                            val orderStatus = OrderStatus.PAID
                            if (orderId == null) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@put
                            }
                            val result = repository.updateOrder(orderId, orderStatus)
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
            }
            route("/receive") {
                put {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val orderId = call.request.queryParameters["orderId"]
                            val orderStatus = OrderStatus.RECEIVED
                            if (orderId == null) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@put
                            }
                            val result = repository.updateOrder(orderId, orderStatus)
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
            }
            route("/cancel") {
                put {
                    if (call.hasRole(RoleManagement.CUSTOMER)) {
                        try {
                            val orderId = call.request.queryParameters["orderId"]
                            val orderStatus = OrderStatus.CANCELED
                            if (orderId == null) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@put
                            }
                            val result = repository.updateOrder(orderId, orderStatus)
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
            }
            route("/confirm") {
                put {
                    if (call.hasRole(RoleManagement.SELLER)) {
                        try {
                            val orderId = call.request.queryParameters["orderId"]
                            val orderStatus = OrderStatus.CONFIRMED
                            if (orderId == null) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@put
                            }
                            val result = repository.updateOrder(orderId, orderStatus)
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
            }
            route("/deliver") {
                put {
                    if (call.hasRole(RoleManagement.SELLER)) {
                        try {
                            val orderId = call.request.queryParameters["orderId"]
                            val orderStatus = OrderStatus.DELIVERED
                            if (orderId == null) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some parameters are missing"
                                    )
                                )
                                return@put
                            }
                            val result = repository.updateOrder(orderId, orderStatus)
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
            }
        }
    }
}
