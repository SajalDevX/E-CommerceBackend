package example.com.repository.product_category

import example.com.dao.products.product_category.ProductCategoryDao
import example.com.model.PagingData
import example.com.model.ProductCategoryResponse
import example.com.utils.Response

class ProductCategoryRepositoryImpl(
    private val dao:ProductCategoryDao
) : ProductCategoryRepository {
    override suspend fun createProductCategory(productCategoryName: String): Response<ProductCategoryResponse> {
        val result = dao.createProductCategory(productCategoryName)
        return if(result){
            Response.Success(
                ProductCategoryResponse(
                    success = true,
                    message = "Product category created successfully"
                )
            )
        }else{
            Response.Success(
                ProductCategoryResponse(
                    success = false,
                    message = "Product category creation failed"
                )
            )
        }
    }

    override suspend fun getProductCategory(paging: PagingData): Response<ProductCategoryResponse> {
        val result = dao.getProductCategory(paging)
        return if(result.isNotEmpty()){
            Response.Success(
                ProductCategoryResponse(
                    success = true,
                    categories = result,
                    message = "Product category parse successfully"
                )
            )
        }else{
            Response.Success(
                ProductCategoryResponse(
                    success = false,
                    message = "Product category parse failed"
                )
            )
        }
    }

    override suspend fun updateProductCategory(productCategoryId:String,productCategoryName:String): Response<ProductCategoryResponse> {
        val result = dao.updateProductCategory(productCategoryId,productCategoryName)
        return if(result){
            Response.Success(
                ProductCategoryResponse(
                    success = true,
                    message = "Product category updated successfully"
                )
            )
        }else{
            Response.Success(
                ProductCategoryResponse(
                    success = false,
                    message = "Product category update failed"
                )
            )
        }
    }

    override suspend fun deleteProductCategory(productCategoryId: String): Response<ProductCategoryResponse> {
        val result = dao.deleteProductCategory(productCategoryId)
        return if(result){
            Response.Success(
                ProductCategoryResponse(
                    success = true,
                    message = "Product category delete successfully"
                )
            )
        }else{
            Response.Success(
                ProductCategoryResponse(
                    success = false,
                    message = "Product category delete failed"
                )
            )
        }
    }
}