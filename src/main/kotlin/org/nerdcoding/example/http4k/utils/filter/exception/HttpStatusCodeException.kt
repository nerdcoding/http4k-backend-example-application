package org.nerdcoding.example.http4k.utils.filter.exception

import org.http4k.core.Status


open class HttpStatusCodeException(
    val status: Status,
    val title: String = status.description,
    val detail: String? = null
): RuntimeException(title)