package example.com.dao.cart

import com.mongodb.client.model.Filters
import example.com.utils.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.slf4j.LoggerFactory

class CartDaoImpl(
    db: CoroutineDatabase
) : CartDao {
    private val cart = db.getCollection<CartEntity>("cart")
    private val logger = LoggerFactory.getLogger(CartDaoImpl::class.java)

    override suspend fun addToCart(userId: String, productId: String, qty: Int): CartEntity? {
        return try {
            val existingCart = cart.findOne(CartEntity::userId eq userId)
            logger.debug("Found existing cart: {}", existingCart)

            if (existingCart == null) {
                val newCart = CartEntity(
                    userId = userId,
                    products = mapOf(productId to qty)
                )
                val result = cart.insertOne(newCart)
                if (result.wasAcknowledged()) {
                    logger.debug("New cart created for user $userId with product $productId and quantity $qty")
                    newCart
                } else {
                    logger.error("Failed to create new cart for user $userId")
                    null
                }
            } else {
                val updateProducts = existingCart.products.toMutableMap()
                updateProducts[productId] = updateProducts.getOrDefault(productId, 0) + qty
                logger.debug("Updated products map: {}", updateProducts)

                val updatedCart = existingCart.copy(products = updateProducts)
                val result = cart.updateOne(CartEntity::userId eq userId, updatedCart)

                if (result.wasAcknowledged()) {
                    logger.debug("Updated cart for user $userId with product $productId and new quantity ${updateProducts[productId]}")
                    updatedCart
                } else {
                    logger.error("Failed to update cart for user $userId")
                    null
                }
            }
        } catch (e: Exception) {
            logger.error("Exception in addToCart: ${e.message}")
            null
        }
    }

    override suspend fun getCartItems(userId: String, pagingData: PagingData): CartEntity? {
        val filters = Filters.eq("userId", userId)
        return withContext(Dispatchers.IO) {
            val cartData = cart.findOne(filters)
            cartData
        }
    }

    override suspend fun updateCartQuantity(userId:String, productId:String, qty: Int): CartEntity? {
        val existingCart = cart.findOne(CartEntity::userId eq userId)
        return if (existingCart != null) {
            val updatedProducts = existingCart.products.toMutableMap()
            if (updatedProducts.containsKey(productId)) {
                // Increase quantity by 1
                updatedProducts[productId] = updatedProducts[productId]!! + 1
                val updatedCart = existingCart.copy(products = updatedProducts)
                cart.updateOne(CartEntity::userId eq userId, updatedCart)
                updatedCart
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun removeCartItem(userId:String, qty: Int, productId:String): Boolean {
        val existingCart = cart.findOne(CartEntity::userId eq userId)
        return if (existingCart != null) {
            val updatedProducts = existingCart.products.toMutableMap()
            if (updatedProducts.containsKey(productId)) {
                val currentQty = updatedProducts[productId]!!
                if (currentQty <= 1) {
                    // Remove the item from the cart if the quantity is 1 or less
                    updatedProducts.remove(productId)
                } else {
                    // Decrease the quantity by 1
                    updatedProducts[productId] = currentQty - 1
                }
                val updatedCart = existingCart.copy(products = updatedProducts)
                val result = cart.updateOne(CartEntity::userId eq userId, updatedCart)
                result.wasAcknowledged()  // Return true if the update was acknowledged
            } else {
                false
            }
        } else {
            false
        }
    }


    override suspend fun deleteAllFromCart(userId: String): Boolean {
        val existingCart = cart.findOne(CartEntity::userId eq userId)
        return if (existingCart != null) {
            val updatedCart = existingCart.copy(products = emptyMap())
            cart.updateOne(CartEntity::userId eq userId, updatedCart)
            true
        } else {
            false
        }
    }
}
