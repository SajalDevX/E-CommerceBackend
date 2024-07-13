package example.com.dao.products.product_category

import example.com.utils.PagingData

interface ProductCategoryDao{
    suspend fun createProductCategory(addProductCategory: String):Boolean
    suspend fun getProductCategory(paging: PagingData):List<ProductCategoryEntity>
    suspend fun updateProductCategory(id: String,name:String):Boolean
    suspend fun deleteProductCategory(deleteProductCategory: String):Boolean
}