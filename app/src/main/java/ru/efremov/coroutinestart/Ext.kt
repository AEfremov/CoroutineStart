package ru.efremov.coroutinestart

import android.util.Log
import retrofit2.Response

fun logDebug(tag: String, msg: String?) {
    Log.d(tag, msg ?: "")
}

fun logError(tag: String, msg: String?) {
    Log.e(tag, msg ?: "")
}

fun logRepos(req: RequestData, response: Response<List<Repo>>) {
    val repos = response.body()
    if (!response.isSuccessful || repos == null) {
        logError("Repos", "Failed loading repos for ${req.org} with response: '${response.code()}: ${response.message()}'")
    } else {
        logDebug("Repos", "${req.org}: loaded ${repos.size} repos")
    }
}

fun logUsers(repo: Repo, response: Response<List<User>>) {
    val users = response.body()
    if (!response.isSuccessful || users == null) {
        logError("Users", "Failed loading contributors for ${repo.name} with response '${response.code()}: ${response.message()}'")
    } else {
        logDebug("Users", "${repo.name}: loaded ${users.size} contributors")
    }
}