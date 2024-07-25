package example.com.repository.product_category

import example.com.utils.PagingData
import example.com.model.ProductCategoryResponse
import example.com.utils.Response

interface ProductCategoryRepository {

    suspend fun createProductCategory(productCategoryName: String,imageUrl:String):Response<ProductCategoryResponse>
    suspend fun getProductCategory(paging: PagingData):Response<ProductCategoryResponse>
//    suspend fun updateProductCategory(productCategoryId:String,productCategoryName:String):Response<ProductCategoryResponse>
    suspend fun deleteProductCategory(productCategoryId: String):Response<ProductCategoryResponse>
}