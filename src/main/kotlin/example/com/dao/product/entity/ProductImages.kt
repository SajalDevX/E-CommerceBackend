package example.com.dao.product.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class ProductImages(
    @BsonId val id: String = ObjectId().toString(),
    val userId: String,
    val productId: String,
    val imageUrl: String
)