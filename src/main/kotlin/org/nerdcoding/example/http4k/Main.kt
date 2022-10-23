package org.nerdcoding.example.http4k

import org.http4k.server.ApacheServer
import org.http4k.server.asServer
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.nerdcoding.example.http4k.handler.PingHandler
import org.slf4j.LoggerFactory


val di = DI {
    bindSingleton { PingHandler() }
}

fun main() {
    val log = LoggerFactory.getLogger("main")

    val app = Router(di)()

    log.info("Starting server...")
    val server = app.asServer(ApacheServer(9000)).start()
    log.info("Server started on port ${server.port()}")
}
