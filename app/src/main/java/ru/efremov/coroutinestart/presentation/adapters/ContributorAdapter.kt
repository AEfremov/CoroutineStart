package ru.efremov.coroutinestart.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.efremov.coroutinestart.databinding.RowContributorBinding
import ru.efremov.coroutinestart.domain.ContributorInfo

class ContributorAdapter(
    private val context: Context
) : ListAdapter<ContributorInfo, ContributorViewHolder>(ContributorDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorViewHolder {
        val binding = RowContributorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContributorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContributorViewHolder, position: Int) {
        val info = getItem(position)
        with(holder.binding) {
            tvName.text = info.login
            tvCount.text = info.contributionsCount.toString()
        }
    }
}