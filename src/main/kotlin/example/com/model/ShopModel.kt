package example.com.model

import example.com.dao.products.product.entity.ProductEntity
import example.com.dao.shop.entity.ShopCategoryEntity
import example.com.dao.shop.entity.ShopEntity
import kotlinx.serialization.Serializable

@Serializable
data class ShopResponse(
    val success:Boolean,
    val message:String,
    val shop:ShopEntity?=null,
    val products:List<ProductEntity>?=null
)
@Serializable
data class ShopCategoryResponse(
    val success:Boolean,
    val message:String,
    val shopCategories:List<ShopCategoryEntity> = emptyList()
)
