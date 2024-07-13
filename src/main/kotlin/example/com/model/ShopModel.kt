package example.com.model

import example.com.dao.shop.entity.ShopCategoryEntity
import example.com.dao.shop.entity.ShopEntity
import kotlinx.serialization.Serializable

@Serializable
data class ShopResponse(
    val success:Boolean,
    val message:String,
    val shop:ShopEntity?=null
)
@Serializable
data class ShopCategoryResponse(
    val success:Boolean,
    val message:String,
    val shopCategories:List<ShopCategoryEntity> = emptyList()
)
