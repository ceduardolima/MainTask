package com.example.maintask.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_action")
data class CurrentActionEntity(
    val action: String,
    val order: Int,
    @ColumnInfo(name = "elapsed_time") val elapsedTime: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)