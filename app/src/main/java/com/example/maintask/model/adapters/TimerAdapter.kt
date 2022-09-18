package com.example.maintask.model.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.viewHolder.TimerViewHolder
import com.example.maintask.viewmodel.TimerViewModel

class TimerAdapter(
    private val context: Context,
    private val taskActions: MutableList<TaskActionModel>,
    private val timerViewModel: TimerViewModel
) : RecyclerView.Adapter<TimerViewHolder>() {

    private val completedActions = Array(itemCount) { false }

    companion object {
        private const val FIRST: Int = 1
        private const val ORDERLESS: Int = 0
        private const val SECONDS_POS: Int = 2
        private const val MINUTE_POS: Int = 1
        private const val ACTION_IS_NOT_AVAILABLE_TO_START = "Essa ação não pode ser iniciada " +
                "antes da anterior ser finalizada"

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        return TimerViewHolder.create(parent, timerViewModel)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        val currentAction = taskActions[position]
        val text = "${currentAction.order} - ${currentAction.action}"
        holder.setText(text, currentAction.time)
        runStopwatch(holder, position)
        resetStopwatch(holder, position)
        setActionStatus(position)
        verifyCompletedTasks()
    }

    private fun runStopwatch(holder: TimerViewHolder, position: Int){
        val currentAction = taskActions[position]
        holder.playButton.setOnClickListener {
            if (isAbleToStart(position)) {
                holder.startStop(currentAction)
                setActionStatus(position)
                verifyCompletedTasks()
            }
            else
                Toast.makeText(
                    context,
                    ACTION_IS_NOT_AVAILABLE_TO_START,
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    private fun resetStopwatch(holder: TimerViewHolder, position: Int){
        holder.resetButton.setOnClickListener {
            holder.reset(taskActions[position])
            setActionStatus(position)
            verifyCompletedTasks()
        }
    }

    private fun verifyCompletedTasks() {
        if (completedActions.contains(false))
            timerViewModel.hasFinishedAllActions(false)
        else
            timerViewModel.hasFinishedAllActions(true)
    }

    private fun setActionStatus(position: Int) {
        val wasCompleted = wasCompleted(position)
        completedActions[position] = wasCompleted
    }

    private fun wasCompleted(position: Int): Boolean {
        val seconds = taskActions[position].time.split(":")[SECONDS_POS].toInt()
        val minutes = taskActions[position].time.split(":")[MINUTE_POS].toInt()
        if ((seconds == 0) && (minutes == 0))
            return false
        return true
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
        return wasCompleted(lastPosition)
    }

    override fun getItemCount() = taskActions.size
}