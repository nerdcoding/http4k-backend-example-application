package org.nerdcoding.example.http4k.service.auth

import org.nerdcoding.example.http4k.model.User
import org.nerdcoding.example.http4k.model.UserRole


class UserService {

    // map of fixed users until database is connected
    private val users = mapOf(
        "heide@gmail.com" to User("heide@example.com", "heide123", listOf(UserRole.ADMIN, UserRole.USER)),
        "fritz@yahoo.com" to User("fritz@example.com", "fritz123", listOf(UserRole.USER)),
        "paula@yahoo.com" to User("paula@example.com", "paula123"),
    )

    fun findUserByEmail(email: String): User? =
        users[email]

}