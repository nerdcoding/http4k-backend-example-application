package org.nerdcoding.example.http4k.handler.login

import kotlinx.serialization.Serializable


@Serializable
data class AuthenticationRequest(val email: String, val password: String)