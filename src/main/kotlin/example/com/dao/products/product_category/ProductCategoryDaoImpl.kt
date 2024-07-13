package example.com.dao.products.product_category

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.utils.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase

class ProductCategoryDaoImpl(
    db: CoroutineDatabase
) : ProductCategoryDao {

    private val category = db.getCollection<ProductCategoryEntity>("product_category")
    override suspend fun createProductCategory(addProductCategory: String): Boolean {
        val productCategory = ProductCategoryEntity(
            categoryName = addProductCategory,
        )
        val result = category.insertOne(productCategory)
        return result.wasAcknowledged()
    }

    override suspend fun getProductCategory(paging: PagingData): List<ProductCategoryEntity> {
        return withContext(Dispatchers.IO) {
            category.find().limit(paging.limit).skip(paging.offset).toList()
        }
    }

    override suspend fun updateProductCategory(id: String,name:String): Boolean {
        val filter = Filters.eq("_id", id)
        val update = Updates.set("categoryName", name)
        val result: UpdateResult = category.updateOne(filter, update)
        return result.modifiedCount>0
    }

    override suspend fun deleteProductCategory(deleteProductCategory: String): Boolean {
        val filter = Filters.eq("_id", deleteProductCategory)
        return withContext(Dispatchers.IO) {
            val result = category.deleteOne(filter)
            result.deletedCount > 0
        }
    }
}