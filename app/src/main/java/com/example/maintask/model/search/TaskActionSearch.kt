package com.example.maintask.model.search

import com.example.maintask.model.task.TaskActionModel

class TaskActionSearch {

    fun searchById(actionList: List<TaskActionModel>, id: Int): Int {
        val index = actionList.binarySearch { taskActionModel ->
            compareValues(taskActionModel.id, id)
        }
        return index
    }
}