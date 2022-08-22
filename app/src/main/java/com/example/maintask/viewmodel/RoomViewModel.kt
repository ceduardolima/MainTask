package com.example.maintask.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.maintask.model.database.entity.*
import com.example.maintask.model.repository.*
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RoomViewModel(
    private val taskRepository: TaskRepository,
    private val actionRepository: ActionRepository,
    private val taskActionRelationRepository: TaskActionRelationRepository,
    private val currentTaskRepository: CurrentTaskRepository,
    private val currentActionRepository: CurrentActionRepository
) : ViewModel() {
    val allTasks: LiveData<List<TaskEntity>> = taskRepository.taskList.asLiveData()
    val allActions: LiveData<List<ActionEntity>> = actionRepository.actionList.asLiveData()
    val allTasActionRelations: LiveData<List<TaskActionRelationEntity>> =
        taskActionRelationRepository.taskActionRelationList.asLiveData()
    val currentTask: LiveData<List<CurrentTaskEntity>> = currentTaskRepository.taskList.asLiveData()
    val currentAction: LiveData<List<CurrentActionEntity>> =
        currentActionRepository.actionList.asLiveData()

    fun setCurrentTask(task: TaskEntity) {
        viewModelScope.launch {
            val currentTask = toCurrentTask(task)
            currentTaskRepository.deleteAll()
            currentTaskRepository.insert(currentTask)
        }
    }

    private fun toCurrentTask(task: TaskEntity): CurrentTaskEntity =
        CurrentTaskEntity(
            task.id, task.title, task.date, task.status, task.isEmergency, task.author,
            task.description, task.tools
        )

    fun setCurrentAction(actionList: List<ActionEntity>){
        viewModelScope.launch {
            val currentActionList = toCurrentActionList(actionList)
            currentActionRepository.deleteAll()
            for (action in currentActionList)
                currentActionRepository.insert(action)
        }
    }

    fun toCurrentActionList(actionList: List<ActionEntity>): List<CurrentActionEntity> {
        val currentActionList = mutableListOf<CurrentActionEntity>()
        for (action in actionList)
            currentActionList.add(
                CurrentActionEntity(
                    action.action,
                    action.order,
                    action.elapsedTime,
                    action.id
                )
            )
        return currentActionList
    }

    fun insertActionList(actionList: List<ActionEntity>) {
        Log.i("teste", "${actionList.size}")
        for (action in actionList)
            insertAction(action)
    }

    fun insertRelationList(relationList: List<TaskActionRelationEntity>) {
        for (relation in relationList)
            insertTaskActionRelation(relation)
    }

    fun insertTaskList(taskList: List<TaskEntity>) {
        for (task in taskList) {
            insertTask(task)
        }
    }

    private fun insertTask(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insert(taskEntity)
    }

    private fun insertAction(actionEntity: ActionEntity) = viewModelScope.launch {
        actionRepository.insert(actionEntity)
    }

    private fun insertTaskActionRelation(taskActionRelationEntity: TaskActionRelationEntity) =
        viewModelScope.launch {
            taskActionRelationRepository.insert(taskActionRelationEntity)
        }

    fun deleteAll() {
        viewModelScope.launch {
            taskRepository.deleteAll()
            actionRepository.deleteAll()
            taskActionRelationRepository.deleteAll()
        }
    }

    fun populateDatabase(
        task: List<TaskEntity>,
        action: List<ActionEntity>,
        relation: List<TaskActionRelationEntity>
    ) {
        viewModelScope.launch {
            taskRepository.deleteAll()
            actionRepository.deleteAll()
            taskActionRelationRepository.deleteAll()

            insertTaskList(task)
            insertActionList(action)
            insertRelationList(relation)
        }
    }
}

class RoomViewModelFactory(
    private val taskRepository: TaskRepository,
    private val actionRepository: ActionRepository,
    private val taskActionRelationRepository: TaskActionRelationRepository,
    private val currentTaskRepository: CurrentTaskRepository,
    private val currentActionRepository: CurrentActionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(
                taskRepository,
                actionRepository,
                taskActionRelationRepository,
                currentTaskRepository,
                currentActionRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}