package ru.efremov.coroutinestart

import kotlinx.coroutines.coroutineScope
import ru.efremov.coroutinestart.data.network.GitHubService
import ru.efremov.coroutinestart.data.network.RequestData
import ru.efremov.coroutinestart.data.network.User

suspend fun loadContributorsConcurrent(
    service: GitHubService,
    req: RequestData
): List<User> = coroutineScope {
    TODO()
}