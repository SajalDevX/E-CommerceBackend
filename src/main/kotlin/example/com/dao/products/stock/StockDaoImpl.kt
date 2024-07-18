package example.com.dao.products.stock

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.util.logging.Filter

class StockDaoImpl (
    db:CoroutineDatabase
): StockDao {
    private val stocks = db.getCollection<StockEntity>("stock")
    override suspend fun increaseStocks(productId: String, shopId: String, quantity: Int): StockEntity? {
        val filter = Filters.and(
            Filters.eq("productId", productId),
            Filters.eq("shopId", shopId)
        )
        val itemExist = stocks.findOne(filter)
        return if (itemExist != null) {
            val updatedQuantity = itemExist.quantity + quantity
            val updateResult = stocks.updateOne(
                filter,
                Updates.set("quantity", updatedQuantity)
            )
            if (updateResult.wasAcknowledged()) {
                itemExist.copy(quantity = updatedQuantity)
            } else {
                null
            }
        } else {
            val stockEntity = StockEntity(
                shopId = shopId,
                productId = productId,
                quantity = quantity
            )
            val result = stocks.insertOne(stockEntity)
            if (result.wasAcknowledged()) stockEntity else null
        }
    }


    override suspend fun decreaseStocks(productId: String, shopId: String, quantity: Int): StockEntity? {
        val filter = Filters.and(
            Filters.eq("productId", productId),
            Filters.eq("shopId", shopId)
        )
        val itemExist = stocks.findOne(filter)
        return if (itemExist != null) {
            val updatedQuantity = itemExist.quantity - quantity
            val updateResult = stocks.updateOne(
                filter,
                Updates.set("quantity", updatedQuantity)
            )
            if (updateResult.wasAcknowledged()) {
                itemExist.copy(quantity = updatedQuantity)
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun getStocks(productId: String, shopId: String): StockEntity? {
        val filter = Filters.and(
            Filters.eq("productId", productId),
            Filters.eq("shopId", shopId)
        )
        return stocks.findOne(filter)
    }

}