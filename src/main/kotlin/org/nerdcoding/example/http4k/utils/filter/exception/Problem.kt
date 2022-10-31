package org.nerdcoding.example.http4k.utils.filter.exception

import kotlinx.serialization.Serializable

@Serializable
data class Problem(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String?,
    val instance: String,
)
