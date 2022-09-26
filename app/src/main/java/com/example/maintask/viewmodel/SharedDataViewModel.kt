package com.example.maintask.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.maintask.model.converter.ActionEntityConverter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.task.TaskActionModel
import kotlinx.coroutines.launch

class SharedDataViewModel(application: Application) : AndroidViewModel(application) {
    private var roomViewModel: RoomViewModel

    private val _taskId = MutableLiveData<Int>()
    val taskId: LiveData<Int>
        get() = _taskId

    private val _currentTask = MutableLiveData<TaskEntity>()
    val currentTask: LiveData<TaskEntity>
        get() = _currentTask

    private var _actionList = MutableLiveData<List<TaskActionModel>>()
    val actionList: LiveData<List<TaskActionModel>>
        get() = _actionList

    private var _taskWasUpdated = MutableLiveData<Boolean>()
    val taskWasUpdated: LiveData<Boolean>
        get() = _taskWasUpdated

    init {
        val roomApplication = (application as RoomApplication)
        roomViewModel = RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository,
            roomApplication.teamMemberRepository,
            roomApplication.employeeRepository
        ).create(RoomViewModel::class.java)
    }

    fun loadTask(fragmentActivity: FragmentActivity, taskId: Int) {
        val getTask = roomViewModel.getTaskById(taskId)
        getTask.observe(fragmentActivity, Observer { task ->
            if (task != null) {
                _currentTask.value = task
                getTask.removeObservers(fragmentActivity)
            }
        })
    }

    fun loadActionList(
        fragmentActivity: FragmentActivity, taskId: Int) {
        val getActionsByTaskId = roomViewModel.getActionByTaskId(taskId)
        getActionsByTaskId.observe(fragmentActivity, Observer { actionList ->
            if (actionList.isNotEmpty()) {
                this._actionList.value = ActionEntityConverter().toTaskActionModelList(actionList)
                getActionsByTaskId.removeObservers(fragmentActivity)
            }
        })
    }

    fun setTaskActionList(taskActionList: List<TaskActionModel>) {
        this._actionList.value = taskActionList
    }

    fun loadTeam(fragmentActivity: FragmentActivity, block: (List<TeamMemberEntity>) -> Unit) {
        val getTeam = roomViewModel.team
        getTeam.observe(fragmentActivity, Observer { team ->
            if (!team.isNullOrEmpty()) {
                block(team)
            }
        })
    }

    fun setTaskAsDone() {
        viewModelScope.launch {
            _taskWasUpdated.value = false
            updateTask()
            updateActions()
            _taskWasUpdated.value = true
        }
    }

    private fun updateTask() {
        val currentTask = _currentTask.value!!
        val (id, title, date, _, isEmergency, author, descriptions, tools) = currentTask
        val completedTask = TaskEntity(id, title, date, 1, isEmergency, author, descriptions, tools)
        roomViewModel.setTaskAsComplete(completedTask)
    }

    private fun updateActions() {
        val actionList = _actionList.value!!
        for (action in actionList)
            roomViewModel.updateElapsedTime(action.id, action.elapsedTime())
    }

    fun setTaskWasUpdated(wasUpdated: Boolean) {
        this._taskWasUpdated.value = wasUpdated
    }
}