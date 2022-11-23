package ru.efremov.coroutinestart

import kotlinx.coroutines.*
import ru.efremov.coroutinestart.core.logDebug
import ru.efremov.coroutinestart.core.logRepos
import ru.efremov.coroutinestart.core.logUsers
import ru.efremov.coroutinestart.data.aggregate
import ru.efremov.coroutinestart.data.network.GitHubService
import ru.efremov.coroutinestart.data.network.RequestData
import ru.efremov.coroutinestart.data.network.User

suspend fun loadContributorsConcurrent(
    service: GitHubService,
    req: RequestData
): List<User> = coroutineScope {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        async(Dispatchers.Default) {
            logDebug("async", "starting loading for ${repo.name}")
            service.getRepoContributors(req.org, repo.name)
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }
    deferreds.awaitAll().flatten().aggregate()
}