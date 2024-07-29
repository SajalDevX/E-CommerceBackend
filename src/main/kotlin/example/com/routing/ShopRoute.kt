package example.com.routing

import example.com.dao.users.entity.UserEntity
import example.com.mappers.hasRole
import example.com.model.ProductResponse
import example.com.model.ShopResponse
import example.com.plugins.RoleManagement
import example.com.repository.shop.ShopRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.shopRoute() {
    val repository by inject<ShopRepository>()
    authenticate("auth-jwt") {
        route("shop") {
            route("category") {
                post {
                    if (call.hasRole(RoleManagement.ADMIN)) {
                        try {
                            val shopCategoryName = call.request.queryParameters["shopCategoryName"] ?: return@post
                            val result = repository.createShopCategory(shopCategoryName)
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
                    if (call.hasRole(RoleManagement.ADMIN)) {
                        try {
                            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 0
                            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 10
                            val request = repository.getShopCategories(limit, offset)
                            call.respond(
                                status = request.code,
                                message = request.data
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
                delete {
                    if (call.hasRole(RoleManagement.ADMIN)) {
                        try {
                            val shopCategoryId = call.request.queryParameters["shopCategoryId"] ?: return@delete
                            val result = repository.deleteShopCategory(shopCategoryId)
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
                put {
                    if (call.hasRole(RoleManagement.ADMIN)) {
                        try {
                            val shopCategoryName = call.request.queryParameters["shopCategoryName"]
                            val shopCategoryId = call.request.queryParameters["shopCategoryId"]
                            if (shopCategoryId.isNullOrEmpty() || shopCategoryName.isNullOrEmpty()) {
                                return@put
                            }
                            val result = repository.updateShopCategory(shopCategoryId, shopCategoryName)
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
                            message = ShopResponse(
                                success = false,
                                message = "You do not have the required permissions to access this resource"
                            )
                        )
                    }
                }
            }
            route("add-shop") {
                post {
                    if (call.hasRole(RoleManagement.SELLER, RoleManagement.ADMIN)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val shopCategoryId = call.request.queryParameters["shopCategoryId"]
                            val shopName = call.request.queryParameters["shopName"]
                            if (userId.isNullOrEmpty() || shopCategoryId.isNullOrEmpty() || shopName.isNullOrEmpty()) {
                                call.respond(
                                    status = HttpStatusCode.MethodNotAllowed,
                                    message = ProductResponse(
                                        success = false,
                                        message = "Some Parameters are missing"
                                    )
                                )
                                return@post
                            }
                            val result = repository.createShop(userId, shopCategoryId, shopName)
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
            }
            route("get-shop"){
                get {
                    if (call.hasRole(RoleManagement.SELLER, RoleManagement.ADMIN)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()

                            val result = repository.getCurrentShop(userId!!)
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
            }
        }
    }
}