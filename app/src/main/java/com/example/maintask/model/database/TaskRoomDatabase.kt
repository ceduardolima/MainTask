package com.example.maintask.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.maintask.model.database.dao.ActionDao
import com.example.maintask.model.database.dao.TaskActionRelationDao
import com.example.maintask.model.database.dao.TaskDao
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import com.example.maintask.model.database.entity.TaskEntity

@Database(entities = [TaskEntity::class, ActionEntity::class, TaskActionRelationEntity::class],
version = 1, exportSchema = false)
abstract class TaskRoomDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun actionDao(): ActionDao
    abstract fun taskActionRelationDao(): TaskActionRelationDao

    companion object{
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(context: Context): TaskRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}