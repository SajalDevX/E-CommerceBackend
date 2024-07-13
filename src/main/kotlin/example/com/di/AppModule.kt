package example.com.di


import example.com.dao.products.brand.BrandDao
import example.com.dao.products.brand.BrandDaoImpl
import example.com.dao.products.product.ProductDao
import example.com.dao.products.product.ProductDaoImpl
import example.com.dao.products.product_category.ProductCategoryDao
import example.com.dao.products.product_category.ProductCategoryDaoImpl
import example.com.dao.products.product_subcategory.ProductSubCategoryDao
import example.com.dao.products.product_subcategory.ProductSubCategoryDaoImpl
import example.com.dao.shop.ShopDao
import example.com.dao.shop.ShopDaoImpl
import example.com.dao.users.UserDao
import example.com.dao.users.UserDaoImpl
import example.com.repository.auth.AuthRepository
import example.com.repository.auth.AuthRepositoryImpl
import example.com.repository.brand.BrandRepository
import example.com.repository.brand.BrandRepositoryImpl
import example.com.repository.product.ProductRepository
import example.com.repository.product.ProductRepositoryImpl
import example.com.repository.product_category.ProductCategoryRepository
import example.com.repository.product_category.ProductCategoryRepositoryImpl
import example.com.repository.product_sub_category.ProductSubCategoryRepository
import example.com.repository.product_sub_category.ProductSubCategoryRepositoryImpl
import example.com.repository.shop.ShopRepository
import example.com.repository.shop.ShopRepositoryImpl
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl(get()) }
    single<ProductDao> { ProductDaoImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<ProductCategoryDao> { ProductCategoryDaoImpl(get()) }
    single<ProductCategoryRepository> { ProductCategoryRepositoryImpl(get()) }
    single<ProductSubCategoryDao> { ProductSubCategoryDaoImpl(get()) }
    single<ProductSubCategoryRepository> { ProductSubCategoryRepositoryImpl(get()) }
    single<BrandDao> { BrandDaoImpl(get()) }
    single<BrandRepository> { BrandRepositoryImpl(get()) }
    single<ShopDao> { ShopDaoImpl(get()) }
    single<ShopRepository> { ShopRepositoryImpl(get()) }


    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("ecom_db")
    }
}
