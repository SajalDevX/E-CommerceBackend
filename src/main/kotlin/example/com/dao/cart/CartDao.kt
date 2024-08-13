package example.com.dao.cart

import example.com.utils.PagingData

interface CartDao {
    suspend fun addToCart(userId:String,productId:String,qty:Int):CartEntity?
    suspend fun getCartItems(userId:String,pagingData: PagingData):CartEntity?
    suspend fun updateCartQuantity(userId:String,productId:String,qty: Int):CartEntity?
    suspend fun removeCartItem(userId:String,qty: Int,productId:String):Boolean
    suspend fun deleteAllFromCart(userId:String):Boolean
    suspend fun deleteSelectedItemFromCart(userId:String,productId:String):Boolean


}