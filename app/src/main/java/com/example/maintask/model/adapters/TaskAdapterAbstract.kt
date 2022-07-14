package com.example.maintask.model.adapters

import android.content.Context
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
import com.example.maintask.model.TaskModel
import java.time.LocalDate

abstract class TaskAdapterAbstract(
    val context: Context,
    val tasks: MutableList<TaskModel>) : RecyclerView.Adapter<TaskAdapterAbstract.TaskAbstractViewHolder>() {

    companion object {
        private val FORTODAY = 0
        private val ISLATE = 1
        private val ISCOMPLETED = 2
        private val OTHERDAY = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAbstractViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskAbstractViewHolder(view)
    }

    override fun getItemCount() = tasks.size

    private fun daysLeft(days: Int) =
        when {
            days == 0 -> "Hoje"
            days > 0 -> "Faltam ${days} dias"
            else -> "${days * (-1)} atrasado"
        }

    inner class TaskAbstractViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById<TextView>(R.id.task_item_title)
        val dateTextView = view.findViewById<TextView>(R.id.task_item_date)
        val auhtorTextView = view.findViewById<TextView>(R.id.task_item_author)
        val descriptionTextView = view.findViewById<TextView>(R.id.task_item_description)
        val buttonOS = view.findViewById<Button>(R.id.task_item_details_button)
        val containerDetails = view.findViewById<ConstraintLayout>(R.id.task_item_details_container)
        val itemContainer = view.findViewById<ConstraintLayout>(R.id.task_item_container)
        val marker = view.findViewById<ImageView>(R.id.task_item_marker)
    }

    private fun changeDetailsVisibility(view: View, container: ConstraintLayout) =
        if(!container.isVisible) {
            container.visibility = View.VISIBLE
            val params = view.layoutParams
            params.height = 650
            view.layoutParams = params
        } else {
            container.visibility = View.GONE
            val params = view.layoutParams
            params.height = 210
            view.layoutParams = params
        }
}