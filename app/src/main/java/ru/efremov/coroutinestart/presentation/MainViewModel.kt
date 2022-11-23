package ru.efremov.coroutinestart.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.efremov.coroutinestart.*
import ru.efremov.coroutinestart.core.logDebug
import ru.efremov.coroutinestart.data.login
import ru.efremov.coroutinestart.data.network.User
import ru.efremov.coroutinestart.data.token
import ru.efremov.coroutinestart.domain.ContributorInfo
import ru.efremov.coroutinestart.domain.toContributorInfo
import kotlin.concurrent.thread

class MainViewModel : ViewModel(), Contributors {

    sealed class State {
        data class UserParams(
            var userName: String,
            var passwordOrToken: String,
            var organizationName: String,
            var loadingVariant: String
        ): State()
        data class ContributorsList(
            var items: List<ContributorInfo>
        ): State()
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    override val job: Job
        get() = Job()

    override fun getParams(): Params {
        return Params(
            login,
            token,
            "kotlin",
            Variant.SUSPEND
        )
    }

    override fun setParams(params: Params) {
        _state.value = State.UserParams(
            userName = params.username,
            passwordOrToken = params.password,
            organizationName = params.org,
            loadingVariant = params.variant.name
        )
    }

    override fun getSelectedVariant(): Variant {
        return Variant.SUSPEND
    }

    override fun updateContributors(users: List<User>) {
        if (users.isNotEmpty()) {
            val contributors = users
                .map {
                    it.toContributorInfo()
                }.toList()
            _state.value = State.ContributorsList(
                items = contributors
            )
        }
    }

    override fun setLoadingStatus(text: String, iconRunning: Boolean) {
        logDebug(LOG_TAG, text)
    }

    override fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean) {
        logDebug(
            LOG_TAG, "newLoadingEnabled=$newLoadingEnabled," +
                " cancellationEnabled=$cancellationEnabled")
    }

    fun setUserParams() {
        saveParams()
        loadContributors()
    }

    fun main() {
        init()
        viewModelScope.launch {
            val deferred: Deferred<Int> = async {
                loadData()
            }
            logDebug("deferred", "waiting...")
            logDebug("await", "${deferred.await()}")
        }
    }

    private suspend fun loadData(): Int {
        logDebug("loadData", "loading...")
        delay(1000L)
        logDebug("loadData", "loaded!")
        return 100
    }

    private suspend fun doWorld() = coroutineScope {
        val job1 = launch {
            delay(2000)
//            logDebug("World 2!")
        }
//        val job2 = launch {
//            delay(1000)
//            log("World 1!")
//        }
//        logDebug("Hello")
        job1.join()
//        job2.join()
    }

    private suspend fun doRepeat() = coroutineScope {
        repeat(100_000) {
            launch {
                delay(1000)
//                logDebug(".")
            }
        }
    }

    companion object {

        const val LOG_TAG = "MainViewModel"
    }
}