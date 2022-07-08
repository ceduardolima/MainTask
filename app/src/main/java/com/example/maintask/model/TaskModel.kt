package com.example.maintask.model

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
    val material: String,
    val actions: String
)
