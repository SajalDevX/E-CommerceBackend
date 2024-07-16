package example.com.dao.wishlist

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class WishlistEntity(
    @BsonId
    val wishlistId: String = ObjectId().toString(),
    val userId: String,
    val products: List<String> = emptyList()
)