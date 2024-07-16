package example.com.dao.review

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ReviewEntity(
    @BsonId
    val reviewId:String=ObjectId().toString(),
    val userId:String,
    val productId:String,
    val username:String,
    val caption:String,
    val rating:Int=0,
    val profilePic:String
)