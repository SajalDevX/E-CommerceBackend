package example.com.dao.shop

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ShopCategory(
    val shopCategoryId: String = ObjectId().toString(),
    val shopCategoryName: String
)