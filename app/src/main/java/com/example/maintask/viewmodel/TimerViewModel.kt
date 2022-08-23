package com.example.maintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.CurrentActionEntity
import com.example.maintask.model.task.TaskActionModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _actionList = MutableLiveData<List<TaskActionModel>>()
    val actionList: LiveData<List<TaskActionModel>> = _actionList

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    fun setActionList(actionList: List<CurrentActionEntity>){
        this._actionList.value = toActionModel(actionList)
    }

    private fun toActionModel(actionList: List<CurrentActionEntity>): List<TaskActionModel> {
        val actionModel = mutableListOf<TaskActionModel>()
        for (action in actionList){
            actionModel.add(
                TaskActionModel(
                    action.action,
                    action.order,
                )
            )
        }
        return actionModel
    }

    fun loadData(block: () -> Unit) {
        viewModelScope.launch {
            _progressBar.value = true
            delay(500)
            block()
            _progressBar.value = false
        }
    }

}