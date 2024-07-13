package example.com.repository.shipping

import example.com.model.AddShipping
import example.com.model.ShippingResponse
import example.com.model.UpdateShipping
import example.com.utils.Response

interface ShippingRepository {
    suspend fun addShipping(userId: String, addShipping: AddShipping): Response<ShippingResponse>
    suspend fun updateShipping(userId: String, updateShipping: UpdateShipping): Response<ShippingResponse>
    suspend fun getShipping(userId: String, orderId: String): Response<ShippingResponse>
    suspend fun deleteShipping(userId: String, orderId: String): Response<ShippingResponse>
}