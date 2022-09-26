package com.example.maintask.model.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.executor.Executor
import com.example.maintask.model.stopwatch.StopwatchStateHolder

class ExecutorViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private val profileImageView: ImageView = view.findViewById(R.id.worker_list_item_user_img)
    private val idTextView: TextView = view.findViewById(R.id.worker_list_item_user_id)
    private val nameTextView: TextView = view.findViewById(R.id.worker_list_item_user_name)
    private val elapsedTimeTextView: TextView = view.findViewById(R.id.worker_list_item_time_spend)

    companion object {
        fun create(parent: ViewGroup): ExecutorViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.worker_list_item, parent,false)
            return ExecutorViewHolder(view)
        }
    }

    fun bind(teamMember: TeamMemberEntity) {
        idTextView.text = teamMember.id.toString()
        nameTextView.text = teamMember.name
        elapsedTimeTextView.text = format(teamMember.workTime)
    }

    private fun format(timestamp: Long): String {
        val secondsFormatted = (timestamp % 60).pad(2)
        val minutes = timestamp / 60
        val minutesFormatted = (minutes % 60).pad(2)
        val hoursFormatted = (minutes / 60).pad(2)
        return when {
            (timestamp > 0) -> "$hoursFormatted:$minutesFormatted:$secondsFormatted"
            (timestamp.compareTo(0) == 0) -> StopwatchStateHolder.DEFAULT_TIME
            else -> "00:00:00"
        }
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')
}