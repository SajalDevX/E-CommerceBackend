package example.com.dao.shop.entity

import example.com.dao.shop.entity.ShopCategory
import kotlinx.serialization.Serializable

@Serializable
data class ShopEntity(
    val userId:String,
    val shopCategory: ShopCategory,
    val shopName:String
)