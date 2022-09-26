package com.example.maintask.model.search

import com.example.maintask.model.task.TaskActionModel

class TaskActionSearch {

    fun searchById(actionList: List<TaskActionModel>, id: Int): Int {
        val list = mutableListOf<TaskActionModel>()
        list.addAll(actionList)
        val index = list.binarySearch { taskActionModel ->
            compareValues(taskActionModel.id, id)
        }
        return index
    }
}