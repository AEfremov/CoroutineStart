package ru.efremov.coroutinestart

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