package example.com.model

import example.com.dao.order.entity.OrderEntity
import example.com.dao.order.entity.OrderItems
import kotlinx.serialization.Serializable

@Serializable
data class AddOrder(
    val quantity: Int,
    val subTotal: Float,
    val total: Float,
    val shippingCharge: Float,
    val orderStatus: String?="pending",
    val orderItems: MutableList<OrderItems>
)

@Serializable
data class OrderData(
    val order: OrderEntity? = null,
    val orders: List<OrderEntity> = emptyList()
)

@Serializable
data class OrderResponse(
    val success: Boolean,
    val message: String,
    val data: OrderData? = null
)