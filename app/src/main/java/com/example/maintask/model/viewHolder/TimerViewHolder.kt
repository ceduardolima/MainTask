package com.example.maintask.model.viewHolder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.time.ElapsedTimeHelper
import com.example.maintask.viewmodel.TimerViewModel

class TimerViewHolder(
    timerRecyclerView: View,
    private val timerViewModel: TimerViewModel
) : RecyclerView.ViewHolder(timerRecyclerView) {
    val action: TextView = timerRecyclerView.findViewById(R.id.timer_item_action)
    val elapsedTime: TextView = timerRecyclerView.findViewById(R.id.timer_item_elapsedTime)
    val playButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_play_button)
    val resetButton: ImageButton = timerRecyclerView.findViewById(R.id.timer_item_reset_button)
    private val selectResponsibleButton: Button = timerRecyclerView.findViewById(R.id.timer_item_responsible)

    companion object {
        fun create(parent: ViewGroup, timerViewModel: TimerViewModel): TimerViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.timer_item, parent, false)
            return TimerViewHolder(view, timerViewModel)
        }
    }

    @SuppressLint("SetTextI18n")
    fun setText(action: TaskActionModel){
        this.action.text = "${action.order} - ${action.action}"
        this.elapsedTime.text = action.time
        if (action.worker != null){
            this.selectResponsibleButton.text = "id: %d".format(action.worker!!.id)
        }

    }

    fun selectResponsible(
        context: Context,
        currentAction: TaskActionModel,
        team: List<TeamMemberEntity>) {
        selectResponsibleButton.setOnClickListener {
            val workerIsNull = currentAction.worker == null
            val workTimeIsZero = currentAction.worker!!.workTime.compareTo(0) == 0
            if (workerIsNull || workTimeIsZero)
                createDialog(context, currentAction, team)
            else
                Toast.makeText(
                    context,
                    "Não foi permitido mudar o Executor.",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun createDialog(
        context: Context,
        currentAction: TaskActionModel,
        team: List<TeamMemberEntity>) {
        val builder = AlertDialog.Builder(context)
        val teamMembersId = getTeamMembersId(team)
        builder.setTitle("Selecione o responsável")
        builder.setCancelable(false)
        builder.setSingleChoiceItems(teamMembersId.toTypedArray(), -1) {
                _, i -> timerViewModel.setWorkerInAction(currentAction.id, team[i].copy())
        }
        builder.setPositiveButton("Ok") { _, i ->
            if (i != -1)
                selectResponsibleButton.text = "id: %d".format(teamMembersId[i])
        }
        builder.show()
    }

    private fun getTeamMembersId(team: List<TeamMemberEntity>): List<String> {
        val teamMembersId = mutableListOf<String>()
        for (teamMember in team)
            teamMembersId.add(teamMember.id.toString())
        return teamMembersId
    }

    fun startStop(actionModel: TaskActionModel) {
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
            timerViewModel.updateWorkTime(action.id,
                ElapsedTimeHelper(elapsedTime.text.toString()).getElapsedTimeInSeconds())
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
        if (action.worker != null)
            timerViewModel.resetWorkTime(action.id)
    }
}