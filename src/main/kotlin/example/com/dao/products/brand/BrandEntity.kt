package example.com.dao.products.brand

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class BrandEntity(
    @BsonId
    val brandId:String = ObjectId().toString(),
    val brandName:String,
    val brandLogo:String?=null
)