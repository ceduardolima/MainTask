package com.example.maintask.model.repository

import androidx.annotation.WorkerThread
import com.example.maintask.model.database.dao.ActionDao
import com.example.maintask.model.database.dao.TaskDao
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class ActionRepository(private val actionDao: ActionDao) {
    val actionList: Flow<List<ActionEntity>> = actionDao.getAllActions()

    @WorkerThread
    suspend fun insert(action: ActionEntity) {
        actionDao.insert(action)
    }

    @WorkerThread
    suspend fun deleteAll(){
        actionDao.deleteAll()
    }
}