package example.com.repository.brand

import example.com.dao.products.brand.BrandEntity
import example.com.model.BrandResponse
import example.com.model.PagingData
import example.com.utils.Response

interface BrandRepository {
    suspend fun addBrand(brandName:String):Response<BrandResponse>
    suspend fun updateBrand(brandId:String,brandName: String):Response<BrandResponse>
    suspend fun getBrand(pagingData: PagingData):Response<BrandResponse>
    suspend fun deleteBand(brandId:String):Response<BrandResponse>
}