package example.com.dao.products.product_category

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class ProductCategory(
    val productCategoryId:String = ObjectId().toString(),
    val categoryName:String,
    val subCategories:List<String> = emptyList(),
    val image:String?
)
