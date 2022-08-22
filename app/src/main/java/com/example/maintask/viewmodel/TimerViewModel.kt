package com.example.maintask.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.repository.CreateAccountRepository
import com.example.maintask.model.task.TaskActionModel
import kotlinx.coroutines.Dispatchers.Main
import java.time.Duration

class TimerViewModel() : ViewModel() {
    private val actionModelList = mutableListOf<TaskActionModel>()
    private val _actionModel = MutableLiveData<List<TaskActionModel>>()
    val actionModel: LiveData<List<TaskActionModel>> = _actionModel

    private fun setActionModel(){
        _actionModel.value = actionModelList
    }

    fun setActionEntityList(actionEntityList: List<ActionEntity>) {
        for(action in actionEntityList){
            this.actionModelList.add(
                TaskActionModel(
                    action.action,
                    action.order
                )
            )
        }
        setActionModel()
    }

}