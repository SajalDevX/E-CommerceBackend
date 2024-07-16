package example.com.dao.wishlist

import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import org.slf4j.LoggerFactory

class WishListDaoImpl(
    db: CoroutineDatabase
) : WishListDao {
    private val wishlist = db.getCollection<WishlistEntity>("wishlist")
    private val log = LoggerFactory.getLogger(WishListDaoImpl::class.java)

    override suspend fun addToWishList(userId: String, productId: String): WishlistEntity? {
        return withContext(Dispatchers.IO) {
            try {
                val wishlistExist = wishlist.findOne(Filters.eq("userId", userId))
                if (wishlistExist == null) {
                    val newWishList = WishlistEntity(
                        userId = userId,
                        products = listOf(productId),
                    )
                    val result = wishlist.insertOne(newWishList)
                    if (result.wasAcknowledged()) {
                        newWishList
                    } else {
                        null
                    }
                } else {
                    val updatedProducts = wishlistExist.products.toMutableList().apply { add(productId) }
                    val updateResult = wishlist.updateOne(
                        Filters.eq("userId", userId),
                        setValue(WishlistEntity::products, updatedProducts)
                    )
                    if (updateResult.wasAcknowledged()) {
                        wishlistExist.copy(products = updatedProducts)
                    } else {
                        null
                    }
                }
            } catch (e: Exception) {
                log.error("Error adding to wishlist for userId: $userId", e)
                null
            }
        }
    }

    override suspend fun getWishList(userId: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val items = wishlist.findOne(Filters.eq("userId", userId))
                items?.products ?: emptyList()
            } catch (e: Exception) {
                log.error("Error fetching wishlist for userId: $userId", e)
                emptyList()
            }
        }
    }

    override suspend fun deleteFromWishList(userId: String, productId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val updateResult = wishlist.updateOne(
                    Filters.eq("userId", userId),
                    org.litote.kmongo.pull(WishlistEntity::products, productId)
                )
                updateResult.modifiedCount > 0
            } catch (e: Exception) {
                log.error("Error deleting from wishlist for userId: $userId and productId: $productId", e)
                false
            }
        }
    }
}
