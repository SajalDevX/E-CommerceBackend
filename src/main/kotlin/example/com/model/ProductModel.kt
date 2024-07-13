package example.com.model

import example.com.dao.products.product.entity.ProductEntity
import kotlinx.serialization.Serializable

@Serializable
data class AddProduct(
    val categoryId: String,
    val subCategoryId: String?,
    val brandId: String?,
    val productName: String,
    val productCode: String?,
    val productQuantity: Int,
    val productDetail: String,
    val price: Double,
    val discountPrice: Double?,
    val videoLink: String?,
    val hotDeal: String?,
    val buyOneGetOne: String?,
    val imageOne: String?,
    val imageTwo: String?,
)

@Serializable
data class UpdateProduct(
    val categoryId: String? = null,
    val subCategoryId: String? = null,
    val brandId: String? = null,
    val productName: String? = null,
    val productCode: String? = null,
    val productQuantity: Int? = null,
    val productDetail: String? = null,
    val price: Double? = null,
    val discountPrice: Double? = null,
    val videoLink: String? = null,
    val hotDeal: String? = null,
    val buyOneGetOne: String? = null,
    val imageOne: String? = null,
    val imageTwo: String? = null
)

@Serializable
data class ProductWithFilter(
    val limit: Int,
    val offset: Int,
    val maxPrice: Double?,
    val minPrice: Double?,
    val categoryId: String?,
    val subCategoryId: String?,
    val brandId: String?,
)

@Serializable
data class ProductResponse(
    val success: Boolean,
    val product: ProductEntity? = null,
    val allProducts: List<ProductEntity> = emptyList(),
    val message: String? = null
)
