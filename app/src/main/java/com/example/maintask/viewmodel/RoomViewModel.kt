package com.example.maintask.viewmodel

import androidx.lifecycle.*
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.repository.ActionRepository
import com.example.maintask.model.repository.TaskActionRelationRepository
import com.example.maintask.model.repository.TaskRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RoomViewModel(
    private val taskRepository: TaskRepository,
    private val actionRepository: ActionRepository,
    private val taskActionRelationRepository: TaskActionRelationRepository
) : ViewModel() {
    val allTasks: LiveData<List<TaskEntity>> = taskRepository.taskList.asLiveData()
    val allActions: LiveData<List<ActionEntity>> = actionRepository.actionList.asLiveData()
    val allTasActionRelations: LiveData<List<TaskActionRelationEntity>> =
        taskActionRelationRepository.taskActionRelationList.asLiveData()

    fun getActionsByRelationList(relationList: List<TaskActionRelationEntity>): List<ActionEntity> =
        runBlocking {
                actionRepository.getActionByRelations(relationList)
        }

    suspend fun getTaskById(id: Int): TaskEntity {
        return taskRepository.getTaskById(id)
    }

    fun getRelationByTaskId(taskId: Int): List<TaskActionRelationEntity> {
        return runBlocking { taskActionRelationRepository.getRelationByTaskId(taskId) }
    }

    fun insertActionList(actionList: List<ActionEntity>) {
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

    fun deleteTaskTable() {
        viewModelScope.launch {
            taskRepository.deleteAll()
        }
    }

}

class RoomViewModelFactory(
    private val taskRepository: TaskRepository,
    private val actionRepository: ActionRepository,
    private val taskActionRelationRepository: TaskActionRelationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(
                taskRepository,
                actionRepository,
                taskActionRelationRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}