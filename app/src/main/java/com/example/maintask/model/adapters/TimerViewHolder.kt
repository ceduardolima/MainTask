package com.example.maintask.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.viewmodel.TimerViewModel

class TimerViewHolder(
    timerRecyclerView: View,
    private val timerViewModel: TimerViewModel
) : RecyclerView.ViewHolder(timerRecyclerView) {
    val action: TextView = timerRecyclerView.findViewById(R.id.timer_item_action)
    val elapsedTime: TextView = timerRecyclerView.findViewById(R.id.timer_item_elapsedTime)
    val playButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_play_button)
    val resetButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_reset_button)

    companion object {
        fun create(parent: ViewGroup, timerViewModel: TimerViewModel): TimerViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.timer_item, parent, false)
            return TimerViewHolder(view, timerViewModel)
        }
    }

    fun setText(action: String, elapsedTime: String){
        this.action.text = action
        this.elapsedTime.text = elapsedTime
    }

    fun startStop(
        actionModel: TaskActionModel,
    ) {
        startStopStopwatch(actionModel, playButton, elapsedTime)
    }

    fun reset(taskActionModel: TaskActionModel){
        resetStopwatch(taskActionModel, playButton, elapsedTime)
    }

    private fun startStopStopwatch(
        action: TaskActionModel,
        button: ImageButton,
        elapsedTime: TextView
    ) {
        if (action.isStopwatchRunning()) {
            action.pauseStopwatch()
            button.setImageResource(R.drawable.ic_play)
            action.time = elapsedTime.text.toString()
            timerViewModel.updateTaskAction(action.id, action.time)
        } else {
            action.startStopwatch(elapsedTime)
            button.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun resetStopwatch(
        action: TaskActionModel,
        button: ImageButton,
        elapsedTime: TextView
    ) {
        action.resetStopwatch()
        elapsedTime.text = action.elapsedTime()
        button.setImageResource(R.drawable.ic_play)
        timerViewModel.updateTaskAction(action.id, action.time)
    }

}