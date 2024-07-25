package example.com.dao.products.product_category

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.products.product_subcategory.ProductSubCategoryEntity
import example.com.utils.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase

class ProductCategoryDaoImpl(
    db: CoroutineDatabase
) : ProductCategoryDao {

    private val category = db.getCollection<ProductCategoryEntity>("product_category")
    override suspend fun createProductCategory(addProductCategory: String, imageUrl: String): Boolean {
        val productCategory = ProductCategoryEntity(
            categoryName = addProductCategory, image = imageUrl
        )
        val result = category.insertOne(productCategory)
        return result.wasAcknowledged()
    }

    override suspend fun getProductCategory(paging: PagingData): List<ProductCategoryEntity> {
        return withContext(Dispatchers.IO) {
            category.find().limit(paging.limit).skip(paging.offset).toList()
        }
    }

    override suspend fun updateProductCategory(id: String, subCategoryEntity: ProductSubCategoryEntity): Boolean {
        val data = category.findOneById(id)
        return if (data != null) {
            data.subCategories.add(subCategoryEntity)
            val updateResult = category.updateOne(
                Filters.eq("_id", id),
                Updates.set("subCategories", data.subCategories)
            )
            updateResult.matchedCount > 0
        } else {
            false
        }
    }



    override suspend fun deleteProductCategory(deleteProductCategory: String): Boolean {
        val filter = Filters.eq("_id", deleteProductCategory)
        return withContext(Dispatchers.IO) {
            val result = category.deleteOne(filter)
            result.deletedCount > 0
        }
    }
}