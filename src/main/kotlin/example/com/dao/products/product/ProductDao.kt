package example.com.dao.products.product

import example.com.dao.products.product.entity.ProductEntity
import example.com.model.AddProduct
import example.com.model.ProductWithFilter
import example.com.model.UpdateProduct

interface ProductDao {
    suspend fun addProduct(userId: String, addProduct: AddProduct): Boolean
    suspend fun updateProduct(userId: String, productId: String, updateProduct: UpdateProduct):Boolean
    suspend fun getProduct(productQuery: ProductWithFilter): List<ProductEntity>
    suspend fun getProductById( productId: String): ProductEntity?
    suspend fun productDetail(productId: String): ProductEntity?
    suspend fun deleteProduct(userId: String, productId: String): Boolean
    suspend fun uploadProductImages(userId:String,productId: String,images:String):Boolean
    suspend fun getProducts(productIds: List<String>):List<ProductEntity>
}