package example.com.routing

import example.com.mappers.hasRole
import example.com.model.BrandTextParams
import example.com.model.CategoryTextParams
import example.com.utils.PagingData
import example.com.model.ProductResponse
import example.com.plugins.RoleManagement
import example.com.repository.brand.BrandRepository
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
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.io.File

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
                        var fileName = ""
                        var textParams: BrandTextParams? = null
                        val multiPartData = call.receiveMultipart()
                        multiPartData.forEachPart { partData ->
                            when (partData) {
                                is PartData.FileItem -> {
                                    fileName =
                                        partData.saveFile(folderPath = Constants.Image.PRODUCT_BRAND_IMAGE_FOLDER_PATH)
                                }

                                is PartData.FormItem -> {
                                    if (partData.name == "brand_data") {
                                        textParams = Json.decodeFromString(partData.value)
                                    }
                                }
                                else -> {}
                            }
                            partData.dispose()
                        }
                        val imageUrl = "${Constants.BASE_URL}${Constants.Image.PRODUCT_BRAND_IMAGE_FOLDER}$fileName"
                        if (textParams == null) {
                            File("${Constants.Image.PRODUCT_BRAND_IMAGE_FOLDER_PATH}/$fileName").delete()
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = ProductResponse(
                                    success = false,
                                    message = "Could not parse data"
                                )
                            )
                        } else {
                            val result = repository.addBrand(
                                textParams!!.brandName,
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