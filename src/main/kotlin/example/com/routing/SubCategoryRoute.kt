package example.com.routing

import example.com.mappers.hasRole
import example.com.model.ProductResponse
import example.com.model.SubCategoryTextParams
import example.com.plugins.RoleManagement
import example.com.repository.product_sub_category.ProductSubCategoryRepository
import example.com.utils.Constants
import example.com.utils.saveFile
import io.ktor.client.engine.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.h2.command.dml.MergeUsing.When
import org.koin.ktor.ext.inject
import java.io.File

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
                        var fileName = ""
                        var textParams: SubCategoryTextParams? = null
                        val multiPartData = call.receiveMultipart()
                        multiPartData.forEachPart { partData ->
                            when (partData) {
                                is PartData.FileItem -> {
                                    fileName =
                                        partData.saveFile(folderPath = Constants.Image.PRODUCT_SUBCATEGORY_IMAGE_FOLDER_PATH)
                                }

                                is PartData.FormItem -> {
                                    if (partData.name == "subCategory_data") {
                                        textParams = Json.decodeFromString(partData.value)
                                    }
                                }

                                else -> {}
                            }
                            partData.dispose()
                        }

                        val imageUrl = "${Constants.BASE_URL}${Constants.Image.PRODUCT_SUBCATEGORY_IMAGE_FOLDER}$fileName"
                        if (textParams == null) {
                            File("${Constants.Image.PRODUCT_SUBCATEGORY_IMAGE_FOLDER_PATH}/$fileName").delete()
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = ProductResponse(
                                    success = false,
                                    message = "Could not parse data"
                                )
                            )
                        } else {
                            val result = repository.createProductSubcategory(
                                textParams!!.categoryId,
                                textParams!!.subCategoryName,
                                imageUrl
                            )
                            call.respond(
                                status = result.code,
                                message = result.data
                            )
                        }

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