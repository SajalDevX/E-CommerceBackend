package example.com.dao.product.entity.category

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class ProductCategory(
    val productCategoryId:String = ObjectId().toString(),
    val categoryName:String,
    val subCategories:List<ProductSubCategory>,
    val image:String?
)
