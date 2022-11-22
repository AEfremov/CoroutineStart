package ru.efremov.coroutinestart

import kotlinx.coroutines.coroutineScope

suspend fun loadContributorsConcurrent(
    service: GitHubService,
    req: RequestData
): List<User> = coroutineScope {
    TODO()
}