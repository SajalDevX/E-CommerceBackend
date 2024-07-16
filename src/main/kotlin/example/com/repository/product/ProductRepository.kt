package example.com.repository.product

import example.com.model.*
import example.com.utils.Response

interface ProductRepository {
    suspend fun addProduct(userId:String,params:AddProduct):Response<ProductResponse>
    suspend fun updateProduct(userId: String, productId: String, updateProduct: UpdateProduct):Response<ProductResponse>
    suspend fun getProducts(productQueryParams: ProductWithFilter):Response<ProductResponse>
    suspend fun getProductById( productId: String):Response<ProductResponse>
    suspend fun deleteProduct(userId: String, productId: String):Response<ProductResponse>
    suspend fun uploadProductImages(userId: String, productId: String, images: String):Response<ProductResponse>
    suspend fun getProductDetail(productId:String):Response<ProductResponse>
}