package com.example.maintask.model.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.model.task.TaskModel
import java.time.LocalDate

class TaskAdapter(context: Context, tasks: MutableList<TaskModel>): TaskAdapterAbstract(context, tasks){

    private var callbacks: MainActivityCallbacks? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        callbacks = context as MainActivityCallbacks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAbstractViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskAbstractViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskAbstractViewHolder, position: Int) {
        holder.titleTextView.text = tasks[position].title
        holder.dateTextView.text = daysLeft(tasks[position].date.dayOfYear - LocalDate.now().dayOfYear)
        holder.descriptionTextView.text = tasks[position].description
        holder.auhtorTextView.text = tasks[position].author

        holder.itemContainer.setOnClickListener { constraintLayout ->
            changeDetailLayoutVisibility(holder.containerDetails)
            resizeLayout(constraintLayout, holder.containerDetails.isVisible)
        }

        holder.buttonOS.setOnClickListener {
            val destinationId = R.id.action_taskFragment_to_detailTaskFragment
            callbacks?.selectedTask = tasks[position]
            changeFragment(destinationId)
        }
    }

}