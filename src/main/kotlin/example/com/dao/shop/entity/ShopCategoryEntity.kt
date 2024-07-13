package example.com.dao.shop.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ShopCategoryEntity(
    @BsonId
    val shopCategoryId: String = ObjectId().toString(),
    val shopCategoryName: String
)