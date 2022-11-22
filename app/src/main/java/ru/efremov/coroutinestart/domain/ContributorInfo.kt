package ru.efremov.coroutinestart.domain

import ru.efremov.coroutinestart.data.network.User

data class ContributorInfo(
    val login: String,
    val contributionsCount: Int
)

fun User.toContributorInfo(): ContributorInfo {
    return ContributorInfo(
        login = login,
        contributionsCount = contributions
    )
}
