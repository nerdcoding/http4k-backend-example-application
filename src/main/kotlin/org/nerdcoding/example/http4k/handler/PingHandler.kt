package org.nerdcoding.example.http4k.handler

import kotlinx.serialization.Serializable


class PingHandler {

    operator fun invoke(): Ping {
        return Ping("ping was called")
    }

}

@Serializable
data class Ping(val message: String)