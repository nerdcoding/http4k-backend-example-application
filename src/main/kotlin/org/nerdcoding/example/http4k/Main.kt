package org.nerdcoding.example.http4k

import org.nerdcoding.example.http4k.formats.kotlinXMessage
import org.nerdcoding.example.http4k.formats.kotlinXMessageLens
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

val app: HttpHandler = routes(
    "/ping" bind GET to {
        Response(OK).body("pong")
    },

    "/formats/json/kotlinx" bind GET to {
        Response(OK).with(kotlinXMessageLens of kotlinXMessage)
    },

    "/testing/kotest" bind GET to {request ->
        Response(OK).body("Echo '${request.bodyString()}'")
    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(ApacheServer(9000)).start()

    println("Server started on " + server.port())
}
