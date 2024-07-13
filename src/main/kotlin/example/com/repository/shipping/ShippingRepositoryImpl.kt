package example.com.repository.shipping

import example.com.dao.shipping.ShippingDao
import example.com.model.AddShipping
import example.com.model.ShippingResponse
import example.com.model.UpdateShipping
import example.com.utils.Response
import io.ktor.http.*

class ShippingRepositoryImpl(
    private val dao: ShippingDao
) : ShippingRepository {
    override suspend fun addShipping(userId: String, addShipping: AddShipping): Response<ShippingResponse> {
        val result = dao.addShipping(userId, addShipping)
        return if (result != null) {
            Response.Success(
                ShippingResponse(
                    success = true,
                    message = "Order Shipped Successfully",
                    data = result
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShippingResponse(
                    success = false,
                    message = "Order Could not be shipped"
                )
            )
        }
    }

    override suspend fun updateShipping(userId: String, updateShipping: UpdateShipping): Response<ShippingResponse> {
        val result = dao.updateShipping(userId,updateShipping)
        return if (result != null) {
            Response.Success(
                ShippingResponse(
                    success = true,
                    message = "Shipped Order Successfully",
                    data = result
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShippingResponse(
                    success = false,
                    message = "Shipped Order Could not be updated"
                )
            )
        }
    }

    override suspend fun getShipping(userId: String, orderId: String): Response<ShippingResponse> {
        val result = dao.getShipping(userId,orderId)
        return if (result!=null) {
            Response.Success(
                ShippingResponse(
                    success = true,
                    message = "Shipped order fetched successfully",
                    data = result
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShippingResponse(
                    success = false,
                    message = "Shipped Order Could not be fetched"
                )
            )
        }
    }

    override suspend fun deleteShipping(userId: String, orderId: String): Response<ShippingResponse> {
        val result = dao.deleteShipping(userId,orderId)
        return if (result) {
            Response.Success(
                ShippingResponse(
                    success = true,
                    message = "Shipped Order Deleted",
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShippingResponse(
                    success = false,
                    message = "Shipped Order Could not be Deleted"
                )
            )
        }
    }
}