package example.com.routing

import example.com.mappers.hasRole
import example.com.model.PagingData
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.brand.BrandRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.brandRoute() {
    val repository by inject<BrandRepository>()
    authenticate("auth-jwt") {
        route("brand") {
            get {
                if (call.hasRole(RoleManagement.CUSTOMER, RoleManagement.SELLER, RoleManagement.ADMIN)) {
                    try {
                        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                        val pagingData = PagingData(
                            limit, offset
                        )
                        val result = repository.getBrand(pagingData)
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
                        val brandName = call.request.queryParameters["brandName"]
                        if (brandName.isNullOrEmpty()) {
                            return@post
                        }
                        val result = repository.addBrand(brandName)
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
                        val brandId = call.request.queryParameters["brandId"]
                        val brandName = call.request.queryParameters["brandName"]
                        if (brandName.isNullOrEmpty() || brandId.isNullOrEmpty()) {
                            return@put
                        }
                        val result = repository.updateBrand(brandId, brandName)
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
                        val brandId = call.request.queryParameters["brandId"]
                        if (brandId.isNullOrEmpty()) {
                            return@delete
                        }
                        val result = repository.deleteBand(brandId)
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