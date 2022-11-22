package ru.efremov.coroutinestart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.efremov.coroutinestart.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val vm by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        vm.main()

        vm.state.observe(this) {
            when (it) {
                is MainViewModel.State.UserParams -> {
                    binding.inputUserName.setText(it.userName)
                    binding.inputPassToken.setText(it.passwordOrToken)
                    binding.tvOrganization.text = it.organizationName
                    binding.tvVariant.text = it.loadingVariant
                }
                is MainViewModel.State.ContributorsList -> {
                    Log.d("ContributorsList", it.items.toString())
                }
            }
        }

        binding.buttonLoad.setOnClickListener {
            vm.setUserParams()
        }

        binding.buttonSaveParams.setOnClickListener {
            vm.saveUserParamsToStorage()
        }
    }
}