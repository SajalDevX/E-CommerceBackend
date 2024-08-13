package example.com.repository.profile

import example.com.model.AddUserAddress
import example.com.model.ProfileResponse
import example.com.model.UpdateProfile
import example.com.utils.Response

interface ProfileRepository {
    suspend fun updateUserProfile(userId:String,updateProfile:UpdateProfile):Response<ProfileResponse>
    suspend fun addNewProfileAddress(userId:String, newAddress:AddUserAddress):Response<ProfileResponse>
    suspend fun getProfile(userId:String):Response<ProfileResponse>
    suspend fun deleteAddress(userId:String,index:Int):Response<ProfileResponse>
}