package example.com.repository.cart

import example.com.dao.cart.CartDao
import example.com.model.CartResponse
import example.com.utils.PagingData
import example.com.utils.Response
import io.ktor.http.*

class CartRepositoryImpl(
    private val dao: CartDao
) : CartRepository {
    override suspend fun addToCart(userId: String, productId: String, qty: Int): Response<CartResponse> {
        val result = dao.addToCart(userId, productId, qty)
        return if (result != null) {
            Response.Success(
                CartResponse(
                    success = true,
                    cart = result,
                    message = "Item added to cart successfully $qty"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                CartResponse(
                    success = false,
                    message = "Item with $userId & $productId & $qty could not be added to cart"
                )
            )
        }
    }

    override suspend fun getCartItems(userId: String, pagingData: PagingData): Response<CartResponse> {
        val result = dao.getCartItems(userId, pagingData)
        return if (result != null) {
            Response.Success(
                CartResponse(
                    success = true,
                    cart = result,
                    message = "Cart Items fetched successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                CartResponse(
                    success = false,
                    message = "Cart items could not be fetched"
                )
            )
        }
    }

    override suspend fun updateCartQuantity(userId: String, productId: String, qty: Int): Response<CartResponse> {
        val result = dao.updateCartQuantity(userId, productId,qty)
        return if (result != null) {
            Response.Success(
                CartResponse(
                    success = true,
                    cart = result,
                    message = "Cart Items updated successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                CartResponse(
                    success = false,
                    message = "Cart items could not be updated"
                )
            )
        }
    }

    override suspend fun removeCartItem(userId: String, qty: Int, productId: String): Response<CartResponse> {
        val result = dao.removeCartItem(userId, qty,productId)
        return if (result) {
            Response.Success(
                CartResponse(
                    success = true,
                    message = "Cart Items deleted successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                CartResponse(
                    success = false,
                    message = "Cart items could not be deleted"
                )
            )
        }    }

    override suspend fun deleteAllFromCart(userId: String): Response<CartResponse> {
        val result = dao.deleteAllFromCart(userId)
        return if (result) {
            Response.Success(
                CartResponse(
                    success = true,
                    message = "Cart Items deleted successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                CartResponse(
                    success = false,
                    message = "Cart items could not be deleted"
                )
            )
        }
    }
}