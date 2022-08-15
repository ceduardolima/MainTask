package com.example.maintask.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maintask.model.database.entity.ActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actionEntity: ActionEntity)

    @Query("SELECT * FROM task_action")
    fun getAllActions(): Flow<List<ActionEntity>>

    @Query("SELECT * FROM task_action WHERE id = :actionId")
    fun getActionById(actionId: Int): ActionEntity

    @Query("DELETE FROM task_action")
    suspend fun deleteAll()
}