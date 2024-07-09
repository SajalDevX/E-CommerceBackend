package example.com.routing

import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.principal
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import example.com.model.*
import example.com.plugins.RoleManagement
import example.com.repository.product.ProductRepository
import example.com.utils.JwtTokenBody
import example.com.utils.Response
import example.com.utils.authenticateWithJwt
import io.ktor.server.plugins.*

fun NormalOpenAPIRoute.productRoute(repository: ProductRepository) {
    route("product") {

        authenticateWithJwt(RoleManagement.CUSTOMER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            route("/{productId}").get<ProductIdQueryParams, ProductResponse, JwtTokenBody> { params ->
                try {
                    val result = repository.getProductDetail(params.productId)
                    when (result) {
                        is Response.Error -> {
                            respond(
                                ProductResponse(
                                    success = result.data.success,
                                    message = result.data.message,
                                )
                            )
                        }
                        is Response.Success ->{
                            respond(
                                ProductResponse(
                                    success = result.data.success,
                                    product = result.data.product,
                                    message = result.data.message,
                                    allProducts = result.data.allProducts                                )
                            )

                        }
                    }
                }catch (badRequestError: BadRequestException) {
                    respond(
                        ProductResponse(
                            success = false,
                            message = badRequestError.message ?: "Bad request"
                        )
                    )
                } catch (anyError: Throwable) {
                    respond(
                        ProductResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
                get<ProductWithFilter, ProductResponse, JwtTokenBody>{params->
                    try {
                        val result = repository.getProducts(params)
                        when (result) {
                            is Response.Error -> {
                                respond(
                                    ProductResponse(
                                        success = result.data.success,
                                        message = result.data.message,
                                    )
                                )
                            }
                            is Response.Success ->{
                                respond(
                                    ProductResponse(
                                        success = result.data.success,
                                        product = result.data.product,
                                        message = result.data.message,
                                        allProducts = result.data.allProducts                                )
                                )

                            }
                        }
                    }catch (badRequestError: BadRequestException) {
                        respond(
                            ProductResponse(
                                success = false,
                                message = badRequestError.message ?: "Bad request"
                            )
                        )
                    } catch (anyError: Throwable) {
                        respond(
                            ProductResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )
                    }
                }
            }

        }

        authenticateWithJwt(RoleManagement.SELLER.role) {
            route("seller").post<Unit, ProductResponse, AddProduct, JwtTokenBody> { _, requestBody ->
                try {
                    val result = repository.addProduct(principal().userId, params = requestBody)
                    when (result) {
                        is Response.Error -> {
                            respond(
                                ProductResponse(
                                    success = result.data.success,
                                    message = result.data.message,
                                )
                            )
                        }

                        is Response.Success -> {
                            respond(
                                ProductResponse(
                                    success = result.data.success,
                                    product = result.data.product,
                                    message = result.data.message,
                                    allProducts = result.data.allProducts
                                )
                            )
                        }
                    }
                } catch (badRequestError: BadRequestException) {
                    respond(
                        ProductResponse(
                            success = false,
                            message = badRequestError.message ?: "Bad request"
                        )
                    )
                } catch (anyError: Throwable) {
                    respond(
                        ProductResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
                get<ProductIdParams, ProductResponse, JwtTokenBody> { params ->
                    try {
                        val userId = principal().userId
                        val result = repository.getProductById(userId, params.productId)
                        when (result) {
                            is Response.Error -> {
                                respond(
                                    ProductResponse(
                                        success = result.data.success,
                                        message = result.data.message,
                                    )
                                )
                            }

                            is Response.Success -> {
                                respond(
                                    ProductResponse(
                                        success = result.data.success,
                                        product = result.data.product,
                                        message = result.data.message,
                                        allProducts = result.data.allProducts
                                    )
                                )
                            }
                        }
                    } catch (badRequestError: BadRequestException) {
                        respond(
                            ProductResponse(
                                success = false,
                                message = badRequestError.message ?: "Bad request"
                            )
                        )
                    } catch (anyError: Throwable) {
                        respond(
                            ProductResponse(
                                success = false,
                                message = "An unexpected error has occurred, try again!"
                            )
                        )

                    }
                }
            }
        }
    }
}

