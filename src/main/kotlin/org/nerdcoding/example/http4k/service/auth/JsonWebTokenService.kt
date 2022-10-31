package org.nerdcoding.example.http4k.service.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.nerdcoding.example.http4k.model.User
import org.nerdcoding.example.http4k.utils.config.ApplicationConfig
import org.nerdcoding.example.http4k.utils.filter.authentication.AuthorizationException
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.spec.SecretKeySpec


class JsonWebTokenService(private val applicationConfig: ApplicationConfig) {

    private val logger = LoggerFactory.getLogger(JsonWebTokenService::class.java)
    private val jwtSignatureAlgorithm = SignatureAlgorithm.HS512.jcaName

    fun generate(user: User): String =
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
                    jwtSignatureAlgorithm
                )
            )
            .setHeaderParam("typ", "JWT")
            .compact()


    fun parse(token: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(
                    SecretKeySpec(
                        applicationConfig.jwtConfig.secret.toByteArray(),
                        jwtSignatureAlgorithm
                    )
                )
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: JwtException) {
            logger.error("Invalid JWT" , e)
            throw AuthorizationException()
        }
    }
}