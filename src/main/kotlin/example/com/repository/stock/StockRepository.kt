package example.com.repository.stock

import example.com.model.StockResponse
import example.com.utils.Response

interface StockRepository {
    suspend fun increaseStocks(productId: String, shopId: String, quantity: Int):Response<StockResponse>
    suspend fun decreaseStocks(productId: String, shopId: String, quantity: Int):Response<StockResponse>
    suspend fun getStocks(productId: String, shopId: String):Response<StockResponse>

}