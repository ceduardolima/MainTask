package com.example.maintask.model.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.viewmodel.TimerViewModel

class TimerAdapter(
    private val context: Context,
    private val taskActions: MutableList<TaskActionModel>,
    private val timerViewModel: TimerViewModel
) : RecyclerView.Adapter<TimerViewHolder>() {

    private val completedActions = Array(itemCount) { _ -> false }

    companion object {
        private const val FIRST: Int = 1
        private const val ORDERLESS: Int = 0
        private const val SECONDS_POS: Int = 2;
        private const val MINUTE_POS: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        return TimerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        val currentAction = taskActions[position]
        holder.bind(context, currentAction, timerViewModel){
                isAbleToStart(position)
            }
        verifyIfActionsWasCompleted(position)
        allTaskWasCompleted()
    }

    private fun allTaskWasCompleted() {
        if (completedActions.contains(false)) timerViewModel.setCompletedActions(false)
        else timerViewModel.setCompletedActions(true)
    }

    private fun verifyIfActionsWasCompleted(position: Int) {
        val wasCompleted = isAbleToStart(position)
        completedActions[position] = wasCompleted
    }

    private fun isAbleToStart(position: Int): Boolean {
        val currentAction = taskActions[position]
        return when (currentAction.order) {
            FIRST -> true
            ORDERLESS -> true
            else -> previousActionWasCompleted(position)
        }
    }

    private fun previousActionWasCompleted(position: Int): Boolean {
        val lastPosition = position - 1
        val seconds = taskActions[lastPosition].time.split(":")[SECONDS_POS].toInt()
        val minutes = taskActions[lastPosition].time.split(":")[MINUTE_POS].toInt()
        if ((seconds == 0) && (minutes == 0))
            return false
        return true
    }

    override fun getItemCount() = taskActions.size
}