package com.example.maintask.model.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.viewmodel.TimerViewModel

class TimerAdapter(
    private val context: Context,
    private val taskActions: MutableList<TaskActionModel>,
    private val timerViewModel: TimerViewModel
) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    private val completedActions = Array(itemCount) { _ -> false }
    companion object {
        private const val ACTION_IS_NOT_AVAILABLE_TO_START = "Essa ação não pode ser iniciada " +
                "antes da anterior ser finalizada"
    }

    inner class TimerViewHolder(
        timerRecyclerView: View
    ) : RecyclerView.ViewHolder(timerRecyclerView) {
        val action: TextView = timerRecyclerView.findViewById(R.id.timer_item_action)
        val elapsedTime: TextView = timerRecyclerView.findViewById(R.id.timer_item_elapsedTime)
        val playButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_play_button)
        val resetButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_reset_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val timerRecyclerView =
            LayoutInflater.from(context).inflate(R.layout.timer_item, parent, false)
        return TimerViewHolder(timerRecyclerView)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.action.text = "${taskActions[position].order} - ${taskActions[position].action}"
        holder.elapsedTime.text = taskActions[position].time
        holder.playButton.setOnClickListener {
            if (verifyAvailableToStart(position)) {
                startStopStopwatch(
                    taskActions[position],
                    holder.playButton,
                    holder.elapsedTime
                )
            }
            else errorMessage(ACTION_IS_NOT_AVAILABLE_TO_START)
            verifyIfActionsWasCompleted(position)
            allTaskWasCompleted()
        }
        holder.resetButton.setOnClickListener {
            resetStopwatch(taskActions[position], holder.playButton, holder.elapsedTime)
        }
    }

    private fun verifyIfActionsWasCompleted(position: Int) {
        val wasCompleted = verifyAvailableToStart(position)
        completedActions[position] = wasCompleted
    }

    private fun verifyAvailableToStart(position: Int): Boolean {
        if (isTheFirstAction(position) || !isOrderlyAction(position))
            return true
        return wasCompleted(position)
    }

    private fun wasCompleted(position: Int): Boolean {
        val seconds = taskActions[position - 1].time.split(":")[2].toInt()
        val minutes = taskActions[position - 1].time.split(":")[1].toInt()
        if ((seconds == 0) && (minutes == 0))
            return false
        return true
    }

    private fun isTheFirstAction(position: Int) = position <= 0

    private fun isOrderlyAction(position: Int) = taskActions[position].order > 0

    private fun allTaskWasCompleted() {
        if (completedActions.contains(false))
            timerViewModel.setCompletedActions(false)
        else
            timerViewModel.setCompletedActions(true)
    }

    private fun startStopStopwatch(
        action: TaskActionModel,
        button: ImageButton,
        elapsedTime: TextView
    ) {
        if (action.isStopwatchRunning()) {
            action.pauseStopwatch()
            button.setImageResource(R.drawable.ic_play)
            timerViewModel.setCurrentAction(action.id, action.elapsedTime())
            action.time = elapsedTime.text.toString()
        } else {
            action.startStopwatch(elapsedTime)
            button.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun errorMessage(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun resetStopwatch(
        action: TaskActionModel,
        button: ImageButton,
        elapsedTime: TextView
    ) {
        action.resetStopwatch()
        elapsedTime.text = action.elapsedTime()
        button.setImageResource(R.drawable.ic_play)
        timerViewModel.setCurrentAction(action.id, action.elapsedTime())
    }

    override fun getItemCount() = taskActions.size
}