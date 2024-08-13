package example.com.dao.users

import example.com.dao.users.entity.UserEntity
import example.com.model.AddUserAddress
import example.com.model.UpdateProfile

interface UserDao {
    suspend fun insertUser(userEntity: UserEntity): UserEntity?
    suspend fun findUserByEmail(email: String): UserEntity?
    suspend fun findUserById(userId: String): UserEntity?
    suspend fun updateUserProfile(userId:String,updateProfile: UpdateProfile):UserEntity?
    suspend fun addProfileAddress(userId:String,addAddress:AddUserAddress):UserEntity?
    suspend fun deleteAddress(userId:String,index:Int):UserEntity?
}