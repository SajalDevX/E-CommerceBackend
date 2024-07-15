package example.com.repository.cart

import example.com.model.CartResponse
import example.com.utils.PagingData
import example.com.utils.Response

interface CartRepository {
    suspend fun addToCart(userId:String,productId:String,qty:Int): Response<CartResponse>
    suspend fun getCartItems(userId:String,pagingData: PagingData): Response<CartResponse>
    suspend fun updateCartQuantity(userId:String,productId:String,qty: Int): Response<CartResponse>
    suspend fun removeCartItem(userId:String,qty: Int,productId:String):Response<CartResponse>
    suspend fun deleteAllFromCart(userId:String):Response<CartResponse>

}
