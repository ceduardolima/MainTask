package com.example.maintask.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.search.TaskActionSearch
import com.example.maintask.model.task.TaskActionModel
import kotlinx.coroutines.launch

class TimerViewModel: ViewModel() {
    private val _taskActionList = MutableLiveData<List<TaskActionModel>>()
    val taskActionList: LiveData<List<TaskActionModel>> = _taskActionList

    private val _dataWasLoaded = MutableLiveData<Boolean>()
    val dataWasLoaded: LiveData<Boolean>
        get() = _dataWasLoaded

    private val _completedActions = MutableLiveData<Boolean>()
    val completedActions: LiveData<Boolean>
        get() = _completedActions

    fun loadData(block: suspend () -> Unit) {
        viewModelScope.launch {
            _dataWasLoaded.value = false
            block()
            _dataWasLoaded.value = true
        }
    }

    fun hasFinishedAllActions(hasFinished: Boolean) {
        this._completedActions.value = hasFinished
    }

    fun setTaskActionList(taskActionList: List<TaskActionModel>) {
        this._taskActionList.value = taskActionList
    }

    fun updateTaskAction(id: Int, time: String) {
        val taskActionList = _taskActionList.value!!.toMutableList()
        val index = TaskActionSearch().searchById(taskActionList, id)
        if (index > 0) {
            val searchedTaskAction = taskActionList[index]
            searchedTaskAction.time = time
            taskActionList[index] = searchedTaskAction
            _taskActionList.value = taskActionList
        }
    }
}