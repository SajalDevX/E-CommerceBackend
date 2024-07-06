package example.com.dao.product.entity.category

import kotlinx.serialization.Serializable

@Serializable
data class ProductSubCategory(
    val categoryId: String,
    val sunCategoryName: String,
    val image: String?
)