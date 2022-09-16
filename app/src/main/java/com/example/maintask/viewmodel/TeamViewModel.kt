package com.example.maintask.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.maintask.model.database.application.RoomApplication
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

    companion object {
        const val SUCCESS = 1
        const val DONT_EXIST = -1
        const val IS_NULL = -2
        const val DUPLICATED = -3
    }

    init {
        val roomApplication = (application as RoomApplication)
        roomViewModel = RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository,
            roomApplication.teamMemberRepository
        ).create(RoomViewModel::class.java)
    }

    fun loadData() {
        viewModelScope.launch {
            _dataWasLoaded.value = false
            _team = roomViewModel.team as MutableLiveData<List<TeamMemberEntity>>
            _dataWasLoaded.value = true
        }
    }

    fun setNewTeamMember(teamMember: TeamMemberEntity): Int =
        when {
            isTeamNull() -> {
                val list = listOf(teamMember)
                _team.value = list
                insertNewMemberOnDatabase(teamMember)
                SUCCESS
            }
            isDuplicated(teamMember.id) -> DUPLICATED
            else -> {
                val list = _team.value!!.toMutableList()
                list.add(teamMember)
                _team.value = list
                insertNewMemberOnDatabase(teamMember)
                SUCCESS
            }
        }

    private fun insertNewMemberOnDatabase(newTeamMemberEntity: TeamMemberEntity) {
        roomViewModel.insertTeamMember(newTeamMemberEntity)
    }

    private fun isDuplicated(id: Int): Boolean {
        val index = getTeamMemberIndexById(id)
        return (index != DONT_EXIST)
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