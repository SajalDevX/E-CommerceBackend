package example.com.dao.shop

import kotlinx.serialization.Serializable

@Serializable
data class ShopEntity(
    val userId:String,
    val shopCategory:ShopCategory,
    val shopName:String
)