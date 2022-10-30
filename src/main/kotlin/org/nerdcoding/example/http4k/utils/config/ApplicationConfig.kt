package org.nerdcoding.example.http4k.utils.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.string

/**
 * Contains all environment specific configuration values of the application.
 */
data class ApplicationConfig(
    val serverConfig: ServerConfig,
    val jwtConfig: JwtConfig,
)

data class ServerConfig(
    val port: Int
)

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val expirationMillis: Int
)

fun createApplicationConfig(env: Environment) =
    ApplicationConfig(
        ServerConfig(
            EnvironmentKey.int().required("SERVER_PORT")(env)
        ),
        JwtConfig(
            EnvironmentKey.string().required("JWT_SECRET")(env),
            EnvironmentKey.string().required("JWT_ISSUER")(env),
            EnvironmentKey.int().required("JWT_EXPIRATION_MILLIS")(env)
        )
    )
