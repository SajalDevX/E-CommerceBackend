package example.com.routing

import example.com.mappers.hasRole
import example.com.mappers.toUpdateProduct
import example.com.model.*
import example.com.plugins.RoleManagement
import example.com.repository.product.ProductRepository
import example.com.utils.Constants
import example.com.utils.getLongParameter
import example.com.utils.saveFile
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.productRoute(repository: ProductRepository) {
    authenticate("auth-jwt") {
        route("product") {
            route("get") {
                get {
                    if (call.hasRole(RoleManagement.CUSTOMER, RoleManagement.SELLER, RoleManagement.ADMIN)) {
                        try {
                            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 0
                            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 10
                            val maxPrice = call.request.queryParameters["maxPrice"]?.toDoubleOrNull() ?: 0.0
                            val minPrice = call.request.queryParameters["minPrice"]?.toDoubleOrNull() ?: 0.0
                            val categoryId = call.request.queryParameters["categoryId"]
                            val subCategoryId = call.request.queryParameters["subCategoryId"]
                            val brandId = call.request.queryParameters["brandId"]
                            val params = ProductWithFilter(
                                limit, offset, maxPrice, minPrice, categoryId, subCategoryId, brandId
                            )
                            val result = repository.getProducts(params)

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
            }
            route("/{productId}") {
                get {
                    if (call.hasRole(RoleManagement.CUSTOMER, RoleManagement.SELLER, RoleManagement.ADMIN)) {
                        try {
                            val productId = call.getLongParameter("productId")
                            val result = repository.getProductDetail(productId = productId)
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
                delete {
                    if (call.hasRole(RoleManagement.SELLER)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val productId = call.getLongParameter("productId")
                            val result = repository.deleteProduct(userId!!, productId)

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
            route("seller") {
                get {
                    if (call.hasRole(RoleManagement.SELLER)) {
                        try {
                            val productId = call.request.queryParameters["productId"]
                            if (productId.isNullOrEmpty()) {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = "Parameters are missing"
                                )
                                return@get
                            }

                            val result = repository.getProductById(productId)

                            call.respond(
                                status = result.code,
                                message = result.data
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
                            message = ProductResponse(
                                success = false,
                                message = "You do not have the required permissions to access this resource"
                            )
                        )
                    }
                }
                post {
                    if (call.hasRole(RoleManagement.SELLER)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val product = call.receiveNullable<AddProduct>()

                            if (userId == null || product == null) {
                                call.respond(
                                    status = HttpStatusCode.BadRequest,
                                    message = ProductResponse(
                                        success = false, message = "Provide the required details"
                                    )
                                )
                                return@post
                            }

                            val result = repository.addProduct(userId, product)
                            call.respond(
                                status = result.code,
                                message = result.data
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
                            message = ProductResponse(
                                success = false,
                                message = "You do not have the required permissions to access this resource"
                            )
                        )
                    }

                }
                put("/update") {
                    if (call.hasRole(RoleManagement.SELLER)) {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val productId = call.request.queryParameters["productId"] ?: ""
                            val params = call.receiveNullable<UpdateProduct>()

                            if (params == null) {
                                call.respond(
                                    status = HttpStatusCode.BadRequest,
                                    message = ProductResponse(
                                        success = false, message = "Provide the required details"
                                    )
                                )
                                return@put
                            }

                            // Fetch existing product details
                            val existingProductResponse = repository.getProductDetail(productId)
                            val existingProduct = existingProductResponse.data.product

                            if (existingProduct == null) {
                                call.respond(
                                    status = HttpStatusCode.NotFound,
                                    message = ProductResponse(
                                        success = false, message = "Product not found"
                                    )
                                )
                                return@put
                            }

                            val updatedProduct = existingProduct.copy(
                                categoryId = params.categoryId ?: existingProduct.categoryId,
                                subCategoryId = params.subCategoryId ?: existingProduct.subCategoryId,
                                brandId = params.brandId ?: existingProduct.brandId,
                                productName = params.productName ?: existingProduct.productName,
                                productCode = params.productCode ?: existingProduct.productCode,
                                productQuantity = params.productQuantity ?: existingProduct.productQuantity,
                                productDetail = params.productDetail ?: existingProduct.productDetail,
                                price = params.price ?: existingProduct.price,
                                discountPrice = params.discountPrice ?: existingProduct.discountPrice,
                                videoLink = params.videoLink ?: existingProduct.videoLink,
                                hotDeal = params.hotDeal ?: existingProduct.hotDeal,
                                buyOneGetOne = params.buyOneGetOne ?: existingProduct.buyOneGetOne,
                                imageOne = params.imageOne ?: existingProduct.imageOne,
                                imageTwo = params.imageTwo ?: existingProduct.imageTwo
                            )

                            val result = repository.updateProduct(userId!!, productId, updatedProduct.toUpdateProduct())
                            call.respond(
                                status = result.code,
                                message = result.data
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
                            message = ProductResponse(
                                success = false,
                                message = "You do not have the required permissions to access this resource"
                            )
                        )
                    }
                }
                post("/img") {
                    if (call.hasRole(RoleManagement.SELLER)) {
                        try {
                            var fileName = ""
                            val multiPartDate = call.receiveMultipart()
                            val principal = call.principal<JWTPrincipal>()
                            val userId = principal?.payload?.getClaim("userId")?.asString()
                            val productId = call.getLongParameter("productId", isQueryParameter = true)
                            multiPartDate.forEachPart { partDate ->
                                when (partDate) {
                                    is PartData.FileItem -> {
                                        fileName =
                                            partDate.saveFile(folderPath = Constants.Image.PRODUCT_IMAGE_FOLDER_PATH)
                                    }

                                    else -> {}
                                }
                                partDate.dispose()
                            }
                            val imageUrl = "${Constants.BASE_URL}${Constants.Image.PRODUCT_IMAGE_FOLDER}$fileName"
                            val result = repository.uploadProductImages(userId!!, productId, imageUrl)

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
            }
        }
    }
}
