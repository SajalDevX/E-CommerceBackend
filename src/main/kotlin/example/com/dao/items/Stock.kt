package example.com.dao.items

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val productId: String,
    val shopId:String,
    val quantity:Int
)