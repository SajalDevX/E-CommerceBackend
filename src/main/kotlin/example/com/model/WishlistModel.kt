package example.com.model

import example.com.dao.products.product.entity.ProductEntity
import kotlinx.serialization.Serializable

@Serializable
data class WishlistResponse(
    val success:Boolean,
    val message:String,
    val product:ProductEntity?=null,
    val products:List<ProductEntity> = emptyList()
)