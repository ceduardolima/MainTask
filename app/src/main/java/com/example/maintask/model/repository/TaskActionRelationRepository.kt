package com.example.maintask.model.repository

import androidx.annotation.WorkerThread
import com.example.maintask.model.database.dao.TaskActionRelationDao
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import kotlinx.coroutines.flow.Flow

class TaskActionRelationRepository(private val taskActionDao: TaskActionRelationDao) {
    val taskActionRelationList: Flow<List<TaskActionRelationEntity>> = taskActionDao.getAllRelations()

    @WorkerThread
    suspend fun insert(taskActionRelation: TaskActionRelationEntity) {
        taskActionDao.insert(taskActionRelation)
    }

    @WorkerThread
    suspend fun deleteAll() {
        taskActionDao.deleteAll()
    }
}