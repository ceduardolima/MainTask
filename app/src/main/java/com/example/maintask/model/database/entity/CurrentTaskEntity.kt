package com.example.maintask.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_task")
data class CurrentTaskEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String,
    val date: String,
    val status: Int,
    @ColumnInfo(name = "is_emergency") val isEmergency: Boolean,
    val author: String,
    val description: String,
    val tools: String
)