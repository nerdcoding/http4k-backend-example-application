package org.nerdcoding.example.http4k.utils.filter.auth

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import org.nerdcoding.example.http4k.model.User
import org.nerdcoding.example.http4k.model.UserRole
import org.nerdcoding.example.http4k.service.auth.JsonWebTokenService
import org.nerdcoding.example.http4k.service.auth.UserService


class AuthFilter(
    private val jsonWebTokenService: JsonWebTokenService,
    private val userService: UserService
) {

    fun authenticationRequired(key: RequestContextLens<User>) = Filter { next -> { request ->
        val token = findBearerToken(request)
            ?: throw AuthenticationException()
        val tokenClaims = jsonWebTokenService.parse(token)
        val authenticatedUser = userService.findUserByEmail(tokenClaims.subject)
            ?: throw AuthenticationException()

        next(request.with(key of authenticatedUser))
    }}

    fun authorizationRequired(key: RequestContextLens<User>, vararg allowedRoles: UserRole) = Filter { next -> { request ->
        val authenticatedUser = key(request)

        if (authenticatedUser.roles.none { userRole ->
            allowedRoles.contains(userRole)
        }) throw AuthorizationException()

        next(request)
    }}

    private fun findBearerToken(request: Request): String? =
        request.header("Authorization")
            ?.trim()
            ?.takeIf { it.startsWith("Bearer") }
            ?.substringAfter("Bearer")
            ?.trim()
}