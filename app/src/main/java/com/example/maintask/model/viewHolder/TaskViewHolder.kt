package com.example.maintask.model.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.database.entity.TaskEntity
import java.time.LocalDate

class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val titleTextView: TextView = view.findViewById(R.id.task_item_title)
    private val dateTextView: TextView = view.findViewById(R.id.task_item_date)
    private val authorTextView: TextView = view.findViewById(R.id.task_item_author)
    private val descriptionTextView: TextView = view.findViewById<TextView>(R.id.task_item_description)
    private val buttonOS: Button = view.findViewById(R.id.task_item_details_button)
    private val containerDetails: ConstraintLayout = view.findViewById(R.id.task_item_details_container)
    private val itemContainer: ConstraintLayout = view.findViewById(R.id.task_item_container)
    val marker = view.findViewById<ImageView>(R.id.task_item_marker)

    companion object {
        private const val STANDARD_ITEM_SIZE = 210
        private const val SELECTED_ITEM_SIZE = 650
        fun create(parent: ViewGroup): TaskViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
            return TaskViewHolder(view)
        }
    }

    fun bind(task: TaskEntity, block: (taskId: Int) -> Unit){
        titleTextView.text = task.title
        dateTextView.text = daysLeft(LocalDate.parse(task.date).dayOfYear - LocalDate.now().dayOfYear)
        descriptionTextView.text = task.description
        authorTextView.text = task.author

        itemContainer.setOnClickListener { constraintLayout ->
            changeDetailLayoutVisibility(containerDetails)
            resizeLayout(constraintLayout, containerDetails.isVisible)
        }

        buttonOS.setOnClickListener {
            block(task.id)
        }
    }
    private fun daysLeft(days: Int) =
        when {
            days == 0 -> "Hoje"
            days > 0 -> "Faltam ${days} dias"
            else -> "atrasado há ${days * (-1)} dia(s)"
        }

    private fun changeDetailLayoutVisibility(layout: ConstraintLayout) {
        val isVisible = layout.isVisible
        if (isVisible) layout.visibility = View.GONE
        else layout.visibility = View.VISIBLE
    }

    private fun resizeLayout(layout: View, detailLayoutIsVisible: Boolean) {
        val params = layout.layoutParams

        if (detailLayoutIsVisible) {
            params.height = SELECTED_ITEM_SIZE
            layout.layoutParams = params
        }
        else {
            params.height = STANDARD_ITEM_SIZE
            layout.layoutParams = params
        }
    }
}