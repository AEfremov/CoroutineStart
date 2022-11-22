package ru.efremov.coroutinestart.data

import ru.efremov.coroutinestart.data.network.User

fun List<User>.aggregate(): List<User> =
    groupBy { it.login }
        .map { (login, group) -> User(login, group.sumOf { it.contributions }) }
        .sortedByDescending { it.contributions }