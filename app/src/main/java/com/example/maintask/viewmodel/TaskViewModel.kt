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

    fun loadData(block: () -> Unit) {
        viewModelScope.launch {
            _loadDataStatus.value = false
            block()
            _loadDataStatus.value = true
        }
    }

    fun getTaskList(): MutableList<TaskModel> {
        // Simulando retorno do servidor
        return mutableListOf(
            TaskModel(
                "Titulo da O.S",
                LocalDate.parse("2022-08-19"),
                status = TaskModel.LATE,
                isEmergency = true,
                "Carlos Eduardo",
                "Descrição detalhada da O.S.",
                "Lista de ferramentas que serão usadas na O.S",
                generateActionList()
            )
        )
    }

    private fun generateActionList(): MutableList<TaskActionModel> {
        val mutableList: MutableList<TaskActionModel> = mutableListOf()
        val t = arrayOf(0, 1, 2, 3)
        for (n in 0..2) {
            val action = TaskActionModel(
                n + 1,
                "Acao $n",
                t[n]
            )
            mutableList.add(action)
        }
        return mutableList
    }

    fun getTaskEntity(taskModelList: List<TaskModel>): MutableList<TaskEntity> {
        val taskEntityList = mutableListOf<TaskEntity>()
        for (task in taskModelList) {
            val taskEntity = TaskEntity(
                1,
                title = task.title,
                author = task.author,
                date = task.date.toString(),
                status = TaskModel.LATE,
                description = task.description,
                tools = task.tools,
                isEmergency = task.isEmergency
            )
            taskEntityList.add(taskEntity)
        }
        return taskEntityList
    }


    fun getActionEntity(actions: List<TaskActionModel>): MutableList<ActionEntity> {
        val actionsEntityList = mutableListOf<ActionEntity>()
        for (action in actions) {
            val actionEntity = ActionEntity(
                action.action,
                action.order,
                action.time,
                id = action.id,
                executor = "${action.id}"
            )
            actionsEntityList.add(actionEntity)
        }
        return actionsEntityList
    }

    fun getTaskActionRelationEntity(
        taskEntityList: List<TaskEntity>,
        actionsEntityList: MutableList<ActionEntity>
    ): MutableList<TaskActionRelationEntity> {
        val relationList = mutableListOf<TaskActionRelationEntity>()
        for (task in taskEntityList) {
            for (action in actionsEntityList) {
                val relationEntity = TaskActionRelationEntity(
                    task.id,
                    action.id,
                    action.id
                )
                relationList.add(relationEntity)
            }
        }
        return relationList
    }
}