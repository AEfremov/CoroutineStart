package ru.efremov.coroutinestart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*

class MainViewModel : ViewModel() {

    fun main() {
        viewModelScope.launch {
            doWorld()
            log("DONE!")
//            repeat(100_000) {
//                launch {
//                    delay(1000)
//                    log(".")
//                }
//            }
        }
    }

    private suspend fun doWorld() = coroutineScope {
        val job1 = launch {
            delay(2000)
            log("World 2!")
        }
//        val job2 = launch {
//            delay(1000)
//            log("World 1!")
//        }
        log("Hello")
        job1.join()
//        job2.join()
    }

    private suspend fun doRepeat() = coroutineScope {
        repeat(100_000) {
            launch {
                delay(1000)
                log(".")
            }
        }
    }

    private fun log(message: String) {
        Log.d(LOG_TAG, message)
    }

    companion object {

        const val LOG_TAG = "MainViewModel"
    }
}