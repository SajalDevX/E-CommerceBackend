package example.com.repository.product_sub_category

import example.com.model.ProductSubCategoryResponse
import example.com.utils.Response

interface ProductSubCategoryRepository {

    suspend fun createProductSubcategory(categoryId: String, subCategoryName: String,imageUrl:String): Response<ProductSubCategoryResponse>
    suspend fun getProductSubCategory(categoryId: String, offset: Int, limit: Int): Response<ProductSubCategoryResponse>
    suspend fun updateProductSubcategory(subCategoryId: String, subCategoryName: String): Response<ProductSubCategoryResponse>
    suspend fun deleteProductSubCategory(subCategoryId: String): Response<ProductSubCategoryResponse>
}