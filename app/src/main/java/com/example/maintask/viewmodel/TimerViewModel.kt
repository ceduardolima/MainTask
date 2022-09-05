package com.example.maintask.viewmodel


import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.task.TaskActionModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel() : ViewModel() {
    private val _actionList = MutableLiveData<List<TaskActionModel>>()
    val actionList: LiveData<List<TaskActionModel>> = _actionList

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    private val _completedActions = MutableLiveData<Boolean>()
    val completedActions: LiveData<Boolean>
        get() = _completedActions

    private val _currentAction = MutableLiveData<Pair<Int, String>>()
    val currentAction: LiveData<Pair<Int, String>>
        get() = _currentAction

    fun setActionList(actionList: List<ActionEntity>){
        this._actionList.value = toActionModel(actionList)
    }

    private fun toActionModel(actionList: List<ActionEntity>): List<TaskActionModel> {
        val actionModel = mutableListOf<TaskActionModel>()
        for (action in actionList){
            actionModel.add(
                TaskActionModel(
                    action.id,
                    action.action,
                    action.order,
                    action.elapsedTime
                )
            )
        }
        return actionModel
    }

    fun setCompletedActions(wasFinished: Boolean) {
        this._completedActions.value = wasFinished
    }

    fun setCurrentAction(id: Int, elapsedTime: String) {
        this._currentAction.value = Pair(id, elapsedTime)
    }

    fun loadData(block: () -> Unit) {
        viewModelScope.launch {
            _progressBar.value = true
            delay(500)
            block()
            _progressBar.value = false
        }
    }

    fun updateActionList(fragmentActivity: FragmentActivity) {
        currentAction.observe(fragmentActivity) { pair ->
            _actionList.value?.forEach { action ->
                if(action.id == pair.first)
                    action.time = pair.second
            }
        }
    }

}