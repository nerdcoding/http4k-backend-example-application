package org.nerdcoding.example.http4k.utils.filter.authentication

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import org.nerdcoding.example.http4k.model.User
import org.nerdcoding.example.http4k.service.auth.JsonWebTokenService
import org.nerdcoding.example.http4k.service.auth.UserService


class AuthenticationFilter(
    private val jsonWebTokenService: JsonWebTokenService,
    private val userService: UserService
) {

    fun authRequired(key: RequestContextLens<User>) = Filter { next -> { request ->
        val token = findBearerToken(request)
            ?: throw AuthorizationException()
        val tokenClaims = jsonWebTokenService.parse(token)
        val authenticatedUser = userService.findUserByEmail(tokenClaims.subject)
            ?: throw AuthorizationException()

        next(request.with(key of authenticatedUser))
    }}

    private fun findBearerToken(request: Request): String? =
        request.header("Authorization")
            ?.trim()
            ?.takeIf { it.startsWith("Bearer") }
            ?.substringAfter("Bearer")
            ?.trim()
}