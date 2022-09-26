package com.example.maintask.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.repository.TeamMemberRepository
import com.example.maintask.model.search.TaskActionSearch
import com.example.maintask.model.search.TeamMemberSearch
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.time.ElapsedTimeHelper
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

    private val _team = MutableLiveData<List<TeamMemberEntity>>()
    val team: LiveData<List<TeamMemberEntity>>
        get() = _team

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
        if (index > -1) {
            val searchedTaskAction = taskActionList[index]
            searchedTaskAction.time = time
            taskActionList[index] = searchedTaskAction
            _taskActionList.value = taskActionList
        }
    }

    fun setTeam(team: List<TeamMemberEntity>) {
        _team.value = team
    }

    fun getTeamList(): List<TeamMemberEntity> {
        return if (_team.value != null) _team.value!!
        else emptyList()
    }

    fun resetWorkTime(actionId: Int) {
        updateWorkTime(actionId, 0)
    }

    fun updateWorkTime(actionId: Int, workTime: Long) {
        try {
            val actionList = _taskActionList.value!!.toMutableList()
            val index = TaskActionSearch().searchById(actionList.toList(), actionId)
            actionList[index].worker?.workTime = workTime
        }
        catch (e: IndexOutOfBoundsException) {
            Log.e("error", "TimerViewModel - updateWorkTime: Index doesnt exist ")
        }
    }

    fun setWorkerInAction(actionId: Int, teamMemberEntity: TeamMemberEntity) {
        val actionList = _taskActionList.value!!.toMutableList()
        val index = TaskActionSearch().searchById(actionList, actionId)
        if (index > -1) {
            val searchedTaskAction = actionList[index]
            searchedTaskAction.worker = teamMemberEntity
            actionList[index] = searchedTaskAction
            _taskActionList.value = actionList
        }
    }

    fun setEmptyActionList() {
        this._taskActionList.value = emptyList()
    }
}