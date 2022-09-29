package com.example.maintask.viewmodel

import androidx.lifecycle.*
import com.example.maintask.model.database.entity.*
import com.example.maintask.model.repository.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoomViewModel(
    private val taskRepository: TaskRepository,
    private val actionRepository: ActionRepository,
    private val taskActionRelationRepository: TaskActionRelationRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val employeeRepository: EmployeeRepository,
) : ViewModel() {
    val allTasks: LiveData<List<TaskEntity>> = taskRepository.taskList.asLiveData()
    val allActions: LiveData<List<ActionEntity>> = actionRepository.actionList.asLiveData()
    val team: LiveData<List<TeamMemberEntity>> = teamMemberRepository.team.asLiveData()
    val employees: LiveData<List<EmployeeEntity>> = employeeRepository.employees.asLiveData()

    fun insertEmployee(employeeEntity: EmployeeEntity) {
        viewModelScope.launch {
            employeeRepository.insert(employeeEntity)
        }
    }

    fun setTaskAsComplete(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.update(task)
        }
    }

    fun insertEmployeeList(employees: List<EmployeeEntity>) {
        for(employee in employees)
            insertEmployee(employee)
    }

    suspend fun getEmployeeById(id: Int): EmployeeEntity {
        return employeeRepository.getEmployeeById(id).first()
    }

    fun insertTeam(team: List<TeamMemberEntity>) {
        for (teamMember in team) {
            insertTeamMember(teamMember)
        }
    }

    fun insertTeamMember(teamMemberEntity: TeamMemberEntity) {
        viewModelScope.launch {
            teamMemberRepository.insert(teamMemberEntity)
        }
    }

    fun deleteTeamMemberTable(teamMemberEntity: TeamMemberEntity) {
        viewModelScope.launch {
            teamMemberRepository.deleteAll()
        }
    }

    fun deleteTeamMemberById(id: Int) {
        viewModelScope.launch {
            teamMemberRepository.deleteMemberById(id)
        }
    }

    fun getTeamMemberById(id: Int): LiveData<TeamMemberEntity> {
        return teamMemberRepository.getMemberById(id).asLiveData()
    }

    fun setWorkTime(id: Int, elapseTime: Long) {
        viewModelScope.launch {
            teamMemberRepository.setWorkTime(id, elapseTime)
        }
    }

    fun getActionByTaskId(taskId: Int): LiveData<List<ActionEntity>> {
        return actionRepository.getActionByTaskId(taskId).asLiveData()
    }

    fun getTaskById(taskId: Int): LiveData<TaskEntity> {
        return taskRepository.getTaskById(taskId).asLiveData()
    }

    fun resetElapsedTime(actionList: List<ActionEntity>) {
        viewModelScope.launch {
            for(action in actionList)
                actionRepository.updateElapsedTime(action.id, "00:00:00")
        }
    }

    fun updateElapsedTime(id: Int, elapseTime: String) {
        viewModelScope.launch {
            actionRepository.updateElapsedTime(id, elapseTime)
        }
    }

    fun insertActionList(actionList: List<ActionEntity>) {
        for (action in actionList)
            insertAction(action)
    }

    fun insertRelationList(relationList: List<TaskActionRelationEntity>) {
        for (relation in relationList)
            insertTaskActionRelation(relation)
    }

    fun insertTaskList(taskList: List<TaskEntity>) {
        for (task in taskList) {
            insertTask(task)
        }
    }

    fun insertTask(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insert(taskEntity)
    }

    private fun insertAction(actionEntity: ActionEntity) = viewModelScope.launch {
        actionRepository.insert(actionEntity)
    }

    private fun insertTaskActionRelation(taskActionRelationEntity: TaskActionRelationEntity) =
        viewModelScope.launch {
            taskActionRelationRepository.insert(taskActionRelationEntity)
        }

    fun deleteAll() {
        viewModelScope.launch {
            taskRepository.deleteAll()
            actionRepository.deleteAll()
            taskActionRelationRepository.deleteAll()
        }
    }
}

class RoomViewModelFactory(
    private val taskRepository: TaskRepository,
    private val actionRepository: ActionRepository,
    private val taskActionRelationRepository: TaskActionRelationRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val employeeRepository: EmployeeRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(
                taskRepository,
                actionRepository,
                taskActionRelationRepository,
                teamMemberRepository,
                employeeRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}