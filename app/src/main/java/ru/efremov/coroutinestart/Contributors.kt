package ru.efremov.coroutinestart

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

enum class Variant {
    BLOCKING,         // Request1Blocking
    BACKGROUND,       // Request2Background
    CALLBACKS,        // Request3Callbacks
    SUSPEND,          // Request4Coroutine
    CONCURRENT,       // Request5Concurrent
    NOT_CANCELLABLE,  // Request6NotCancellable
    PROGRESS,         // Request6Progress
    CHANNELS          // Request7Channels
}

private enum class LoadingStatus {
    COMPLETED,
    CANCELED,
    IN_PROGRESS
}

interface Contributors: CoroutineScope {

    val job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun init() {
        // Load stored params (user & password values)
        loadInitialParams()
    }

    fun loadInitialParams() {
        setParams(loadStoredParams())
    }

    fun saveParams() {
        val params = getParams()
        if (params.username.isEmpty() && params.password.isEmpty()) {
            removeStoredParams()
        } else {
            saveParams(params)
        }
    }

    fun saveUserParamsToStorage() {
        job.cancel()
        saveParams()
    }

    fun loadContributors() {
        val (username, password, org, _) = getParams()
        val req = RequestData(username, password, org)

        clearResults()
        val service = createGitHubService(req.username, req.password)

        val startTime = System.currentTimeMillis()
        when (getSelectedVariant()) {
            Variant.BLOCKING -> { // Blocking UI thread
                val users = loadContributorsBlocking(service, req)
                updateResults(users, startTime)
            }
            Variant.BACKGROUND -> {
                loadContributorsBackground(service, req) { users -> }
            }
            Variant.CALLBACKS -> {
                loadContributorsCallbacks(service, req) { users ->
                    Log.d("adas", users.toString())
                    updateResults(users, startTime)
                }
            }
            Variant.SUSPEND -> TODO()
            Variant.CONCURRENT -> TODO()
            Variant.NOT_CANCELLABLE -> TODO()
            Variant.PROGRESS -> TODO()
            Variant.CHANNELS -> TODO()
        }
    }

    private fun clearResults() {
        updateContributors(listOf())
        updateLoadingStatus(LoadingStatus.IN_PROGRESS)
        setActionsStatus(newLoadingEnabled = false)
    }

    private fun updateLoadingStatus(
        status: LoadingStatus,
        startTime: Long? = null
    ) {
        val time = if (startTime != null) {
            val time = System.currentTimeMillis() - startTime
            "${(time / 1000)}.${time % 1000 / 100} sec"
        } else {
            ""
        }

        val text = "Loading status: " +
                when (status) {
                    LoadingStatus.COMPLETED -> "completed in $time"
                    LoadingStatus.IN_PROGRESS -> "in progress $time"
                    LoadingStatus.CANCELED -> "canceled"
                }
        setLoadingStatus(text, status == LoadingStatus.IN_PROGRESS)
    }

    private fun updateResults(
        users: List<User>,
        startTime: Long,
        completed: Boolean = true
    ) {
        updateContributors(users)
        updateLoadingStatus(
            if (completed) LoadingStatus.COMPLETED else LoadingStatus.IN_PROGRESS,
            startTime
        )
        if (completed) {
            setActionsStatus(newLoadingEnabled = true)
        }
    }

    private fun Job.setUpCancellation() {
        // make active the 'cancel' button
        setActionsStatus(newLoadingEnabled = false, cancellationEnabled = true)

        val loadingJob = this

        // update the status and remove the listener after the loading job is completed
        launch {
            loadingJob.join()
            setActionsStatus(newLoadingEnabled = true)
        }
    }

    fun getSelectedVariant(): Variant

    fun updateContributors(users: List<User>)

    fun setLoadingStatus(text: String, iconRunning: Boolean)

    fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean = false)

    fun getParams(): Params

    fun setParams(params: Params)
}
