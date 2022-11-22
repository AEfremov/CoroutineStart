package ru.efremov.coroutinestart

import ru.efremov.coroutinestart.core.logRepos
import ru.efremov.coroutinestart.core.logUsers
import ru.efremov.coroutinestart.data.aggregate
import ru.efremov.coroutinestart.data.network.GitHubService
import ru.efremov.coroutinestart.data.network.RequestData
import ru.efremov.coroutinestart.data.network.User

suspend fun loadContributorsSuspend(
    service: GitHubService,
    req: RequestData
): List<User> {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    return repos.flatMap { repo ->
        service.getRepoContributors(req.org, repo.name)
            .also { logUsers(repo, it) }
            .bodyList()
    }.aggregate()
}