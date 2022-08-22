package com.example.maintask.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.CurrentActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentActionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(currentActionEntity: CurrentActionEntity)

    @Query("SELECT * FROM current_action")
    fun getAllActions(): Flow<List<CurrentActionEntity>>

    @Query("SELECT * FROM current_action WHERE id = :actionId")
    fun getActionById(actionId: Int): Flow<CurrentActionEntity>

    @Query("DELETE FROM current_action")
    suspend fun deleteAll()
}