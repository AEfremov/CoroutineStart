package ru.efremov.coroutinestart.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.efremov.coroutinestart.databinding.ActivityMainBinding
import ru.efremov.coroutinestart.presentation.adapters.ContributorAdapter

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
//                    binding.tvOrganization.text = it.organizationName
//                    binding.tvVariant.text = it.loadingVariant
                }
                is MainViewModel.State.ContributorsList -> {
                    Log.d("ContributorsList", it.items.toString())
                    val adapter = ContributorAdapter(this)
                    binding.rvContributors.adapter = adapter
                    binding.rvContributors.itemAnimator = null
                    val llm = LinearLayoutManager(this@MainActivity)
                    val decor = DividerItemDecoration(
                        binding.rvContributors.context,
                        llm.orientation
                    )
                    binding.rvContributors.addItemDecoration(decor)
                    adapter.submitList(it.items)
                }
            }
        }

        binding.buttonLoad.setOnClickListener {
            binding.buttonLoad.isEnabled = false // todo
            vm.setUserParams()
        }

        binding.buttonSaveParams.setOnClickListener {
            vm.saveUserParamsToStorage()
        }

        vm.progress.observe(this) {
            if (it.isNotEmpty()) {
                binding.llProgress.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                binding.tvProgressText.text = it
            } else {
                binding.llProgress.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.tvProgressText.text = ""
            }
        }
    }
}