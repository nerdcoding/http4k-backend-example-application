package org.nerdcoding.example.http4k.service.auth

import org.nerdcoding.example.http4k.model.User


class UserService {

    // map of fixed users until database is connected
    private val users = mapOf(
        "heide@gmail.com" to User("heide@gmail.com", "heide123"),
        "fritz@yahoo.com" to User("fritz@yahoo.com", "fritz123"),
    )

    fun findUserByEmail(email: String): User? =
        users[email]

}