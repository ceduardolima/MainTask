package com.example.maintask.model.repository

import androidx.annotation.WorkerThread
import com.example.maintask.model.database.dao.TaskDao
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val taskList: Flow<List<TaskEntity>> = taskDao.getAllTask()

    @WorkerThread
    suspend fun insert(task: TaskEntity) {
        taskDao.insert(task)
    }

    suspend fun getTaskById(id: Int): TaskEntity {
        return taskDao.getTaskById(id)
    }

    @WorkerThread
    suspend fun deleteAll() {
        taskDao.deleteAll()
    }
}