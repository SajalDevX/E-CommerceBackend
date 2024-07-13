package example.com.dao.shop

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.shop.entity.ShopCategoryEntity
import example.com.dao.shop.entity.ShopEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ShopDaoImpl(
    db: CoroutineDatabase
) : ShopDao {
    private val shop = db.getCollection<ShopEntity>("shop")
    private val shopCategory = db.getCollection<ShopCategoryEntity>("shop_category")

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
}