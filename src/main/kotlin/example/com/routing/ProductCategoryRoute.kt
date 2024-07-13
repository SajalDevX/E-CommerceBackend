package example.com.routing

import example.com.mappers.hasRole
import example.com.utils.PagingData
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.product_category.ProductCategoryRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.productCategoryRoute() {
    val repository by inject<ProductCategoryRepository>()
    authenticate("auth-jwt") {
        route("product-category") {
            get {
                if (call.hasRole(RoleManagement.SELLER, RoleManagement.ADMIN, RoleManagement.CUSTOMER)) {
                    try {
                        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                        val paging = PagingData(
                            limit, offset
                        )
                        val result = repository.getProductCategory(paging)
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
            post {
                if (call.hasRole(RoleManagement.ADMIN)) {
                    try {
                        val params = call.request.queryParameters["productCategoryName"]
                        if (params == null) {
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = ProductResponse(
                                    success = false,
                                    message = "Invalid or missing request body"
                                )
                            )
                            return@post
                        }

                        val result = repository.createProductCategory(params)
                        call.respond(
                            status = result.code,
                            message = result.data
                        )
                    } catch (badRequestError: BadRequestException) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ProductResponse(
                                success = false,
                                message = badRequestError.localizedMessage ?: "Bad request"
                            )
                        )
                    } catch (anyError: Throwable) {
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = ProductResponse(
                                success = false,
                                message = "An unexpected error has occurred, please try again!"
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
                if (call.hasRole(RoleManagement.ADMIN)) {
                    try {
                        val params1 = call.request.queryParameters["productCategoryId"]
                        val params2 = call.request.queryParameters["productCategoryName"]
                        val result = repository.updateProductCategory(params1!!, params2!!)
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
                if (call.hasRole(RoleManagement.ADMIN)) {
                    try {
                        val params = call.request.queryParameters["productCategoryId"]
                        val result = repository.deleteProductCategory(params!!)
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