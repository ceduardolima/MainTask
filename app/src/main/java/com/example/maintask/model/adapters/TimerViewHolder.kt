package com.example.maintask.model.adapters

import android.content.Context
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

class TimerViewHolder(
    timerRecyclerView: View
) : RecyclerView.ViewHolder(timerRecyclerView) {
    val action: TextView = timerRecyclerView.findViewById(R.id.timer_item_action)
    val elapsedTime: TextView = timerRecyclerView.findViewById(R.id.timer_item_elapsedTime)
    val playButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_play_button)
    val resetButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_reset_button)

    companion object {
        private const val ACTION_IS_NOT_AVAILABLE_TO_START = "Essa ação não pode ser iniciada " +
                "antes da anterior ser finalizada"
        fun create(parent: ViewGroup): TimerViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.timer_item, parent, false)
            return TimerViewHolder(view)
        }
    }

    fun bind(context: Context, actionModel: TaskActionModel, timerViewModel: TimerViewModel, ableToStart: () -> Boolean) {
        action.text = "${actionModel.order} - ${actionModel.action}"

        playButton.setOnClickListener {
            if(ableToStart())
                startStopStopwatch(actionModel, playButton, elapsedTime, timerViewModel)
            else
                Toast.makeText(context, ACTION_IS_NOT_AVAILABLE_TO_START, Toast.LENGTH_LONG).show()
        }

        resetButton.setOnClickListener {
            resetStopwatch(actionModel, playButton, elapsedTime, timerViewModel)
        }
    }

    private fun startStopStopwatch(
        action: TaskActionModel,
        button: ImageButton,
        elapsedTime: TextView,
        timerViewModel: TimerViewModel
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

    private fun resetStopwatch(
        action: TaskActionModel,
        button: ImageButton,
        elapsedTime: TextView,
        timerViewModel: TimerViewModel
    ) {
        action.resetStopwatch()
        elapsedTime.text = action.elapsedTime()
        button.setImageResource(R.drawable.ic_play)
        timerViewModel.setCurrentAction(action.id, action.elapsedTime())
    }

}