package org.nerdcoding.example.http4k.handler.login

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.http4k.core.Status
import org.nerdcoding.example.http4k.model.User
import org.nerdcoding.example.http4k.utils.config.ApplicationConfig
import org.nerdcoding.example.http4k.utils.filter.exception.HttpStatusCodeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.spec.SecretKeySpec


class LoginHandler(private val applicationConfig: ApplicationConfig) {

    // map of fixed users until database is connected
    private val users = mapOf(
        "heide@gmail.com" to User("heide@gmail.com", "heide123"),
        "fritz@yahoo.com" to User("fritz@yahoo.com", "fritz123"),
    )

    operator fun invoke(authenticationRequest: AuthenticationRequest): AuthenticationResponse {

        val user = users[authenticationRequest.email] ?: throw HttpStatusCodeException(
            Status.UNAUTHORIZED,
            "Invalid credentials"
        )
        if (authenticationRequest.password != user.credentials)
            throw HttpStatusCodeException(Status.UNAUTHORIZED, "Invalid credentials")


        return AuthenticationResponse(
            user.email,
            user.roles,
            createJsonWebToken(user),
            "Bearer",
            applicationConfig.jwtConfig.expirationMillis
        )
    }

    private fun createJsonWebToken(user: User) =
        Jwts.builder()
            .setIssuer(applicationConfig.jwtConfig.issuer)
            .setSubject(user.email)
            .setIssuedAt(Date())
            .setExpiration(
                Date.from(
                    LocalDateTime.now().plus(applicationConfig.jwtConfig.expirationMillis, ChronoUnit.MILLIS)
                        .atZone(ZoneId.systemDefault()).toInstant()
                )
            )
            .signWith(
                SecretKeySpec(
                    applicationConfig.jwtConfig.secret.toByteArray(),
                    SignatureAlgorithm.HS512.jcaName
                )
            )
            .compact()

}
