package example.com.dao.products.product_subcategory

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ProductSubCategoryEntity(
    @BsonId
    val subCategoryId:String = ObjectId().toString(),
    val categoryId: String,
    val subCategoryName: String,
    val image: String?=null
)