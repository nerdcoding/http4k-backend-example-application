package org.nerdcoding.example.http4k.utils.exception

import org.http4k.core.Status


class HttpStatusCodeException(
    val status: Status,
    val title: String = status.description,
    val detail: String? = null
): RuntimeException(title)