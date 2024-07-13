package example.com.model


import example.com.dao.shipping.ShippingEntity
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class AddShipping(
    val orderId: String,
    val shippingAddress: String,
    val shipCity: String,
    val shipPhone: Int,
    val shipName: String,
    val shipEmail: String,
    val shipCountry: String
)

@Serializable
data class UpdateShipping(
    val orderId: String,
    val shipAddress: String?,
    val shipCity: String?,
    val shipPhone: Int?,
    val shipName: String?,
    val shipEmail: String?,
    val shipCountry: String?
)
@Serializable
data class ShippingResponse(
    val success:Boolean,
    val message:String,
    val data:ShippingEntity?=null
)