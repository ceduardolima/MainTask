package com.example.maintask.viewmodel

import androidx.lifecycle.*
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    private val _buttonClick = MutableLiveData<Boolean>()
    val buttonClick: LiveData<Boolean>
        get() = _buttonClick

    private val _dataWasLoaded = MutableLiveData<Boolean>()
    val dataWasLoaded: LiveData<Boolean>
        get() = _dataWasLoaded

    private var _teamIsEmpty = true
    val teamIsEmpty: Boolean
        get() = _teamIsEmpty

    fun loadData(block: suspend () -> Unit) {
        viewModelScope.launch {
            _dataWasLoaded.value = false
            block()
            delay(500)
            _dataWasLoaded.value = true
        }
    }

    fun setButtonClick(click: Boolean) {
        this._buttonClick.value = click
    }

    fun setTeamIsEmpty(isEmpty: Boolean) {
        this._teamIsEmpty = isEmpty
    }
}