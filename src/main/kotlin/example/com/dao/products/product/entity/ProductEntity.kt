package example.com.dao.products.product.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ProductEntity(
    @BsonId
    val productId: String = ObjectId().toString(),
    val userId:String,
    val categoryId: String,
    val subCategoryId: String?,
    val brandId: String?,
    val productName: String,
    val productCode: String?,
    val productQuantity: Int,
    val productDetail: String,
    val price: Double,
    val discountPrice: Double?,
    val videoLink: String?,
    val hotDeal: String?,
    val ratingId:List<String> = emptyList(),
    val buyOneGetOne: String?,
)
