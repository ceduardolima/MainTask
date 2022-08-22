package com.example.maintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.maintask.model.database.entity.TaskEntity
import com.google.android.gms.tasks.Task

class DetailTaskViewModel: ViewModel() {
    private val _currentTask = MutableLiveData<TaskEntity>()
    val currentTask: LiveData<TaskEntity> = _currentTask
    private val _actionStringList = MutableLiveData<List<String>>()
    val actionStringList: LiveData<List<String>> = _actionStringList
    private val _actionIdList = MutableLiveData<List<Int>>()
    val actionIdList: LiveData<List<Int>> = _actionIdList

    fun setCurrentTask(taskEntity: TaskEntity) {
        this._currentTask.value = taskEntity
    }

    fun setActionStringList(actionStringList: List<String>) {
        this._actionStringList.value = actionStringList
    }

    fun setActionIdList(actionIdList: List<Int>) {
        this._actionIdList.value = actionIdList
    }

}