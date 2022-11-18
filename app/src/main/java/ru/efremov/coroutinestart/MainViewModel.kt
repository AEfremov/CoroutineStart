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

//            val coroutines: List<Deferred<String>> = List(100) {
//                async(start = CoroutineStart.DEFAULT) {
//                    doWork(it.toString())
//                }
//            }

            val coroutines: List<Job> = List(100) {
                launch(CoroutineName("main coroutine"), start = CoroutineStart.DEFAULT) {
                    doWork(it.toString())
                }
            }

            coroutines.forEach {
                it.cancel("Cancel")
//                Log.d(LOG_TAG, it.await())
            }

//            repeat(100) {
//                launch(Dispatchers.IO) {
//                    doWorkAsync(it.toString())
//                }
//            }
        }
    }

    private suspend fun doWork(name: String): String {
        delay(Random().nextInt(1000).toLong())
        return "Done. $name"
    }

    private suspend fun doWorkAsync(name: String) {
        delay(Random().nextInt(1000).toLong())
        Log.d(LOG_TAG, "Done in $name")
    }

    fun <T> Flow<T>.unique(): Flow<T> {
        return flow {
            var lastValue: Any? = NoValue
            collect { value: T ->
                if (lastValue != value) {
                    lastValue = value
                    emit(value)
                }
            }
        }
    }

    private object NoValue

    companion object {

        const val LOG_TAG = "MainViewModel"
    }
}