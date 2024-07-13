package example.com.repository.shop

import example.com.dao.shop.entity.ShopCategoryEntity
import example.com.model.ShopCategoryResponse
import example.com.model.ShopResponse
import example.com.utils.Response

interface ShopRepository {

    suspend fun createShopCategory(shopCategoryName:String):Response<ShopCategoryResponse>
    suspend fun getShopCategories(limit:Int,offset:Int):Response<ShopCategoryResponse>
    suspend fun updateShopCategory(shopCategoryId:String,shopCategoryName: String):Response<ShopCategoryResponse>
    suspend fun deleteShopCategory(shopCategoryId: String):Response<ShopCategoryResponse>
    suspend fun createShop(userId:String,shopCategoryId: String,shopName:String):Response<ShopResponse>
}