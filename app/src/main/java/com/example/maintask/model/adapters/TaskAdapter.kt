package com.example.maintask.model.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.TaskModel

class TaskAdapter(val context: Context, private val tasks: MutableList<TaskModel>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.titleTextView.text = tasks[position].title
        //holder.dateTextView.text = tasks[position].date.toString()
    }

    override fun getItemCount() = tasks.size

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById<TextView>(R.id.task_item_title)
        val dateTextView = view.findViewById<TextView>(R.id.task_item_date)
        val auhtorTextView = view.findViewById<TextView>(R.id.task_item_author)
        val descriptionTextView = view.findViewById<TextView>(R.id.task_item_description)
        val buttonOS = view.findViewById<Button>(R.id.task_item_details_button)
        val containetDetails = view.findViewById<ConstraintLayout>(R.id.task_item_container)
        val marker = view.findViewById<ImageView>(R.id.task_item_marker)
    }
}