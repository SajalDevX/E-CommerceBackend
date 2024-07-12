package example.com.routing

import example.com.mappers.hasRole
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.product_sub_category.ProductSubCategoryRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.productSubCategoryRoute() {
    val repository by inject<ProductSubCategoryRepository>()

    authenticate("auth-jwt") {
        route("product-sub-category") {
            get {
                if (call.hasRole(RoleManagement.ADMIN, RoleManagement.SELLER, RoleManagement.CUSTOMER)) {
                    try {
                        val categoryId = call.request.queryParameters["categoryId"]
                        val offset = call.request.queryParameters["offset"]?.toIntOrNull()
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull()
                        val result = repository.getProductSubCategory(categoryId!!, offset!!, limit!!)

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
                        val categoryId = call.request.queryParameters["categoryId"]
                        val subCategoryName = call.request.queryParameters["subCategoryName"]

                        val result = repository.createProductSubcategory(categoryId!!, subCategoryName!!)

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
                if (call.hasRole(RoleManagement.ADMIN)) {
                    try {
                        val subCategoryId = call.request.queryParameters["subCategoryId"]
                        val subCategoryName = call.request.queryParameters["subCategoryName"]
                        val result = repository.updateProductSubcategory(subCategoryId!!, subCategoryName!!)
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
                        val subCategoryId = call.request.queryParameters["subCategoryId"]
                        val result = repository.deleteProductSubCategory(subCategoryId!!)
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