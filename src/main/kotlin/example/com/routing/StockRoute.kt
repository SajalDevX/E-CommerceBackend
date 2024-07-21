package example.com.routing

import example.com.mappers.hasRole
import example.com.model.StockResponse
import example.com.plugins.RoleManagement
import example.com.repository.stock.StockRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.stockRoute() {
    val repository by inject<StockRepository>()
    authenticate("auth-jwt") {
        route("stock") {
            post {
                if (call.hasRole(RoleManagement.SELLER)) {
                    try {

                        val productId = call.request.queryParameters["productId"]
                        val shopId = call.request.queryParameters["shopId"]
                        val qty = call.request.queryParameters["qty"]?.toIntOrNull()
                        if (productId.isNullOrEmpty() || shopId.isNullOrEmpty() || qty == null) {
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing Parameters"
                            )
                            return@post
                        }
                        val result = repository.increaseStocks(productId, shopId, qty)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )

                    } catch (badRequestError: BadRequestException) {
                        return@post
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = StockResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = StockResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
            put {
                if (call.hasRole(RoleManagement.SELLER)) {
                    try {

                        val productId = call.request.queryParameters["productId"]
                        val shopId = call.request.queryParameters["shopId"]
                        val qty = call.request.queryParameters["qty"]?.toIntOrNull()
                        if (productId.isNullOrEmpty() || shopId.isNullOrEmpty() || qty == null) {
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing Parameters"
                            )
                            return@put
                        }
                        val result = repository.decreaseStocks(productId, shopId, qty)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )

                    } catch (badRequestError: BadRequestException) {
                        return@put
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = StockResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = StockResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
            get {
                if (call.hasRole(RoleManagement.SELLER)) {
                    try {

                        val productId = call.request.queryParameters["productId"]
                        val shopId = call.request.queryParameters["shopId"]
                        if (productId.isNullOrEmpty() || shopId.isNullOrEmpty() ) {
                            call.respond(
                                status = HttpStatusCode.InternalServerError,
                                message = "Missing Parameters"
                            )
                            return@get
                        }
                        val result = repository.getStocks(productId, shopId)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )

                    } catch (badRequestError: BadRequestException) {
                        return@get
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = StockResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = StockResponse(
                            success = false,
                            message = "You do not have the required permissions to access this resource"
                        )
                    )
                }
            }
        }
    }
}