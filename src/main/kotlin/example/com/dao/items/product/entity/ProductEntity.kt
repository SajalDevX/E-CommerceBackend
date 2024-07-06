package example.com.dao.items.product.entity

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ProductEntity(
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
    val imageOne: String?,
    val imageTwo: String?,
)

@Serializable
data class ProductRating(
    val ratingId:String = ObjectId().toString(),
    val productId:String,
    val userId:String,
    val stars:Int,
    val comment:String,
    val likes:Int
)