package example.com.dao.users.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class UserEntity(
    @BsonId
    val userId: String = ObjectId().toString(),
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val imageUrl: String = "",
    val userDetails: UserDetails,
)

@Serializable
data class UserDetails(
    val addresses: List<UserAddress> = emptyList(),
    val age: String? = null,
    val mobile: String,
    val gender: String,
    val userRole: String,
)

@Serializable
data class UserAddress(
    val fatherName: String,
    val motherName: String,
    val pin: String,
    val mobileNumber: String,
    val otherMobileNumber: String? = null,
    val city: String,
    val road: String,
    val state: String,
)