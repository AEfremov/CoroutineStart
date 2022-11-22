package ru.efremov.coroutinestart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*

class MainViewModel : ViewModel(), Contributors {

    // todo убрать перед коммитом
    private val githubUserName = "AEfremov"
    private val githubToken = "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"

    sealed class State {
        data class UserParams(
            var userName: String,
            var passwordOrToken: String,
            var organizationName: String,
            var loadingVariant: String
        ): State()
        data class ContributorsList(
            var items: List<User>
        ): State()
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    override val job: Job
        get() = Job()

    override fun getParams(): Params {
        return Params(githubUserName, githubToken, "kotlin", Variant.CALLBACKS)
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
        return Variant.CALLBACKS
    }

    override fun updateContributors(users: List<User>) {
        if (users.isNotEmpty()) {
            _state.value = State.ContributorsList(
                items = users
            )
            // todo отобразить юзеров в recyclerview
        }
    }

    override fun setLoadingStatus(text: String, iconRunning: Boolean) {
        logDebug(LOG_TAG, text)
    }

    override fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean) {
        logDebug(LOG_TAG, "newLoadingEnabled=$newLoadingEnabled," +
                " cancellationEnabled=$cancellationEnabled")
    }

    fun setUserParams() {
        saveParams()
        loadContributors()
    }

    fun main() {
        init()
//        viewModelScope.launch {
//            doWorld()
//            logDebug("DONE!")
////            repeat(100_000) {
////                launch {
////                    delay(1000)
////                    log(".")
////                }
////            }
//        }
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