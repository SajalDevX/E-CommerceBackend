package example.com.dao.shop

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.order.entity.OrderEntity
import example.com.dao.products.product.entity.ProductEntity
import example.com.dao.shop.entity.Orders
import example.com.dao.shop.entity.ShopCategoryEntity
import example.com.dao.shop.entity.ShopEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.addToSet
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ShopDaoImpl(
    db: CoroutineDatabase
) : ShopDao {
    private val shop = db.getCollection<ShopEntity>("shop")
    private val shopCategory = db.getCollection<ShopCategoryEntity>("shop_category")
    private val products = db.getCollection<ProductEntity>("products")
    override suspend fun createShopCategory(shopCategoryName: String): Boolean {
        val shopCategoryExist =
            shopCategory.find(ShopCategoryEntity::shopCategoryName eq shopCategoryName).toList().singleOrNull()
        return if (shopCategoryExist == null) {
            val shopCategoryData = ShopCategoryEntity(
                shopCategoryName = shopCategoryName
            )
            val result = shopCategory.insertOne(shopCategoryData)
            result.wasAcknowledged()
        } else {
            false
        }
    }

    override suspend fun getShopCategories(limit: Int, offset: Int): List<ShopCategoryEntity> {
        return withContext(Dispatchers.IO) {
            shopCategory.find().limit(limit).skip(offset).toList()
        }
    }


    override suspend fun updateShopCategory(shopCategoryId: String, shopCategoryName: String): Boolean {
        val filters = Filters.eq("_id", shopCategoryId)
        val updates = Updates.set("shopCategoryName", shopCategoryName)
        val result: UpdateResult = shopCategory.updateOne(filters, updates)
        return result.modifiedCount > 0
    }

    override suspend fun deleteShopCategory(shopCategoryId: String): Boolean {
        val filter = Filters.eq("_id", shopCategoryId)
        return withContext(Dispatchers.IO) {
            val result = shopCategory.deleteOne(filter)
            result.deletedCount > 0
        }
    }

    override suspend fun createShop(userId: String, shopCategoryId: String, shopName: String): ShopEntity? {
        val shopNameExists = shop.find(ShopEntity::shopName eq shopName).toList().singleOrNull()
        return if (shopNameExists == null) {
            val filter = Filters.eq("_id", shopCategoryId)
            val shopCategoryData = shopCategory.findOne(filter)
            val shopData = ShopEntity(
                userId = userId,
                shopCategoryName = shopCategoryData!!.shopCategoryName,
                shopCategoryId = shopCategoryId,
                shopName = shopName
            )
            val result = shop.insertOne(shopData)
            if (result.wasAcknowledged()) {
                shopData
            } else {
                null
            }
        } else {
            null
        }
    }

    override suspend fun getShop(userId: String): ShopEntity? {
        return withContext(Dispatchers.IO) {
            shop.findOne(ShopEntity::userId eq userId)
        }
    }
    override suspend fun updateOrders(orderEntity: OrderEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val shopUpdates = mutableListOf<UpdateResult>()
            val userId = orderEntity.userId
            val orderId = orderEntity.orderId

            // Group the order items by their corresponding seller (shop)
            val shopOrdersMap = orderEntity.orderItems.groupBy { orderItem ->
                // Find the product entity to get the seller (shop) ID
                products.findOne(ProductEntity::productId eq orderItem.productId)?.userId
            }

            // Iterate over each seller (shop) and update their orders
            shopOrdersMap.forEach { (sellerId, orderItems) ->
                sellerId?.let { validSellerId ->
                    val shopEntity = shop.findOne(ShopEntity::userId eq validSellerId)
                    shopEntity?.let {
                        orderItems.forEach { orderItem ->
                            val orderEntry = Orders(
                                productId = orderItem.productId,
                                qty = orderItem.quantity.toString(),
                                userId = userId,
                                orderId = orderId
                            )
                            // Update the shop's orders with the current order entry
                            val updateResult = shop.updateOne(
                                ShopEntity::userId eq validSellerId,
                                addToSet(ShopEntity::orders, orderEntry)
                            )
                            shopUpdates.add(updateResult)
                        }
                    }
                }
            }

            // Check if all updates were successful
            shopUpdates.all { it.modifiedCount > 0 }
        }
    }


}