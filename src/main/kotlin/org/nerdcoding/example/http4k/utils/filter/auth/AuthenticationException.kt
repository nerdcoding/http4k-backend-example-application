package org.nerdcoding.example.http4k.utils.filter.auth

import org.http4k.core.Status
import org.nerdcoding.example.http4k.utils.filter.exception.HttpStatusCodeException


class AuthenticationException: HttpStatusCodeException(Status.UNAUTHORIZED, "Unauthorized")