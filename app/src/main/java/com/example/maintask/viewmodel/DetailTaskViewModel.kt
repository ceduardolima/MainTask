package com.example.maintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.CurrentTaskEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailTaskViewModel: ViewModel() {
    private val scope = viewModelScope
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
        scope.launch {
            _progressBar.value = true
            block()
            delay(500)
            _progressBar.value = false
        }
    }

}