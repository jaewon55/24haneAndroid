package com.hane24.hoursarenotenough24.inoutlog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hane24.hoursarenotenough24.data.LogTableItem
import com.hane24.hoursarenotenough24.databinding.FragmentLogListLogsItemBinding

class LogTableAdapter :
    ListAdapter<LogTableItem, LogTableAdapter.LogTableViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogTableViewHolder =
        LogTableViewHolder(
            FragmentLogListLogsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LogTableViewHolder, position: Int) {
        val logData = getItem(position)
        holder.bind(logData)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LogTableItem>() {
        override fun areItemsTheSame(oldItem: LogTableItem, newItem: LogTableItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LogTableItem, newItem: LogTableItem): Boolean {
            return oldItem == newItem
        }
    }

    class LogTableViewHolder(private val binding: FragmentLogListLogsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(logData: LogTableItem) {
            binding.logData = logData
        }
    }

}