package org.nerdcoding.example.http4k.handler.ping

import kotlinx.serialization.Serializable


@Serializable
data class PingResponse(val message: String)