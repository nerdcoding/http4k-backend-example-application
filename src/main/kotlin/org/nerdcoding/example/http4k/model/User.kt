package org.nerdcoding.example.http4k.model

import java.util.Collections


data class User(
    val email: String,
    val credentials: String,
    val roles: List<String> = Collections.emptyList()
)
