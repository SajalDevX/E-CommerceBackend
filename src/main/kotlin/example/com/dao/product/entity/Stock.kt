package example.com.dao.product.entity

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val productId: String,
    val shopId:String,
    val quantity:Int
)