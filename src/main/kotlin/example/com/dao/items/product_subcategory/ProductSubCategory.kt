package example.com.dao.items.product_subcategory

import kotlinx.serialization.Serializable

@Serializable
data class ProductSubCategory(
    val categoryId: String,
    val sunCategoryName: String,
    val image: String?
)