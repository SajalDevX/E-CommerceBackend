package example.com.dao.products.product

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import example.com.dao.products.product.entity.ProductEntity
import example.com.dao.products.product.entity.ProductImages
import example.com.model.AddProduct
import example.com.model.ProductWithFilter
import example.com.model.UpdateProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineDatabase

class ProductDaoImpl(
    db: CoroutineDatabase
) : ProductDao {

    private val products = db.getCollection<ProductEntity>("products")
    private val productImages = db.getCollection<ProductImages>("product_img")
    override suspend fun addProduct(userId: String, addProduct: AddProduct): Boolean {
        val insertedProduct = ProductEntity(
            userId = userId,
            categoryId = addProduct.categoryId,
            subCategoryId = addProduct.subCategoryId,
            brandId = addProduct.brandId,
            productName = addProduct.productName,
            productCode = addProduct.productCode,
            productQuantity = addProduct.productQuantity,
            productDetail = addProduct.productDetail,
            price = addProduct.price,
            discountPrice = addProduct.discountPrice,
            videoLink = addProduct.videoLink,
            hotDeal = addProduct.hotDeal,
            buyOneGetOne = addProduct.buyOneGetOne,
            imageOne = addProduct.imageOne,
            imageTwo = addProduct.imageTwo
        )
        val result = products.insertOne(insertedProduct)
        return result.wasAcknowledged()
    }

    override suspend fun updateProduct(userId: String, productId: String, updateProduct: UpdateProduct): Boolean {
        val filter = Filters.and(
            Filters.eq("_id", productId), Filters.eq("userId", userId)
        )
        val updates = mutableListOf<Bson>()

        updateProduct.categoryId.let { updates.add(Updates.set("categoryId", it)) }
        updateProduct.subCategoryId?.let { updates.add(Updates.set("subCategoryId", it)) }
        updateProduct.brandId?.let { updates.add(Updates.set("brandId", it)) }
        updateProduct.productName.let { updates.add(Updates.set("productName", it)) }
        updateProduct.productCode?.let { updates.add(Updates.set("productCode", it)) }
        updateProduct.productQuantity.let { updates.add(Updates.set("productQuantity", it)) }
        updateProduct.productDetail.let { updates.add(Updates.set("productDetail", it)) }
        updateProduct.price.let { updates.add(Updates.set("price", it)) }
        updateProduct.discountPrice?.let { updates.add(Updates.set("discountPrice", it)) }
        updateProduct.videoLink?.let { updates.add(Updates.set("videoLink", it)) }
        updateProduct.hotDeal?.let { updates.add(Updates.set("hotDeal", it)) }
        updateProduct.buyOneGetOne?.let { updates.add(Updates.set("buyOneGetOne", it)) }
        updateProduct.imageOne?.let { updates.add(Updates.set("imageOne", it)) }
        updateProduct.imageTwo?.let { updates.add(Updates.set("imageTwo", it)) }

        val update = Updates.combine(updates)

        val updateResult: UpdateResult = products.updateOne(filter, update)

        return updateResult.wasAcknowledged()
    }

    override suspend fun getProduct(productQuery: ProductWithFilter): List<ProductEntity> {
        val filters = mutableListOf<Bson>()

        productQuery.maxPrice?.let {
            filters.add(Filters.lte("price", it))
        }
        productQuery.minPrice?.let {
            filters.add(Filters.gte("price", it))
        }
        productQuery.categoryId?.let {
            filters.add(Filters.eq("categoryId", it))
        }
        productQuery.subCategoryId?.let {
            filters.add(Filters.eq("subCategoryId", it))
        }
        productQuery.brandId?.let {
            filters.add(Filters.eq("brandId", it))
        }
        val queryFilter = if (filters.isNotEmpty()) Filters.and(filters) else Filters.empty()

        return withContext(Dispatchers.IO) {
            products.find(queryFilter).limit(productQuery.limit).skip(productQuery.offset).toList()
        }
    }

    override suspend fun getProductById(userId: String, productId: String): ProductEntity? {
        val queryFilter = Filters.and(
            Filters.eq("_id", productId),
            Filters.eq("userId", userId)
        )

        return withContext(Dispatchers.IO) {
            products.findOne(queryFilter)
        }
    }

    override suspend fun productDetail(productId: String): ProductEntity? {
        val filter = Filters.eq("_id", productId)

        return withContext(Dispatchers.IO) {
            products.findOne(filter)
        }
    }

    override suspend fun deleteProduct(userId: String, productId: String): Boolean {
        val queryFilter = Filters.and(
            Filters.eq("_id", productId),
            Filters.eq("userId", userId)
        )

        return withContext(Dispatchers.IO) {
            val result = products.deleteOne(queryFilter)
            result.deletedCount>0
        }
    }

    override suspend fun uploadProductImages(userId: String, productId: String, images: String): Boolean {
        val imageEntity = ProductImages(
            userId = userId,
            productId = productId,
            imageUrl = images
        )

        val insertedImageResult = productImages.insertOne(imageEntity)

        if (insertedImageResult.wasAcknowledged()) {
            val updateResult = products.updateOne(
                Filters.eq("_id", productId),
                Updates.addToSet("images", imageEntity.imageUrl)
            )
            return updateResult.wasAcknowledged()
        }

        return false
    }

}