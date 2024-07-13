package example.com.model

import example.com.dao.products.product_subcategory.ProductSubCategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProductSubCategoryResponse(
    val success: Boolean,
    val message:String,
    val subCategories : List<ProductSubCategoryEntity> = emptyList(),
)