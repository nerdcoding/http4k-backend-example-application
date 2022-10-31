package org.nerdcoding.example.http4k.service.auth

import org.nerdcoding.example.http4k.model.User
import org.nerdcoding.example.http4k.model.UserRole


class UserService {

    // map of fixed users until database is connected
    private val users = mapOf(
        "heide@gmail.com" to User("heide@yahoo.com", "heide123", listOf(UserRole.ADMIN, UserRole.USER)),
        "fritz@yahoo.com" to User("fritz@yahoo.com", "fritz123", listOf(UserRole.USER)),
        "paula@yahoo.com" to User("paula@yahoo.com", "paula123"),
    )

    fun findUserByEmail(email: String): User? =
        users[email]

}