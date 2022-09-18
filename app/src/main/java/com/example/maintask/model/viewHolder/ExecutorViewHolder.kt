package com.example.maintask.model.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.executor.Executor

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

    fun bind(executor: Executor) {
        idTextView.text = executor.id.toString()
        nameTextView.text = executor.name
        elapsedTimeTextView.text = executor.elapsedTime
    }
}