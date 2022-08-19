package com.example.maintask.model.task

import java.time.LocalDate


data class TaskModel(
    val title: String,
    val date: LocalDate,
    val status: Int,
    val isEmergency: Boolean,
    val author: String,
    val description: String,
    val tools: String,
    val actions: MutableList<TaskActionModel>
){
    companion object {
        const val LATE = 0
        const val FINISHED = 1
    }
}
