package example.com.dao.products.product_category

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


@Serializable
data class ProductCategoryEntity(
    @BsonId
    val productCategoryId:String = ObjectId().toString(),
    val categoryName:String,
    val subCategories:List<String> = emptyList(),
    val image:String?=null
)
