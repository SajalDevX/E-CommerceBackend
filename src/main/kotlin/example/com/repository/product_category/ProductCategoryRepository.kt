package example.com.repository.product_category

import example.com.model.PagingData
import example.com.model.ProductCategoryResponse
import example.com.utils.Response

interface ProductCategoryRepository {

    suspend fun createProductCategory(productCategoryName: String):Response<ProductCategoryResponse>
    suspend fun getProductCategory(paging: PagingData):Response<ProductCategoryResponse>
    suspend fun updateProductCategory(productCategoryId:String,productCategoryName:String):Response<ProductCategoryResponse>
    suspend fun deleteProductCategory(productCategoryId: String):Response<ProductCategoryResponse>
}