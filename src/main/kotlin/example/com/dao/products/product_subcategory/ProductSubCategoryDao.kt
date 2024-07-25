package example.com.dao.products.product_subcategory

import io.ktor.http.*

interface ProductSubCategoryDao {
    suspend fun createProductSubcategory(categoryId: String, subCategoryName: String,imageUrl: String): Boolean
    suspend fun getProductSubCategory(categoryId: String, offset: Int, limit: Int): List<ProductSubCategoryEntity>
    suspend fun updateProductSubcategory(subCategoryId: String, subCategoryName: String): Boolean
    suspend fun deleteProductSubCategory(subCategoryId: String): Boolean
}