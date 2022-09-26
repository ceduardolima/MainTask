package com.example.maintask.model.repository

import android.app.TaskInfo
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.maintask.model.database.dao.TaskDao
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val taskList: Flow<List<TaskEntity>> = taskDao.getAllTask()

    @WorkerThread
    suspend fun insert(task: TaskEntity) {
        taskDao.insert(task)
    }

    @WorkerThread
    fun getTaskById(id: Int): Flow<TaskEntity> {
        return taskDao.getTaskById(id)
    }

    @WorkerThread
    suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }

    @WorkerThread
    suspend fun deleteAll() {
        taskDao.deleteAll()
    }
}