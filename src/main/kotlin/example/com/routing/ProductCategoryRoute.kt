package example.com.routing

import example.com.mappers.hasRole
import example.com.model.CategoryTextParams
import example.com.utils.PagingData
import example.com.model.ProductResponse
import example.com.model.SubCategoryTextParams
import example.com.plugins.RoleManagement
import example.com.repository.product_category.ProductCategoryRepository
import example.com.utils.Constants
import example.com.utils.saveFile
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.io.File

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
                        var fileName = ""
                        var textParams: CategoryTextParams? = null
                        val multiPartData = call.receiveMultipart()
                        multiPartData.forEachPart { partData ->
                            when (partData) {
                                is PartData.FileItem -> {
                                    fileName =
                                        partData.saveFile(folderPath = Constants.Image.PRODUCT_CATEGORY_IMAGE_FOLDER_PATH)
                                }

                                is PartData.FormItem -> {
                                    if (partData.name == "category_data") {
                                        textParams = Json.decodeFromString(partData.value)
                                    }
                                }

                                else -> {}
                            }
                            partData.dispose()
                        }
                        val imageUrl = "${Constants.BASE_URL}${Constants.Image.PRODUCT_CATEGORY_IMAGE_FOLDER}$fileName"
                        if (textParams == null) {
                            File("${Constants.Image.PRODUCT_CATEGORY_IMAGE_FOLDER_PATH}/$fileName").delete()
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = ProductResponse(
                                    success = false,
                                    message = "Could not parse data"
                                )
                            )
                        } else {
                            val result = repository.createProductCategory(
                                textParams!!.categoryName,
                                imageUrl
                            )
                            call.respond(
                                status = result.code,
                                message = result.data
                            )
                        }
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