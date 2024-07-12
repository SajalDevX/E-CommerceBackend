package example.com.repository.brand

import example.com.dao.products.brand.BrandDao
import example.com.model.BrandResponse
import example.com.model.PagingData
import example.com.utils.Response
import io.ktor.http.*

class BrandRepositoryImpl(
    private val dao:BrandDao
) : BrandRepository {
    override suspend fun addBrand(brandName: String): Response<BrandResponse> {
        val result = dao.createBrand(brandName)
        return if(result){
            Response.Success(
                BrandResponse(
                    success = true,
                    message = "Brand added successfully"
                )
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                BrandResponse(
                    success = false,
                    message = "Brand could not be added"
                )
            )
        }
    }

    override suspend fun updateBrand(brandId: String, brandName: String): Response<BrandResponse> {
        val result = dao.updateBrand(brandId, brandName)
       return if(result){
            Response.Success(
                BrandResponse(
                    success = true,
                    message = "Brand updated successfully"
                )
            )
        }else{
           Response.Error(
               code = HttpStatusCode.InternalServerError,
               BrandResponse(
                   success = false,
                   message = "Brand update failed"
               )
           )
       }
    }

    override suspend fun getBrand(pagingData: PagingData): Response<BrandResponse> {
        val result = dao.getBrand(pagingData)
        return if(result.isNotEmpty()){
            Response.Success(
                BrandResponse(
                    success = true,
                    brands = result,
                    message = "Brands data fetched successfully"
                )
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                BrandResponse(
                    success = false,
                    message = "Brands could not be fetched"
                )
            )
        }
    }

    override suspend fun deleteBand(brandId: String): Response<BrandResponse> {
        val result = dao.deleteBrand(brandId)
        return if (result){
            Response.Success(
                BrandResponse(
                    success = true,
                    message = "Brand Successfully deleted"
                )
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                BrandResponse(
                    success = false,
                    message = "Brand could not deleted"
                )
            )
        }
    }
}