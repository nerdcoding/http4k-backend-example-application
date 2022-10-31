package org.nerdcoding.example.http4k

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.RequestContexts
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.RequestContextKey
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.kodein.di.DI
import org.kodein.di.instance
import org.nerdcoding.example.http4k.handler.login.AuthenticationRequest
import org.nerdcoding.example.http4k.handler.login.AuthenticationResponse
import org.nerdcoding.example.http4k.handler.login.LoginHandler
import org.nerdcoding.example.http4k.handler.ping.PingHandler
import org.nerdcoding.example.http4k.handler.ping.PingResponse
import org.nerdcoding.example.http4k.model.User
import org.nerdcoding.example.http4k.model.UserRole
import org.nerdcoding.example.http4k.service.auth.JsonWebTokenService
import org.nerdcoding.example.http4k.service.auth.UserService
import org.nerdcoding.example.http4k.utils.filter.auth.AuthFilter
import org.nerdcoding.example.http4k.utils.filter.exception.ExceptionFilter
import org.nerdcoding.example.http4k.utils.filter.exception.HttpStatusCodeException


class Router(di: DI) {
    private val pingHandler: PingHandler by di.instance()
    private val loginHandler: LoginHandler by di.instance()
    private val jsonWebTokenService: JsonWebTokenService by di.instance()
    private val userService: UserService by di.instance()

    operator fun invoke(): RoutingHttpHandler {
        val requestContexts = RequestContexts()
        val requestContextsKey = RequestContextKey.required<User>(requestContexts)

        val authFilter = AuthFilter(jsonWebTokenService, userService)

        return ExceptionFilter() // Filter catches all kind of exceptions and creates a problem response.
            // Filters errors during unmarshalling of inbound requests.
            .then(ServerFilters.CatchLensFailure { error ->
                throw HttpStatusCodeException(
                    Status.BAD_REQUEST,
                    "Invalid client request",
                    if (error.cause != null) error.cause?.message ?: "" else ""
                )
            })
            // Initialize Request Context
            .then(ServerFilters.InitialiseRequestContext(requestContexts))
            //.then(DebuggingFilters.PrintRequest())
            //.then(DebuggingFilters.PrintResponse())
            .then(routes(
                "/ping/anonymous" bind Method.GET to {
                    val response = pingHandler.pingAnonymous()

                    Body.auto<PingResponse>().toLens()(
                        response,
                        Response(Status.OK)
                    )
                },
                "/ping/user" bind Method.GET to authFilter.authenticationRequired(requestContextsKey)
                    .then(authFilter.authorizationRequired(
                        requestContextsKey,
                        UserRole.USER,
                        UserRole.ADMIN
                    ))
                    .then {
                        val response = pingHandler.pingUser()

                        Body.auto<PingResponse>().toLens()(
                            response,
                            Response(Status.OK)
                        )
                    },
                "/ping/admin" bind Method.GET to authFilter.authenticationRequired(requestContextsKey)
                    .then(authFilter.authorizationRequired(
                        requestContextsKey,
                        UserRole.ADMIN
                    ))
                    .then {
                        val response = pingHandler.pingAdmin()

                        Body.auto<PingResponse>().toLens()(
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