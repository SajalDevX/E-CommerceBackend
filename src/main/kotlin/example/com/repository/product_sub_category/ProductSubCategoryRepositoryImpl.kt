package example.com.repository.product_sub_category

import example.com.dao.products.product_subcategory.ProductSubCategoryDao
import example.com.model.ProductCategoryResponse
import example.com.model.ProductSubCategoryResponse
import example.com.utils.Response
import io.ktor.http.*

class ProductSubCategoryRepositoryImpl(
    private val dao: ProductSubCategoryDao
) : ProductSubCategoryRepository {
    override suspend fun createProductSubcategory(
        categoryId: String,
        subCategoryName: String,
        imageUrl:String
    ): Response<ProductSubCategoryResponse> {
        val result = dao.createProductSubcategory(categoryId, subCategoryName,imageUrl)
        return if (result) {
            Response.Success(
                ProductSubCategoryResponse(
                    success = true,
                    message = "Product sub-category created successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ProductSubCategoryResponse(
                    success = false,
                    message = "Product sub-category creation failed"
                )
            )
        }
    }

    override suspend fun getProductSubCategory(
        categoryId: String,
        offset: Int,
        limit: Int
    ): Response<ProductSubCategoryResponse> {
        val result = dao.getProductSubCategory(categoryId, offset, limit)
        return if (result.isNotEmpty()) {
            Response.Success(
                ProductSubCategoryResponse(
                    success = true,
                    subCategories = result,
                    message = "Product sub-category parse successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ProductSubCategoryResponse(
                    success = false,
                    message = "Product sub-category parse failed"
                )
            )
        }
    }

    override suspend fun updateProductSubcategory(
        subCategoryId: String,
        subCategoryName: String
    ): Response<ProductSubCategoryResponse> {
        val result = dao.updateProductSubcategory(subCategoryId, subCategoryName)
        return if (result) {
            Response.Success(
                ProductSubCategoryResponse(
                    success = true,
                    message = "Product sub-category updated successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ProductSubCategoryResponse(
                    success = false,
                    message = "Product sub-category update failed"
                )
            )
        }
    }

    override suspend fun deleteProductSubCategory(subCategoryId: String): Response<ProductSubCategoryResponse> {
        val result = dao.deleteProductSubCategory(subCategoryId)
        return if (result) {
            Response.Success(
                ProductSubCategoryResponse(
                    success = true,
                    message = "Product sub-category delete successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ProductSubCategoryResponse(
                    success = false,
                    message = "Product sub-category delete failed"
                )
            )
        }
    }
}