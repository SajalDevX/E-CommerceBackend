package example.com.repository.profile

import example.com.dao.users.UserDao
import example.com.model.*
import example.com.utils.Response
import io.ktor.http.*

class ProfileRepositoryImpl(
    private val dao:UserDao
): ProfileRepository {
    override suspend fun updateUserProfile(userId: String, updateProfile: UpdateProfile): Response<ProfileResponse> {
        val result = dao.updateUserProfile(userId, updateProfile)
        return if (result==null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProfileResponse(
                    success = false,
                    message = "Could not add your order"
                )
            )
        } else {
            Response.Success(
                data = ProfileResponse(
                    success = true,
                    data =result,
                    message = "Order added successfully"
                )
            )
        }
    }

    override suspend fun updateProfileAddress(
        userId: String,
        updateProfile: UpdateUserAddress
    ): Response<ProfileResponse> {
        val result = dao.updateProfileAddress(userId,updateProfile)
        return if (result==null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProfileResponse(
                    success = false,
                    message = "Could not add your order"
                )
            )
        } else {
            Response.Success(
                data = ProfileResponse(
                    success = true,
                    data =result,
                    message = "Order added successfully"
                )
            )
        }
    }

    override suspend fun getProfile(userId: String): Response<ProfileResponse> {
        val result = dao.findUserById(userId)
        return if (result==null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProfileResponse(
                    success = false,
                    message = "Could not load your profile"
                )
            )
        } else {
            Response.Success(
                data = ProfileResponse(
                    success = true,
                    data =result,
                    message = "Profile loaded successfully"
                )
            )
        }
    }
}