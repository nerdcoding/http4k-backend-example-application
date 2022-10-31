package org.nerdcoding.example.http4k.handler.ping


class PingHandler {

    fun pingAnonymous() =
        PingResponse("ping was called with anonymous user")

    fun pingUser() =
        PingResponse("ping was called with authenticated user")

    fun pingAdmin() =
        PingResponse("ping was called with admin user")

}

