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
    val userData: UserData,
    val userDetails: UserDetails,
)
@Serializable
data class UserDetails(
    val addresses:List<UserAddress> = emptyList(),
    val age:String?=null,
    val mobile:String,
    val gender:String,
)
@Serializable
data class UserData(
    val userRole: String,
    val cartItems:List<String> = emptyList(),
    val wishlist:List<String> = emptyList(),
    val delivered:List<String> = emptyList(),
    val ordered :List<String> = emptyList(),
    val cancelled:List<String> = emptyList(),
)
@Serializable
data class UserAddress(
    val fatherName:String,
    val motherName:String,
    val pin:String,
    val mobileNumber:String,
    val otherMobileNumber:String?=null,
    val city:String,
    val road:String,
    val state:String,
)