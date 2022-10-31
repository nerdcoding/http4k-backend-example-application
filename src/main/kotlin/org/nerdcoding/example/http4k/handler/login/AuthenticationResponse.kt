package org.nerdcoding.example.http4k.handler.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val email: String,
    @SerialName("access_token")
    val accessToken: String,
    //@SerialName("refresh_token")
    //val refreshToken: String?,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val expiresIn: Long
)