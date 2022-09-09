package com.example.maintask.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.maintask.model.converter.ActionEntityConverter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.task.TaskActionModel

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

    init {
        val roomApplication = (application as RoomApplication)
        roomViewModel = RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository
        ).create(RoomViewModel::class.java)
    }

    fun loadTask(fragmentActivity: FragmentActivity, taskId: Int) {
        val getTask = roomViewModel.getTaskById(taskId)
        getTask.observe(fragmentActivity) { task ->
            if (task != null) {
                _currentTask.value = task
                getTask.removeObservers(fragmentActivity)
            }
        }
    }

    fun loadActionList(
        fragmentActivity: FragmentActivity, taskId: Int) {
        val getActionsByTaskId = roomViewModel.getActionByTaskId(taskId)
        getActionsByTaskId.observe(fragmentActivity) { actionList ->
            if (actionList.isNotEmpty()) {
                this._actionList.value = ActionEntityConverter().toTaskActionModelList(actionList)
                getActionsByTaskId.removeObservers(fragmentActivity)
            }
        }
    }

    fun setTaskActionList(taskActionList: List<TaskActionModel>) {
        this._actionList.value = taskActionList
    }
}