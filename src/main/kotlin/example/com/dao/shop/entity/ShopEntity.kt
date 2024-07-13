package example.com.dao.shop.entity

import kotlinx.serialization.Serializable

@Serializable
data class ShopEntity(
    val userId:String,
    val shopCategoryId: String,
    val shopCategoryName:String,
    val shopName:String
)