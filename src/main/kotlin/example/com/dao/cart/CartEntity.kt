package example.com.dao.cart

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class CartEntity(
    @BsonId
    val cartId: String = ObjectId().toString(),
    val userId: String,
    val products: Map<String, Int> = emptyMap()
)