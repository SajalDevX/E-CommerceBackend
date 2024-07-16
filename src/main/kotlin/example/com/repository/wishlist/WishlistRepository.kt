package example.com.repository.wishlist

import example.com.model.WishlistResponse
import example.com.utils.Response

interface WishlistRepository {
    suspend fun addToWishlist(userId:String,productId:String):Response<WishlistResponse>
    suspend fun getWishlist(userId:String):Response<WishlistResponse>
    suspend fun deleteFromWishlist(userId:String,productId:String):Response<WishlistResponse>
}