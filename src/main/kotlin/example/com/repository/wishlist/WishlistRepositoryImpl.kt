package example.com.repository.wishlist

import example.com.dao.products.product.ProductDao
import example.com.dao.products.product.entity.ProductEntity
import example.com.dao.wishlist.WishListDao
import example.com.model.ShopCategoryResponse
import example.com.model.WishlistResponse
import example.com.utils.Response
import io.ktor.http.*

class WishlistRepositoryImpl(
    private val wishlistDao:WishListDao,
    private val productDao:ProductDao
) : WishlistRepository {
    override suspend fun addToWishlist(userId: String, productId: String): Response<WishlistResponse> {
        val result = wishlistDao.addToWishList(userId,productId)
        val productItem  = productDao.getProductById(productId)
        return if (result!=null) {
            Response.Success(
                WishlistResponse(
                    success = true,
                    product = productItem,
                    message = "Product added to wishlist"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                WishlistResponse(
                    success = false,
                    message = "Product could not be added to wishlist"

                )
            )
        }
    }


    override suspend fun getWishlist(userId: String): Response<WishlistResponse> {
        val result = wishlistDao.getWishList(userId)
        val list = productDao.getProducts(result)
        return if (result.isNotEmpty()) {
            Response.Success(
                WishlistResponse(
                    success = true,
                    products = list,
                    message = "Wishlist fetched successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                WishlistResponse(
                    success = false,
                    message = "Wishlist could not be fetched"

                )
            )
        }
    }

    override suspend fun deleteFromWishlist(userId: String, productId: String): Response<WishlistResponse> {
        val result = wishlistDao.deleteFromWishList(userId,productId)
        return if (result) {
            Response.Success(
                WishlistResponse(
                    success = true,
                    message = "Product removed from wishlist successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                WishlistResponse(
                    success = false,
                    message = "Product could not be removed"

                )
            )
        }
    }
}