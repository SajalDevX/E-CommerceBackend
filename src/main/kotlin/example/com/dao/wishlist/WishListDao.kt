package example.com.dao.wishlist

import example.com.dao.products.product.entity.ProductEntity

interface WishListDao {
    suspend fun addToWishList(userId:String,productId:String):WishlistEntity?
    suspend fun getWishList(userId:String):List<String>
    suspend fun deleteFromWishList(userId:String,productId:String):Boolean

}