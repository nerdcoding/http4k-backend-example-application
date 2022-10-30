package org.nerdcoding.example.http4k

import org.http4k.cloudnative.env.Environment
import org.http4k.server.ApacheServer
import org.http4k.server.asServer
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.nerdcoding.example.http4k.handler.PingHandler
import org.nerdcoding.example.http4k.utils.config.ApplicationConfig
import org.nerdcoding.example.http4k.utils.config.createApplicationConfig
import org.slf4j.LoggerFactory
import java.io.File


fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("main")

    val di = DI {
        bindSingleton { createApplicationConfig(getEnvironment(args)) }
        bindSingleton { PingHandler() }
    }
    val applicationConfig: ApplicationConfig by di.instance()

    val app = Router(di)()
    log.info("Starting server...")
    val server = app.asServer(
        ApacheServer(applicationConfig.serverConfig.port)
    ).start()
    log.info("Server started on port ${server.port()}")
}

private fun getEnvironment(args: Array<String>) =
    if (args.isNotEmpty() && args[0] == "dev")
        Environment.from(File("config/.env.dev"))
    else
        Environment.ENV