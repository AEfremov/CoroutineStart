package ru.efremov.coroutinestart.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.SelectClause0
import kotlinx.coroutines.selects.SelectInstance
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class BasicViewModel : ViewModel() {

    private val parentJob = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d(LOG_TAG, "Exception caught: $throwable")
    }
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + exceptionHandler)

    fun main() {

        val childJob1 = coroutineScope.launch {
            delay(3000)
            Log.d(LOG_TAG, "first coroutine finished")
        }

        val childJob2 = coroutineScope.launch {
//            error()
            delay(2000)
            Log.d(LOG_TAG, "second coroutine finished")

            launch(CoroutineName("3rd")) {
                delay(100)
                error()
            }

            withContext(Dispatchers.Main) {
                delay(100)
                error()
            }
        }

        val childJob3 = coroutineScope.launch {
            delay(1000)
            error()
            Log.d(LOG_TAG, "third coroutine finished")
        }

//        viewModelScope.launch {
//            supervisorScope {
//            }
//        }

//        coroutineScope.launch {
//            childJob1.join()
//            childJob2.join()
//        }


//        Log.d(LOG_TAG, "main $this")

//        Log.d(LOG_TAG, parentJob.children.contains(childJob1).toString())
//        Log.d(LOG_TAG, parentJob.children.contains(childJob2).toString())

//        parentJob.children.asSequence().forEach {
//            Log.d(LOG_TAG, "${it.key} - $it")
//        }
    }

    private fun error() {
        throw RuntimeException()
    }

    suspend fun computeCoroutineScope(): String = coroutineScope {
        val color = async { delay(60_000); "purple" }
        val height = async<Double> { delay(100); throw Exception() }
        "%s %.1f".format(color.await(), height.await())
    }

    suspend fun computeSupervisorScope(): String = supervisorScope {
        val color = async { delay(60_000); "purple" }
        val height = async<Double> { delay(100); throw Exception() }
        "%s %.1f".format(color.await(), height.await())
    }

    fun method() {
        val job = viewModelScope.launch(Dispatchers.Default) {
            Log.d(LOG_TAG, "Started")
            val before = System.currentTimeMillis()
            var count = 0
            for (i in 0 until 100_000_000) {
                for (j in 0 until 100) {
                    ensureActive()
                    count++
                }
            }
            Log.d(LOG_TAG, "Finished: ${System.currentTimeMillis() - before}")
        }
        job.invokeOnCompletion {
            Log.d(LOG_TAG, "Coroutine was cancelled. $it")
        }
        viewModelScope.launch {
            delay(3000)
            job.cancel()
        }
    }


    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    companion object {

        private const val LOG_TAG = "BasicViewModel"
    }
}