package example.com.repository.shop

import example.com.dao.shop.ShopDao
import example.com.model.ShopCategoryResponse
import example.com.model.ShopResponse
import example.com.utils.Response
import io.ktor.http.*

class ShopRepositoryImpl(
    private val dao: ShopDao
) : ShopRepository {
    override suspend fun createShopCategory(shopCategoryName: String): Response<ShopCategoryResponse> {
        val result = dao.createShopCategory(shopCategoryName)
        return if (result) {
            Response.Success(
                ShopCategoryResponse(
                    success = true,
                    message = "Shop category created successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShopCategoryResponse(
                    success = false,
                    message = "Shop category creation failed"

                )
            )
        }
    }

    override suspend fun getShopCategories(limit: Int, offset: Int): Response<ShopCategoryResponse> {
        val result = dao.getShopCategories(limit, offset)
        return if (result.isNotEmpty()) {
            Response.Success(
                ShopCategoryResponse(
                    success = true,
                    shopCategories = result,
                    message = "Shop category fetched successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShopCategoryResponse(
                    success = false,
                    message = "Shop category could not be fetched"

                )
            )
        }
    }

    override suspend fun updateShopCategory(
        shopCategoryId: String,
        shopCategoryName: String
    ): Response<ShopCategoryResponse> {
        val result = dao.updateShopCategory(shopCategoryId, shopCategoryName)
        return if (result) {
            Response.Success(
                ShopCategoryResponse(
                    success = true,
                    message = "Shop category updated successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShopCategoryResponse(
                    success = false,
                    message = "Shop category could not be updated"

                )
            )
        }
    }

    override suspend fun deleteShopCategory(shopCategoryId: String): Response<ShopCategoryResponse> {
        val result = dao.deleteShopCategory(shopCategoryId)
        return if (result) {
            Response.Success(
                ShopCategoryResponse(
                    success = true,
                    message = "Shop category deleted successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShopCategoryResponse(
                    success = false,
                    message = "Shop category could not be deleted"

                )
            )
        }
    }

    override suspend fun createShop(userId: String, shopCategoryId: String, shopName: String): Response<ShopResponse> {
        val result = dao.createShop(userId, shopCategoryId, shopName)
        return if (result != null) {
            Response.Success(
                ShopResponse(
                    success = true,
                    shop = result,
                    message = "Shop added successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                ShopResponse(
                    success = false,
                    message = "Shop  could not be added"

                )
            )
        }
    }
}