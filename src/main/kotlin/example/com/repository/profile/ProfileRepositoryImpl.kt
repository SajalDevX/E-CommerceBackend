package example.com.repository.profile

import example.com.dao.users.UserDao
import example.com.model.AddUserAddress
import example.com.model.ProfileResponse
import example.com.model.UpdateProfile
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

    override suspend fun addNewProfileAddress(userId: String, newAddress: AddUserAddress): Response<ProfileResponse> {
        val result = dao.addProfileAddress(userId, newAddress)
        return if (result == null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProfileResponse(
                    success = false,
                    message = "Could not add the new address"
                )
            )
        } else {
            Response.Success(
                data = ProfileResponse(
                    success = true,
                    data = result,
                    message = "Address added successfully"
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

    override suspend fun deleteAddress(userId: String, index: Int): Response<ProfileResponse> {
        val result = dao.deleteAddress(userId, index)
        return if (result == null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProfileResponse(
                    success = false,
                    message = "Could not delete address"
                )
            )
        } else {
            Response.Success(
                data = ProfileResponse(
                    success = true,
                    data = result,
                    message = "Address deleted successfully"
                )
            )
        }
    }
}