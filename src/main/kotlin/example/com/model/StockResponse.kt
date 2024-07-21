package example.com.model

import example.com.dao.products.stock.StockEntity
import kotlinx.serialization.Serializable

@Serializable
data class StockResponse(
    val success:Boolean,
    val data:StockEntity?=null,
    val message:String,
)