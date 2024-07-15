package example.com.model

import example.com.dao.cart.CartEntity
import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val success:Boolean,
    val message:String,
    val cart:CartEntity?=null
)