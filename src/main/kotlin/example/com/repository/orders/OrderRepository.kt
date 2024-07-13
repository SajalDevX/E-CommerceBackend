package example.com.repository.orders

import example.com.mappers.OrderStatus
import example.com.model.AddOrder
import example.com.model.OrderResponse
import example.com.utils.PagingData
import example.com.utils.Response

interface OrderRepository {
    suspend fun createOrder(userId:String,addOrder: AddOrder): Response<OrderResponse>
    suspend fun getOrders(userId: String,pagingData: PagingData):Response<OrderResponse>
    suspend fun updateOrder(orderId:String,orderStatus: OrderStatus): Response<OrderResponse>
}