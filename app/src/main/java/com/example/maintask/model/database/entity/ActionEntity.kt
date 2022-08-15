package com.example.maintask.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "action")
data class ActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val action: String,
    val order: Int,
    @ColumnInfo(name = "elapsed_time") val elapsedTime: String
)
