package example.com.di


import example.com.dao.products.product.ProductDao
import example.com.dao.products.product.ProductDaoImpl
import example.com.dao.users.UserDao
import example.com.dao.users.UserDaoImpl
import example.com.repository.auth.AuthRepository
import example.com.repository.auth.AuthRepositoryImpl
import example.com.repository.product.ProductRepository
import example.com.repository.product.ProductRepositoryImpl
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl(get()) }
    single<ProductDao> { ProductDaoImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }


    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("ecom_db")
    }
}
