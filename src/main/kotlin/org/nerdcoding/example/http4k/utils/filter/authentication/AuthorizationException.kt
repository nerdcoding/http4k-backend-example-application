package org.nerdcoding.example.http4k.utils.filter.authentication

import org.http4k.core.Status
import org.nerdcoding.example.http4k.utils.filter.exception.HttpStatusCodeException


class AuthorizationException: HttpStatusCodeException(Status.UNAUTHORIZED, "Unauthorized")