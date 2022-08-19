package com.example.maintask.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(taskEntity: TaskEntity)

    @Query("SELECT * FROM task")
    fun getAllTask(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntity

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}