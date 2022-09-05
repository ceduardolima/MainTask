package com.example.maintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailTaskViewModel: ViewModel() {
    private val scope = viewModelScope
    private val _currentTask = MutableLiveData<TaskEntity>()
    val currentTask: LiveData<TaskEntity>
        get() = _currentTask

    private val _actionStringList = MutableLiveData<List<String>>()
    val actionStringList: LiveData<List<String>>
        get() = _actionStringList

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    private var _actionList = mutableListOf<ActionEntity>()
    val actionList: List<ActionEntity>
        get() = _actionList

    fun setActionList(actionList: List<ActionEntity>) {
        this._actionList = actionList.toMutableList()
    }

    fun setActionStringList(actionStringList: List<String>) {
        this._actionStringList.value = actionStringList
    }

    fun loadData(block: () -> Unit){
        scope.launch {
            _progressBar.value = true
            block()
            delay(500)
            _progressBar.value = false
        }
    }

    fun findAndSetTask(taskId: Int, taskList: List<TaskEntity>) {
        for(task in taskList)
            if(task.id == taskId)
                setCurrentTask(task)
    }

    private fun setCurrentTask(currentTask: TaskEntity) {
        this._currentTask.value = currentTask
    }

}