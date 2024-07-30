package example.com.dao.shop.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ShopEntity(
    @BsonId
    val shopId: String = ObjectId().toString(),
    val userId: String,
    val shopCategoryId: String,
    val shopCategoryName: String,
    val shopName: String,
//    val shopItems:Map<String,Int> = emptyMap(),
    val orders: List<Orders> = emptyList()
)

@Serializable
data class Orders(
    val productId: String,
    val qty: String
)