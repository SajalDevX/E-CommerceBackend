package example.com.model

import example.com.dao.products.product_category.ProductCategoryEntity
import kotlinx.serialization.Serializable



@Serializable
data class ProductCategoryResponse(
    val success:Boolean,
    val message:String,
    val categories:List<ProductCategoryEntity> = emptyList()
)