package com.example.maintask.model.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.daysLeft.DaysLeft
import com.example.maintask.model.task.StatusCode
import com.google.rpc.context.AttributeContext
import java.time.LocalDate

class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val titleTextView: TextView = view.findViewById(R.id.task_item_title)
    private val dateTextView: TextView = view.findViewById(R.id.task_item_date)
    private val itemContainer: ConstraintLayout = view.findViewById(R.id.task_item_container)
    private val marker = view.findViewById<ImageView>(R.id.task_item_marker)

    companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
            return TaskViewHolder(view)
        }
    }

    fun bind(task: TaskEntity, block: (taskId: Int) -> Unit){
        setText(task)
        setMarker(task)
        changeFragment(task) { block(task.id) }
    }

    private fun setText(task: TaskEntity) {
        val days = LocalDate.parse(task.date).dayOfYear
        titleTextView.text = task.title
        if (task.status == StatusCode.COMPLETED)
            dateTextView.text = "Finalizada"
        else
            dateTextView.text = DaysLeft.getDaysLeftString(days)
    }

    private fun setMarker(task: TaskEntity) {
        if (task.isEmergency)
            marker.setBackgroundResource(R.drawable.task_item_marker_red)
        else
            marker.setBackgroundResource(R.drawable.task_item_marker_grey)
    }

    private fun changeFragment(task: TaskEntity, block: () -> Unit) {
        itemContainer.setOnClickListener {
            if (isTaskCompleted(task))
                Toast.makeText(view.context, "O.S já foi finalizada.", Toast.LENGTH_LONG).show()
            else block()
        }
    }

    private fun isTaskCompleted(task: TaskEntity): Boolean {
        return task.status == StatusCode.COMPLETED
    }
}