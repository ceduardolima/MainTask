package com.example.maintask.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.task.TaskModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private var roomViewModel: RoomViewModel

    private var _taskId: Int = -1
    val taskId: Int
        get() = _taskId

    private val _buttonClick = MutableLiveData<Boolean>()

    private val _loadDataStatus = MutableLiveData<Boolean>()
    val loadDataStatus: LiveData<Boolean>
        get() = _loadDataStatus

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

    fun observerTaskListAndCreateRecyclerView(
        fragmentActivity: FragmentActivity,
        createRecyclerView: (taskList: List<TaskEntity>) -> Unit) {
        roomViewModel.allTasks.observe(fragmentActivity, Observer {
            taskList -> createRecyclerView(taskList)
        })
    }

    fun observerButtonClickAndNavigate(fragmentActivity: FragmentActivity, navigate: (id: Int) -> Unit) {
        _buttonClick.observe(fragmentActivity, Observer { click ->
            if (click && (taskId != -1)) {
                navigate(taskId)
            }
        })
    }

    fun setTaskId(taskId: Int) {
        this._taskId = taskId
    }

    fun setButtonClick(click: Boolean) {
        this._buttonClick.value = click
    }
}