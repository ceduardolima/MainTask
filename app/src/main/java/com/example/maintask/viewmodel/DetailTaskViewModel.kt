package com.example.maintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.CurrentTaskEntity
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailTaskViewModel: ViewModel() {
    private val _currentTask = MutableLiveData<CurrentTaskEntity>()
    val currentTask: LiveData<CurrentTaskEntity>
        get() = _currentTask

    private val _actionStringList = MutableLiveData<List<String>>()
    val actionStringList: LiveData<List<String>>
        get() = _actionStringList

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    fun setCurrentTask(currentTask: CurrentTaskEntity) {
        this._currentTask.value = currentTask
    }

    fun setActionStringList(actionStringList: List<String>) {
        this._actionStringList.value = actionStringList
    }


    fun loadData(block: () -> Unit){
        viewModelScope.launch {
            _progressBar.value = true
            delay(500)
            block()
            _progressBar.value = false
        }
    }

}