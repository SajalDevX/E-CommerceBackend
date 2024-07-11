package example.com.model

import example.com.dao.products.product_category.ProductCategoryEntity
import kotlinx.serialization.Serializable

//@Serializable
//data class ProductCategory(
//    val categoryId:String?=null,
//    val categoryName: String?=null
//)
//@Serializable
//data class UpdateProductCategory(
//    val categoryId:String,
//    val categoryName:String
//)
//@Serializable
//data class DeleteProductCategory(
//    val categoryId:String
//)

@Serializable
data class ProductCategoryResponse(
    val success:Boolean,
    val message:String,
    val categories:List<ProductCategoryEntity> = emptyList()
)