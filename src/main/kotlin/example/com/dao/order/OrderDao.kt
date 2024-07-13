package example.com.dao.order

import example.com.dao.order.entity.OrderEntity
import example.com.mappers.OrderStatus
import example.com.model.AddOrder
import example.com.utils.PagingData

interface OrderDao {
    suspend fun createOrder(userId:String,addOrder: AddOrder):OrderEntity?
    suspend fun getOrders(userId: String,pagingData: PagingData):List<OrderEntity>
    suspend fun updateOrder(orderId:String,orderStatus: OrderStatus):OrderEntity?

}
