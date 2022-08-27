package com.example.maintask.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maintask.model.database.entity.CurrentActionEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompletedActionsViewModel : ViewModel() {
    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    private val _actions = MutableLiveData<List<CurrentActionEntity>>()
    val actions: LiveData<List<CurrentActionEntity>>
        get() = _actions

    private val _elapsedTime = MutableLiveData<Int>()
    val elapsedTime: LiveData<Int>
        get() = _elapsedTime

    init {
        _elapsedTime.value = 0
    }

    fun loadDatabase(block: suspend () -> Unit) {
        viewModelScope.launch {
            _progressBar.value = false
            delay(500)
            block()
            _progressBar.value = true
        }
    }

    fun setActions(actions: List<CurrentActionEntity>) {
        this._actions.value = actions
    }

    fun addElapsedTime(elapsedTime: MutableList<Int>) {
        _elapsedTime.value = _elapsedTime.value?.plus(elapsedTime[2])
        _elapsedTime.value = _elapsedTime.value?.plus(elapsedTime[1] * 60)
        _elapsedTime.value = _elapsedTime.value?.plus(elapsedTime[0] * 3600)
        Log.i("tempo", "${this.elapsedTime}")
    }

    fun getElapsedTime(): String{
        val seconds = (_elapsedTime.value?.rem(60))
        val minutes = _elapsedTime.value?.div(60)
        val hours = (minutes?.div(60))
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun resetElapsedTime() {
        _elapsedTime.value = 0
    }

}