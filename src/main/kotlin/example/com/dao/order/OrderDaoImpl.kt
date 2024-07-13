package example.com.dao.order

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.order.entity.OrderEntity
import example.com.dao.order.entity.OrderItems
import example.com.mappers.OrderStatus
import example.com.mappers.orderStatusCode
import example.com.model.AddOrder
import example.com.utils.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
class OrderDaoImpl(
    db: CoroutineDatabase
) : OrderDao {
    private val orderDb = db.getCollection<OrderEntity>("order")
    override suspend fun createOrder(userId: String, addOrder: AddOrder): OrderEntity? {
        val orderItems = addOrder.orderItems.map {
            OrderItems(
                productId = it.productId,
                quantity = it.quantity
            )
        }
        val orderData = OrderEntity(
            userId = userId,
            quantity = addOrder.quantity,
            shippingCharge = addOrder.shippingCharge,
            subTotal = addOrder.subTotal,
            orderItems = orderItems,
            )
        val result = orderDb.insertOne(orderData)
        return if (result.wasAcknowledged()) {
            orderData
        } else {
            null
        }
    }
    override suspend fun getOrders(userId: String, pagingData: PagingData): List<OrderEntity> {
        return withContext(Dispatchers.IO) {
            orderDb.find().skip(pagingData.offset).limit(pagingData.limit).toList()
        }
    }
    override suspend fun updateOrder(orderId: String, orderStatus: OrderStatus): OrderEntity? {
        val filter = Filters.and(
            Filters.eq("_id", orderId),
        )
        val orderExists = orderDb.find(filter).toList().singleOrNull()
        if (orderExists != null) {
            val update1 = Updates.set("status", orderStatus.name.lowercase())
            val update2 = Updates.set("statusCode", orderStatus.name.lowercase().orderStatusCode())
            val updates = Updates.combine(update1, update2)
            val result: UpdateResult = orderDb.updateOne(filter, updates)
            val orderData = orderDb.find(filter).toList().singleOrNull()
            return if (result.modifiedCount > 0) {
                orderData
            } else {
                null
            }
        } else {
            return null
        }
    }
}
