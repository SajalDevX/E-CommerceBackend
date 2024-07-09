package example.com.repository.product

import example.com.dao.products.product.ProductDao
import example.com.model.*
import example.com.utils.Response
import io.ktor.http.*

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun addProduct(userId: String, params: AddProduct): Response<ProductResponse> {
        val product = productDao.addProduct(userId, params)
        return if (!product) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Could not add your product"
                )
            )
        } else {
            Response.Success(
                data = ProductResponse(
                    success = true,
                    message = "Product added successfully"
                )
            )
        }
    }

    override suspend fun updateProduct(
        userId: String,
        productId: String,
        updateProduct: UpdateProduct
    ): Response<ProductResponse> {
        val update = productDao.updateProduct(userId, productId, updateProduct)
        return if (!update) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Could not update your product"
                )
            )
        } else {
            Response.Success(
                data = ProductResponse(
                    success = true,
                    message = "Product updated successfully"
                )
            )
        }
    }

    override suspend fun getProducts(productQueryParams: ProductWithFilter): Response<ProductResponse> {
        val products = productDao.getProduct(productQueryParams)
        return if (products.isEmpty()) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Could not fetch products from database"
                )
            )
        } else {
            Response.Success(
                data = ProductResponse(
                    success = true,
                    allProducts = products,
                    message = "Product fetched from database"
                )
            )
        }
    }

    override suspend fun getProductById(userId: String, productId: String): Response<ProductResponse> {
        val product = productDao.getProductById(userId, productId)
        return if (product == null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Could not fetch requested product from database"
                )
            )
        } else {

            Response.Success(
                data = ProductResponse(
                    success = true,
                    product = product,
                    message = "Product fetched from database"
                )
            )
        }
    }

    override suspend fun deleteProduct(userId: String, productId: String): Response<ProductResponse> {
        val request = productDao.deleteProduct(userId, productId)
        return if (request) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Could not delete requested product from database"
                )
            )
        } else {

            Response.Success(
                data = ProductResponse(
                    success = true,
                    message = "Product deleted from database"
                )
            )
        }
    }


    override suspend fun uploadProductImages(
        userId: String,
        productId: String,
        images: String
    ): Response<ProductResponse> {
        val request = productDao.uploadProductImages(userId, productId, images)
        return if (request) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Could not upload images to  database"
                )
            )
        } else {

            Response.Success(
                data = ProductResponse(
                    success = true,
                    message = "Images uploaded to database"
                )
            )
        }
    }

    override suspend fun getProductDetail(productId: String): Response<ProductResponse> {
        val request = productDao.productDetail(productId)
        return if (request==null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Could not get product details"
                )
            )
        } else {
            Response.Success(
                data = ProductResponse(
                    success = true,
                    product = request,
                    message = "Product details fetched successfully"
                )
            )
        }
    }
}