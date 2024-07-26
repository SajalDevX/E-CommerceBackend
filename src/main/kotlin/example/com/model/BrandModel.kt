package example.com.model

import example.com.dao.products.brand.BrandEntity
import kotlinx.serialization.Serializable

@Serializable
data class BrandResponse(
    val success:Boolean,
    val message:String,
    val brands:List<BrandEntity?> = emptyList()
)
@Serializable
data class BrandTextParams(
    val brandName:String
)