package com.example.maintask.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maintask.model.database.entity.CurrentTaskEntity
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentTaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(currentTaskEntity: CurrentTaskEntity)

    @Query("SELECT * FROM current_task")
    fun getAllTask(): Flow<List<CurrentTaskEntity>>

    @Query("SELECT * FROM current_task WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): CurrentTaskEntity

    @Query("DELETE FROM current_task")
    suspend fun deleteAll()
}