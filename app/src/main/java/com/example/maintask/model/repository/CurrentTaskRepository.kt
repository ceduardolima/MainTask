package com.example.maintask.model.repository

import androidx.annotation.WorkerThread
import com.example.maintask.model.database.dao.CurrentTaskDao
import com.example.maintask.model.database.entity.CurrentTaskEntity
import kotlinx.coroutines.flow.Flow

class CurrentTaskRepository(private val currentTaskDao: CurrentTaskDao) {
    val taskList: Flow<List<CurrentTaskEntity>> = currentTaskDao.getAllTask()

    @WorkerThread
    suspend fun insert(task: CurrentTaskEntity) {
        currentTaskDao.insert(task)
    }

    suspend fun getTaskById(id: Int): CurrentTaskEntity {
        return currentTaskDao.getTaskById(id)
    }

    @WorkerThread
    suspend fun deleteAll() {
        currentTaskDao.deleteAll()
    }
}