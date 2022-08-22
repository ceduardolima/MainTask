package com.example.maintask.model.repository

import androidx.annotation.WorkerThread
import com.example.maintask.model.database.dao.CurrentActionDao
import com.example.maintask.model.database.entity.CurrentActionEntity
import kotlinx.coroutines.flow.Flow

class CurrentActionRepository(private val currentActionDao: CurrentActionDao) {
    val actionList: Flow<List<CurrentActionEntity>> = currentActionDao.getAllActions()

    @WorkerThread
    suspend fun insert(action: CurrentActionEntity) {
        currentActionDao.insert(action)
    }

    @WorkerThread
    suspend fun deleteAll(){
        currentActionDao.deleteAll()
    }
}