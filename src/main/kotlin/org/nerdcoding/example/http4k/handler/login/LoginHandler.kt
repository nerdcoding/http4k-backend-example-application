package org.nerdcoding.example.http4k.handler.login

import org.http4k.core.Status
import org.nerdcoding.example.http4k.service.auth.JsonWebTokenService
import org.nerdcoding.example.http4k.service.auth.UserService
import org.nerdcoding.example.http4k.utils.config.ApplicationConfig
import org.nerdcoding.example.http4k.utils.filter.exception.HttpStatusCodeException


class LoginHandler(
    private val applicationConfig: ApplicationConfig,
    private val jsonWebTokenService: JsonWebTokenService,
    private val userService: UserService
) {


    operator fun invoke(authenticationRequest: AuthenticationRequest): AuthenticationResponse {

        val user = userService.findUserByEmail(authenticationRequest.email) ?: throw HttpStatusCodeException(
            Status.UNAUTHORIZED,
            "Invalid credentials"
        )
        if (authenticationRequest.password != user.credentials)
            throw HttpStatusCodeException(Status.UNAUTHORIZED, "Invalid credentials")


        return AuthenticationResponse(
            user.email,
            user.roles,
            jsonWebTokenService.generate(user),
            "Bearer",
            applicationConfig.jwtConfig.expirationMillis
        )
    }



}
