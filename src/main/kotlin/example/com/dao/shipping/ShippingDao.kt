package example.com.dao.shipping

import example.com.model.AddShipping
import example.com.model.UpdateShipping

interface ShippingDao {
    suspend fun addShipping(userId:String,addShipping: AddShipping):ShippingEntity?
    suspend fun getShipping(userId:String,orderId:String):ShippingEntity?
    suspend fun updateShipping(userId:String,updateShipping:UpdateShipping):ShippingEntity?
    suspend fun deleteShipping(userId:String,orderId:String):Boolean
}