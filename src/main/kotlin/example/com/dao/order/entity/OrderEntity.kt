package example.com.dao.order.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class OrderEntity(
    @BsonId
    val orderId: String = ObjectId().toString(),
    val userId: String,
    val paymentId: String?=null,
    val paymentType: String?=null,
    val quantity: Int,
    val subTotal: Float,
    val shippingCharge: Float =0.0f,
    val vat: Float?=null,
    val cancelOrder: Boolean = false,
    val coupon: String? = null,
    val status: String = "pending",
    val statusCode: Int = 0,
    val orderItems:List<OrderItems> = emptyList()
)

@Serializable
data class OrderItems(
    val productId: String,
    val quantity: Int
)