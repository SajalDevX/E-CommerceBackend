package example.com.model

import example.com.dao.users.entity.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfile(
    val name: String,
    val imageUrl: String?=null,
    val mobile: String,
    val age: String,
    val gender: String,
)


@Serializable
data class AddUserAddress(
    val fatherName: String,
    val motherName: String,
    val pin: String,
    val mobileNumber: String,
    val otherMobileNumber: String? = null,
    val city: String,
    val road: String,
    val state: String,
)


@Serializable
data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val data: UserEntity? = null
)