package org.nerdcoding.example.http4k

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.kodein.di.DI
import org.kodein.di.instance
import org.nerdcoding.example.http4k.handler.Ping
import org.nerdcoding.example.http4k.handler.PingHandler
import org.nerdcoding.example.http4k.utils.filter.ExceptionFilter


class Router(di: DI) {


    private val pingHandler: PingHandler by di.instance()

    operator fun invoke(): RoutingHttpHandler {

        return ExceptionFilter()
            .then(DebuggingFilters.PrintRequest())
            .then(DebuggingFilters.PrintResponse())
            .then(routes(
                "/ping" bind Method.GET to {
                    val response = pingHandler()

                    Body.auto<Ping>().toLens()(
                        response,
                        Response(Status.OK)
                    )
                },
            ))
    }

}