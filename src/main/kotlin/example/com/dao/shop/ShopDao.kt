package example.com.dao.shop

import example.com.dao.shop.entity.ShopCategoryEntity
import example.com.dao.shop.entity.ShopEntity


interface ShopDao{
    suspend fun createShopCategory(shopCategoryName:String):Boolean
    suspend fun getShopCategories(limit:Int,offset:Int):List<ShopCategoryEntity>
    suspend fun updateShopCategory(shopCategoryId:String,shopCategoryName: String):Boolean
    suspend fun deleteShopCategory(shopCategoryId: String):Boolean
    suspend fun createShop(userId:String,shopCategoryId: String,shopName:String): ShopEntity?
    suspend fun getShop(userId:String):ShopEntity?
}