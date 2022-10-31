package org.nerdcoding.example.http4k

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.kodein.di.DI
import org.kodein.di.instance
import org.nerdcoding.example.http4k.handler.Ping
import org.nerdcoding.example.http4k.handler.PingHandler
import org.nerdcoding.example.http4k.handler.login.AuthenticationRequest
import org.nerdcoding.example.http4k.handler.login.AuthenticationResponse
import org.nerdcoding.example.http4k.handler.login.LoginHandler
import org.nerdcoding.example.http4k.utils.filter.exception.ExceptionFilter
import org.nerdcoding.example.http4k.utils.filter.exception.HttpStatusCodeException


class Router(di: DI) {

    private val pingHandler: PingHandler by di.instance()
    private val loginHandler: LoginHandler by di.instance()

    operator fun invoke(): RoutingHttpHandler {

        return ExceptionFilter() // Filter catches all kind of exceptions and creates a problem response/
            // Filters errors during unmarshalling of inbound requests.
            .then(ServerFilters.CatchLensFailure { error ->
                throw HttpStatusCodeException(
                    Status.BAD_REQUEST,
                    "Invalid client request",
                    if (error.cause != null) error.cause?.message ?: "" else ""
                )
            })
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
                "/login" bind Method.POST to { request: Request ->
                    val authenticationRequest = Body.auto<AuthenticationRequest>().toLens()(request)
                    val response = loginHandler(authenticationRequest)

                    Body.auto<AuthenticationResponse>().toLens()(
                        response,
                        Response(Status.OK)
                    )
                },
            ))
    }

}