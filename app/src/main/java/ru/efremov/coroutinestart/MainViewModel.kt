package ru.efremov.coroutinestart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MainViewModel : ViewModel() {

    fun method() {
        val job = viewModelScope.launch(Dispatchers.Default) {
            Log.d(LOG_TAG, "Started")
            val before = System.currentTimeMillis()
            var count = 0
            for (i in 0 until 100_000_000) {
                for (j in 0 until 100) {
                    ensureActive()
                    count++
//                    if (isActive) {
//                        count++
//                    } else {
//                        throw CancellationException()
//                    }
                }
            }
            Log.d(LOG_TAG, "Finished: ${System.currentTimeMillis() - before}")
        }
        job.invokeOnCompletion {
            Log.d(LOG_TAG, "Coroutine was cancelled: $it")
        }
        viewModelScope.launch {
            delay(3000)
            job.cancel()
        }
    }


////    private val parentJob = Job()
//    private val parentJob = SupervisorJob()
//    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        Log.d(LOG_TAG, "exception caught: $throwable")
//    }
//    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + exceptionHandler)
//
//    fun method() {
//        val childJob1 = coroutineScope.launch {
//            delay(3000)
//            Log.d(LOG_TAG, "1 coroutine finished")
//        }
//        val childJob2 = coroutineScope.launch {
//            delay(2000)
//            Log.d(LOG_TAG, "2 coroutine finished")
//            launch {
//                Log.d(LOG_TAG, "2 coroutine do something")
//                error()
//            }
//        }
//        val childJob3 = coroutineScope.async {
//            delay(1000)
//            error()
//            Log.d(LOG_TAG, "3 coroutine finished")
//        }
//        coroutineScope.launch {
//            childJob3.await()
//        }
////        val childJob3 = coroutineScope.launch {
////            val res = async {
////                delay(1000)
////                error()
////                Log.d(LOG_TAG, "3 coroutine finished")
////            }
////        }
//    }
//
//    private fun error() {
//        throw java.lang.RuntimeException()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        coroutineScope.cancel()
//    }

    companion object {

        const val LOG_TAG = "MainViewModel"
    }
}