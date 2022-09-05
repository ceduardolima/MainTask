package com.example.maintask.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerCounterProcessViewModel(application: Application): AndroidViewModel(application) {
    private var roomViewModel: RoomViewModel

    private val _taskId = MutableLiveData<Int>()
    val taskId: LiveData<Int>
        get() = _taskId

    private val _currentTask = MutableLiveData<TaskEntity>()
    val currentTask: LiveData<TaskEntity>
        get() = _currentTask

    private var _actionList = MutableLiveData<List<ActionEntity>>()
    val actionList: LiveData<List<ActionEntity>>
        get() = _actionList

    private var _actionTitleList = MutableLiveData<List<String>>()
    val actionTitleList: LiveData<List<String>>
        get() = _actionTitleList

    private val _detailsFragmentButtonClick = MutableLiveData<Boolean>()
    val detailsFragmentButtonClick: LiveData<Boolean>
        get() = _detailsFragmentButtonClick

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar

    init {
        val roomApplication = (application as RoomApplication)
        roomViewModel = RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository
        ).create(RoomViewModel::class.java)
    }

    fun loadData(block: () -> Unit){
        viewModelScope.launch {
            _progressBar.value = false
            block()
            delay(500)
            _progressBar.value = true
        }
    }

    fun loadTaskAndActions(fragmentActivity: FragmentActivity, taskId: Int) {
        if(taskId != -1) {
            loadTask(fragmentActivity, taskId)
            loadActionList(fragmentActivity, taskId)
        }
    }

    private fun loadTask(fragmentActivity: FragmentActivity, taskId: Int) {
        roomViewModel.getTaskById(taskId).observe(fragmentActivity, Observer {
            task -> _currentTask.value = task
        })
    }

    private fun loadActionList(fragmentActivity: FragmentActivity, taskId: Int) {
        roomViewModel.getActionByTaskId(taskId)
            .observe(fragmentActivity, Observer { actionList ->
            this._actionList.value = actionList
            setActionTitleList(actionList)
        })
    }

    private fun setActionTitleList(actionList: List<ActionEntity>) {
        val stringList = mutableListOf<String>()
        actionList.forEach {
                action -> stringList.add(action.action)
        }
        _actionTitleList.value = stringList
    }

    fun observeIfWasDataLoaded(fragmentActivity: FragmentActivity, block: () -> Unit) {
        _progressBar.observe(fragmentActivity, Observer { wasLoaded ->
              if(wasLoaded) {
                  block()
              }
        })
    }

    fun observerDetailFragmentButtonClickAndNavigate(
        fragmentActivity: FragmentActivity,
        navigate: () -> Unit){
        _detailsFragmentButtonClick.observe(fragmentActivity, Observer { click ->
            if (click) {
                navigate()
                _detailsFragmentButtonClick.value = false
                _detailsFragmentButtonClick.removeObservers(fragmentActivity)
            }
        })
    }

    fun setButtonClick(wasClicked: Boolean) {
        _detailsFragmentButtonClick.value = wasClicked
    }

}