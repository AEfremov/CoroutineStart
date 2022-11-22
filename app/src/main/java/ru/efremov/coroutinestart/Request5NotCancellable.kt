package ru.efremov.coroutinestart

import ru.efremov.coroutinestart.data.network.GitHubService
import ru.efremov.coroutinestart.data.network.RequestData
import ru.efremov.coroutinestart.data.network.User

suspend fun loadContributorsNotCancellable(
    service: GitHubService,
    req: RequestData
): List<User> {
    TODO()
}