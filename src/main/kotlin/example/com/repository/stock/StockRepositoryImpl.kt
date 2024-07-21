package example.com.repository.stock

import example.com.dao.products.stock.StockDao
import example.com.model.StockResponse
import example.com.utils.Response
import io.ktor.http.*

class StockRepositoryImpl(
    private val stockDao:StockDao,
) : StockRepository {
    override suspend fun increaseStocks(productId: String, shopId: String, quantity: Int): Response<StockResponse> {
        val result = stockDao.increaseStocks(productId,shopId, quantity)
        return if(result!=null){
            Response.Success(
                StockResponse(
                    success = true,
                    message = "Stock added successfully",
                    data = result
                )
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                StockResponse(
                    success = false,
                    message = "Stock could not be added",
                )
            )
        }
    }

    override suspend fun decreaseStocks(productId: String, shopId: String, quantity: Int): Response<StockResponse> {
        val result = stockDao.decreaseStocks(productId,shopId, quantity)
        return if(result!=null){
            Response.Success(
                StockResponse(
                    success = true,
                    message = "Stock reduced successfully",
                    data = result
                )
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                StockResponse(
                    success = false,
                    message = "Stock could not be reduced",
                )
            )
        }
    }

    override suspend fun getStocks(productId: String, shopId: String): Response<StockResponse> {
        val result = stockDao.getStocks(productId,shopId)
        return if(result!=null){
            Response.Success(
                StockResponse(
                    success = true,
                    message = "Stock fetched successfully",
                    data = result
                )
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                StockResponse(
                    success = false,
                    message = "Stock could not be fetched",
                )
            )
        }
    }
}