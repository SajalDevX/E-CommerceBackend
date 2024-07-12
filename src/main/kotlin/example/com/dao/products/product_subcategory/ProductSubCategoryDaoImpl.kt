package example.com.dao.products.product_subcategory

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase

class ProductSubCategoryDaoImpl(
    db: CoroutineDatabase
) : ProductSubCategoryDao {

    private val subCategory = db.getCollection<ProductSubCategoryEntity>("product_sub_category")
    override suspend fun createProductSubcategory(categoryId: String, subCategoryName: String): Boolean {
        val productSubCategoryEntity = ProductSubCategoryEntity(
            categoryId = categoryId, subCategoryName = subCategoryName
        )
        val result = subCategory.insertOne(productSubCategoryEntity)
        return result.wasAcknowledged()
    }

    override suspend fun getProductSubCategory(
        categoryId: String,
        offset: Int,
        limit: Int
    ): List<ProductSubCategoryEntity> {
        return withContext(Dispatchers.IO) {
            subCategory.find().limit(limit).skip(offset).toList()
        }
    }

    override suspend fun updateProductSubcategory(subCategoryId: String, subCategoryName: String): Boolean {
        val filter = Filters.eq("_id", subCategoryId)
        val update = Updates.set("subCategoryName", subCategoryName)
        val result: UpdateResult = subCategory.updateOne(filter, update)
        return result.wasAcknowledged()
    }

    override suspend fun deleteProductSubCategory(subCategoryId: String): Boolean {
        val filter = Filters.eq("_id", subCategoryId)
        return withContext(Dispatchers.IO) {
            val result = subCategory.deleteOne(filter)
            result.deletedCount > 0
        }
    }
}