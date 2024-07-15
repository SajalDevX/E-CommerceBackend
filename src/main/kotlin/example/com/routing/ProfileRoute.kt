package example.com.routing

import example.com.mappers.hasRole
import example.com.model.ProductResponse
import example.com.model.UpdateProfile
import example.com.model.UpdateUserAddress
import example.com.plugins.RoleManagement
import example.com.repository.profile.ProfileRepository
import example.com.utils.Constants
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
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Routing.profileRoute() {
    val repository by inject<ProfileRepository>()
    authenticate("auth-jwt") {
        route("profile") {
            get {
                if (call.hasRole(RoleManagement.SELLER, RoleManagement.ADMIN, RoleManagement.CUSTOMER)) {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        val result = repository.getProfile(userId!!)
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
            put {
                if (call.hasRole(RoleManagement.SELLER, RoleManagement.ADMIN, RoleManagement.CUSTOMER)) {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        var fileName = ""
                        var userParams: UpdateProfile? = null
                        val multiPart = call.receiveMultipart()
                        multiPart.forEachPart { partData ->
                            when (partData) {
                                is PartData.FileItem -> {
                                    fileName = partData.saveFile(folderPath = Constants.Image.PROFILE_IMAGE_FOLDER_PATH)
                                }

                                is PartData.FormItem -> {
                                    if (partData.name == "profile_data") {
                                        userParams = Json.decodeFromString(partData.value)
                                    }
                                }

                                else -> {}
                            }
                            partData.dispose()
                        }
                        val imageUrl = "${Constants.BASE_URL}${Constants.Image.PROFILE_IMAGE_FOLDER}$fileName"
                        val result = repository.updateUserProfile(
                            userId = userId!!,
                            updateProfile = userParams!!.copy(
                                imageUrl = if (fileName.isNotEmpty()) imageUrl else userParams!!.imageUrl
                            )
                        )
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
            put("address") {
                if (call.hasRole(RoleManagement.SELLER, RoleManagement.ADMIN, RoleManagement.CUSTOMER)) {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = principal?.payload?.getClaim("userId")?.asString()
                        val params = call.receiveNullable<UpdateUserAddress>()

                        val result = repository.updateProfileAddress(userId!!, params!!)
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
        }
    }
}
