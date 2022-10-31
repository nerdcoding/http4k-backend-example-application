package org.nerdcoding.example.http4k

import org.http4k.cloudnative.env.Environment
import org.http4k.server.ApacheServer
import org.http4k.server.asServer
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.nerdcoding.example.http4k.handler.PingHandler
import org.nerdcoding.example.http4k.handler.login.LoginHandler
import org.nerdcoding.example.http4k.service.auth.JsonWebTokenService
import org.nerdcoding.example.http4k.service.auth.UserService
import org.nerdcoding.example.http4k.utils.config.ApplicationConfig
import org.nerdcoding.example.http4k.utils.config.createApplicationConfig
import org.slf4j.LoggerFactory
import java.io.File


fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("main")

    val di = createDependencyInjectionBindings(args)
    val applicationConfig: ApplicationConfig by di.instance()

    val app = Router(di)()
    log.info("Starting server...")
    val server = app.asServer(
        ApacheServer(applicationConfig.serverConfig.port)
    ).start()
    log.info("Server started on port ${server.port()}")
}

private fun createDependencyInjectionBindings(args: Array<String>) =
    DI {
        bindSingleton { createApplicationConfig(getEnvironment(args)) }

        // Handlers
        bindSingleton { PingHandler() }
        bindSingleton { LoginHandler(instance(), instance(), instance()) }

        // Services
        bindSingleton { JsonWebTokenService(instance()) }
        bindSingleton { UserService() }
    }

private fun getEnvironment(args: Array<String>) =
    if (args.isNotEmpty() && args[0] == "dev")
        Environment.from(File("config/.env.dev"))
    else
        Environment.ENV