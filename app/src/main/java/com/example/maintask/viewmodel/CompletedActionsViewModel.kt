package com.example.maintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.TeamMemberEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompletedActionsViewModel : ViewModel() {
    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    private val _elapsedTime = MutableLiveData<Int>()
    val elapsedTime: LiveData<Int>
        get() = _elapsedTime

    private val _team = MutableLiveData<List<TeamMemberEntity>>()
    val team: LiveData<List<TeamMemberEntity>>
        get() = _team

    fun loadData(block: suspend () -> Unit) {
        viewModelScope.launch {
            _progressBar.value = false
            delay(500)
            block()
            _progressBar.value = true
        }
    }

    fun restartElapsedTime() {
        _elapsedTime.value = 0
    }

    fun addElapsedTime(elapsedTime: MutableList<Int>) {
        _elapsedTime.value = _elapsedTime.value?.plus(elapsedTime[2])
        _elapsedTime.value = _elapsedTime.value?.plus(elapsedTime[1] * 60)
        _elapsedTime.value = _elapsedTime.value?.plus(elapsedTime[0] * 3600)
    }

    fun getElapsedTime(): String{
        val seconds = (_elapsedTime.value?.rem(60))
        val minutes = _elapsedTime.value?.div(60)
        val hours = (minutes?.div(60))
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun setTeam(team: List<TeamMemberEntity>) {
        this._team.value = team
    }

    fun setEmptyTeam() {
        this._team.value = listOf()
    }
}