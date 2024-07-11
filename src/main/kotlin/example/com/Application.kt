package example.com

import com.papsign.ktor.openapigen.OpenAPIGen
import example.com.di.configureDI
import example.com.plugins.configureSecurity
import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(OpenAPIGen)
    configureDI()
    configureSerialization()
    configureSecurity()
    configureRouting()

}