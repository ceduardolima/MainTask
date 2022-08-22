package com.example.maintask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.task.TaskModel
import java.time.LocalDate

class TaskViewModel() : ViewModel() {

    private val _taskId = MutableLiveData<Int?>()
    val taskId: LiveData<Int?>
        get() = _taskId

    private val _actionIdList = MutableLiveData<List<Int>>()
    val actionIdList: LiveData<List<Int>>
        get() = _actionIdList

    private val _buttonClick = MutableLiveData<Boolean>()
    val buttonClick: LiveData<Boolean>
        get() = _buttonClick

    fun setTaskId(taskId: Int){
        this._taskId.value = taskId
    }

    fun setActionIdList(actionIdList: List<Int>) {
        this._actionIdList.value = actionIdList
    }

    fun setButtonClick(click: Boolean){
        this._buttonClick.value = click
    }

    fun getTaskList(): MutableList<TaskModel> {
        // Simulando retorno do servidor
        return mutableListOf(
            TaskModel(
                "Trocar o pneu do carro",
                LocalDate.parse("2022-08-19"),
                status = TaskModel.LATE,
                isEmergency = true,
                "Carlos Eduardo",
                "O carro passou por um buraco e furou o pneu. Há urgência nessa solicitação pois precisamos do quanto antes que ele esteja em boas condições para que possamos executar as tarefas do dia",
                "Estepe, macaco, chave L",
                generateActionList()
            )
        )
    }

    private fun generateActionList(): MutableList<TaskActionModel> {
        val mutableList: MutableList<TaskActionModel> = mutableListOf()
        for (n in 0..11) {
            val action = TaskActionModel(
                "Acao $n",
                n
            )
            mutableList.add(action)
        }
        return mutableList
    }

    fun getTaskEntity(taskModelList: List<TaskModel>): MutableList<TaskEntity> {
        val taskEntityList = mutableListOf<TaskEntity>()
        for (task in taskModelList) {
            val taskEntity = TaskEntity(
                1,
                title = task.title,
                author = task.author,
                date = task.date.toString(),
                status = TaskModel.LATE,
                description = task.description,
                tools = task.tools,
                isEmergency = task.isEmergency
            )
            taskEntityList.add(taskEntity)
        }
        return taskEntityList
    }


    fun getActionEntity(actions: List<TaskActionModel>): MutableList<ActionEntity> {
        val actionsEntityList = mutableListOf<ActionEntity>()
        for (action in actions) {
            val actionEntity = ActionEntity(
                action.action,
                action.order,
                action.elapsedTime(),
                action.order
            )
            actionsEntityList.add(actionEntity)
        }
        return actionsEntityList
    }

    fun getTaskActionRelationEntity(
        taskEntityList: List<TaskEntity>,
        actionsEntityList: MutableList<ActionEntity>
    ): MutableList<TaskActionRelationEntity> {
        val relationList = mutableListOf<TaskActionRelationEntity>()
        for (task in taskEntityList) {
            for (action in actionsEntityList) {
                val relationEntity = TaskActionRelationEntity(
                    task.id,
                    action.id,
                    action.id
                )
                relationList.add(relationEntity)
            }
        }
        return relationList
    }
}