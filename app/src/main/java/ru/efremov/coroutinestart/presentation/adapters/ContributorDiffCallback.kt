package ru.efremov.coroutinestart.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.efremov.coroutinestart.domain.ContributorInfo

object ContributorDiffCallback : DiffUtil.ItemCallback<ContributorInfo>() {

    override fun areItemsTheSame(oldItem: ContributorInfo, newItem: ContributorInfo): Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(oldItem: ContributorInfo, newItem: ContributorInfo): Boolean {
        return oldItem == newItem
    }
}