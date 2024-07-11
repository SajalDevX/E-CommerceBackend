package example.com.plugins

import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.route.apiRouting
import example.com.repository.auth.AuthRepository
import example.com.repository.product.ProductRepository
import example.com.routing.authRouting
import example.com.routing.productRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(Routing) {
        apiRouting {
            val authRepository by inject<AuthRepository>()
            val productRepository by inject<ProductRepository>()
            authRouting(authRepository)
            productRoute(productRepository)
            static {
                resources("static")
            }
        }
    }
}
