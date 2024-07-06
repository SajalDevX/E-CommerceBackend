package example.com.dao.product.entity

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Brand(
    val brandId:String = ObjectId().toString(),
    val brandName:String,
    val brandLogo:String?
)