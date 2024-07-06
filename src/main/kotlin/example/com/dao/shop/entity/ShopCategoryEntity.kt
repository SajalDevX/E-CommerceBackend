package example.com.dao.shop.entity

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ShopCategory(
    val shopCategoryId: String = ObjectId().toString(),
    val shopCategoryName: String
)