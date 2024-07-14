package example.com.repository.profile

import example.com.model.ProfileResponse
import example.com.model.UpdateProfile
import example.com.model.UpdateUserAddress
import example.com.utils.Response

interface ProfileRepository {
    suspend fun updateUserProfile(userId:String,updateProfile:UpdateProfile):Response<ProfileResponse>
    suspend fun updateProfileAddress(userId:String,updateProfile:UpdateUserAddress):Response<ProfileResponse>
    suspend fun getProfile(userId:String):Response<ProfileResponse>
}