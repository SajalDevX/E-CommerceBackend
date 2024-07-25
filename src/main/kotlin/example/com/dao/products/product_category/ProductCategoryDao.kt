package example.com.dao.products.product_category

import example.com.dao.products.product_subcategory.ProductSubCategoryEntity
import example.com.utils.PagingData

interface ProductCategoryDao{
    suspend fun createProductCategory(addProductCategory: String,imageUrl:String):Boolean
    suspend fun getProductCategory(paging: PagingData):List<ProductCategoryEntity>
    suspend fun updateProductCategory(id: String,subCategoryEntity: ProductSubCategoryEntity):Boolean
    suspend fun deleteProductCategory(deleteProductCategory: String):Boolean
}