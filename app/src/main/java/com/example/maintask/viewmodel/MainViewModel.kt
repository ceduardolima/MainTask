package com.example.maintask.viewmodel

import androidx.lifecycle.ViewModel
import com.example.maintask.model.database.entity.ActionEntity

class MainViewModel(): ViewModel() {
    private var _actionList = listOf<ActionEntity>()
    val actionList: List<ActionEntity>
        get() = _actionList

    private fun setActionList(actionList: List<ActionEntity>) {
        this._actionList = actionList
    }
}