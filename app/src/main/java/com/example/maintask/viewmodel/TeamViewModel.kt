package com.example.maintask.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.EmployeeEntity
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.search.TeamMemberSearch
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TeamViewModel(application: Application) : AndroidViewModel(application) {
    private var roomViewModel: RoomViewModel

    private var _team = MutableLiveData<List<TeamMemberEntity>>()
    val team: LiveData<List<TeamMemberEntity>>
        get() = _team

    private val _dataWasLoaded = MutableLiveData<Boolean>()
    val dataWasLoaded: LiveData<Boolean>
        get() = _dataWasLoaded

    private val _currentEmployee = MutableLiveData<EmployeeEntity?>()

    companion object {
        const val DOES_NOT_EXIST = -1
    }

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

    fun loadData() {
        viewModelScope.launch {
            _dataWasLoaded.value = false
            _team = roomViewModel.team as MutableLiveData<List<TeamMemberEntity>>
            _dataWasLoaded.value = true
        }
    }

    fun searchEmployeeById(id: Int) {
        viewModelScope.launch {
            val employee = roomViewModel.getEmployeeById(id)
            _currentEmployee.value = employee
        }
    }

    fun observerAddNewTeamMember(
        fragmentActivity: FragmentActivity,
        verifyEmployee: (employee: EmployeeEntity?) -> Boolean) {
        _currentEmployee.observe(fragmentActivity, Observer { employee ->
            val success = verifyEmployee(employee)
            if (success) {
                val (id, name, photoPath) = employee!!
                val teamMember = TeamMemberEntity(id, name, photoPath)
                insertNewMemberOnTeamMemberTable(teamMember)
            }
        })
    }

    private fun insertNewMemberOnTeamMemberTable(newTeamMemberEntity: TeamMemberEntity) {
        roomViewModel.insertTeamMember(newTeamMemberEntity)
    }

    fun isDuplicated(id: Int): Boolean {
        val index = getTeamMemberIndexById(id)
        return (index != DOES_NOT_EXIST)
    }

    fun deleteTeamMemberById(id: Int) {
        if (isTeamNull()) return
        val index = getTeamMemberIndexById(id)
        if (index >= 0) {
            val teamMember = _team.value!!.toMutableList()
            val member = teamMember.removeAt(index)
            _team.value = teamMember
            roomViewModel.deleteTeamMemberById(member.id)
        }
    }

    private fun getTeamMemberIndexById(id: Int): Int {
        val teamMember = _team.value!!.toMutableList()
        return TeamMemberSearch().searchById(teamMember, id)
    }

    private fun isTeamNull() = (_team.value == null)
}