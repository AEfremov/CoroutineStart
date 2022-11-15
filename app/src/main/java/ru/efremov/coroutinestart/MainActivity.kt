package ru.efremov.coroutinestart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.efremov.coroutinestart.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

//    private val handler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            Log.d("message", msg.toString())
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
            binding.progress.isVisible = true
            binding.buttonLoad.isEnabled = false

            val deferredCity: Deferred<String> = lifecycleScope.async {
                loadCity()
            }
            val deferredTemperature: Deferred<Int> = lifecycleScope.async {
                val temperature = loadTemperature()
                temperature
            }
            lifecycleScope.launch {
                val city = deferredCity.await()
                val temp = deferredTemperature.await()
                binding.tvLocation.text = city
                binding.tvTemperature.text = temp.toString()
                Toast.makeText(
                    this@MainActivity,
                    "City: $city Temp: $temp",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
//        handler.sendMessage(Message.obtain(handler, 0, 17))
    }

    private suspend fun loadData() {
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        val city = loadCity()
        binding.tvLocation.text = city
        val temperature = loadTemperature()
        binding.tvTemperature.text = temperature.toString()
    }

    private fun loadWithoutCoroutine(step: Int = 0, obj: Any? = null) {
        when (step) {
            0 -> {
                binding.progress.isVisible = true
                binding.buttonLoad.isEnabled = false
                loadCityWithoutCoroutine {
                    loadWithoutCoroutine(1, it)
                }
            }
            1 -> {
                val city = obj as String
                binding.tvLocation.text = city
                loadTemperatureWithoutCoroutine(city) {
                    loadWithoutCoroutine(2, it)
                }
            }
            2 -> {
                val temp = obj as Int
                binding.tvTemperature.text = temp.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
    }

    private fun loadCityWithoutCoroutine(callback: (String) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke("Moscow")
        }, 5000)
    }

    private suspend fun loadCity(): String {
        delay(5000)
        return "Moscow"
    }

    private suspend fun loadTemperature(): Int {
        delay(5000)
        return 17
    }

    private fun loadTemperatureWithoutCoroutine(city: String, callback: (Int) -> Unit) {
        Toast.makeText(
            this,
            getString(R.string.loading_temperature_toast, city),
            Toast.LENGTH_SHORT
        ).show()

        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke(17)
        }, 5000)
    }

}