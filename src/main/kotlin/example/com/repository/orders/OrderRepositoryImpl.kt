package example.com.repository.orders

import example.com.dao.order.OrderDao
import example.com.mappers.OrderStatus
import example.com.model.AddOrder
import example.com.model.OrderData
import example.com.model.OrderResponse
import example.com.model.ProductResponse
import example.com.utils.PagingData
import example.com.utils.Response
import io.ktor.http.*

class OrderRepositoryImpl(
    private val dao:OrderDao
) : OrderRepository {
    override suspend fun createOrder(userId: String, addOrder: AddOrder): Response<OrderResponse> {
        val result = dao.createOrder(userId,addOrder)
        return if (result==null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = OrderResponse(
                    success = false,
                    message = "Could not add your order"
                )
            )
        } else {
            Response.Success(
                data = OrderResponse(
                    success = true,
                    data = OrderData(
                        order = result
                    ),
                    message = "Order added successfully"
                )
            )
        }
    }
    override suspend fun getOrders(userId: String, pagingData: PagingData): Response<OrderResponse> {
        val result = dao.getOrders(userId,pagingData)
        return if(result.isEmpty()){
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = OrderResponse(
                    success = false,
                    message = "Could not get your orders"
                )
            )
        }else{
            Response.Success(
                OrderResponse(
                    success = true,
                    data = OrderData(
                        orders = result
                    ),
                    message = "Order fetched successfully"
                )
            )
        }
    }
    override suspend fun updateOrder(
        orderId: String,
        orderStatus: OrderStatus
    ): Response<OrderResponse> {
        val result = dao.updateOrder( orderId, orderStatus)
        return if (result==null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = OrderResponse(
                    success = false,
                    message = "Could not update your product"
                )
            )
        } else {
            Response.Success(
                data = OrderResponse(
                    success = true,
                    data = OrderData(
                        order = result
                    ),
                    message = "Product updated successfully"
                )
            )
        }
    }
}