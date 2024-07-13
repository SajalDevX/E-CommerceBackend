package example.com.dao.shipping

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ShippingEntity(
    @BsonId
    val shippingId: String = ObjectId().toString(),
    var userId: String,
    var orderId: String,
    var shipAddress: String,
    var shipCity: String,
    var shipPhone: Int,
    var shipName: String?,
    var shipEmail: String?,
    var shipCountry: String?
)