package com.example.maintask.model.database.application

import android.app.Application
import com.example.maintask.model.database.TaskRoomDatabase
import com.example.maintask.model.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RoomApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { TaskRoomDatabase.getDatabase(this, applicationScope)}
    val taskRepository by lazy { TaskRepository(database.taskDao())}
    val actionRepository by lazy { ActionRepository(database.actionDao())}
    val taskActionRepository by lazy { TaskActionRelationRepository(database.taskActionRelationDao())}
    val currentTaskRepository by lazy { CurrentTaskRepository(database.currentTaskDao()) }
    val currentActionRepository by lazy { CurrentActionRepository(database.currentActionDao()) }
}