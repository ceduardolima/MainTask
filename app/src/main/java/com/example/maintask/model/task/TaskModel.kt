package com.example.maintask.model.task

import java.io.Serializable
import java.text.DateFormat
import java.time.LocalDate
import java.util.*

data class TaskModel(
    val title: String,
    val date: LocalDate,
    val status: Int,
    val isEmergency: Boolean,
    val author: String,
    val description: String,
    val tools: String,
    val actions: MutableList<TaskActionModel>
)
