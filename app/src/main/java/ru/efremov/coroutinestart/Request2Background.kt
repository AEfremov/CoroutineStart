package ru.efremov.coroutinestart

import ru.efremov.coroutinestart.data.network.GitHubService
import ru.efremov.coroutinestart.data.network.RequestData
import ru.efremov.coroutinestart.data.network.User
import kotlin.concurrent.thread

fun loadContributorsBackground(
    service: GitHubService,
    req: RequestData,
    updateResults: (List<User>) -> Unit
) {
    thread {
        loadContributorsBlocking(service, req)
    }
}