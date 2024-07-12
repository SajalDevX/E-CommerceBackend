package example.com.dao.products.brand

import example.com.model.PagingData

interface BrandDao {
    suspend fun createBrand(brandName:String):Boolean
    suspend fun getBrand(paging:PagingData):List<BrandEntity>
    suspend fun updateBrand(brandId:String,brandName:String):Boolean
    suspend fun deleteBrand(brandId:String):Boolean
}