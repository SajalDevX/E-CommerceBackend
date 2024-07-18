package example.com.dao.products.stock

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class StockEntity(
    val stockId:String=ObjectId().toString(),
    val productId: String,
    val shopId:String,
    val quantity:Int
)