package example.com.dao.products.brand

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.utils.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class BrandDaoImpl(
    db: CoroutineDatabase
) : BrandDao {
    private val brand = db.getCollection<BrandEntity>("brand")
    override suspend fun createBrand(brandName: String): Boolean {
        val brandExists = brand.findOne(BrandEntity::brandName eq brandName)
        return if (brandExists == null) {
            val productBrand = BrandEntity(
                brandName = brandName
            )
            val result = brand.insertOne(productBrand)
            return result.wasAcknowledged()
        } else {
            false
        }
    }

    override suspend fun getBrand(paging: PagingData): List<BrandEntity> {
        return withContext(Dispatchers.IO) {
            brand.find().limit(paging.limit).skip(paging.offset).toList()
        }
    }

    override suspend fun updateBrand(brandId: String, brandName: String): Boolean {
        val filters = Filters.eq("_id", brandId)
        val update = Updates.set("brandName", brandName)
        val result: UpdateResult = brand.updateOne(filters, update)
        return result.modifiedCount > 0
    }

    override suspend fun deleteBrand(brandId: String): Boolean {
        val filters = Filters.eq("_id", brandId)
        return withContext(Dispatchers.IO) {
            val result = brand.deleteOne(filters)
            result.deletedCount > 0
        }
    }
}