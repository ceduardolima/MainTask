package com.example.maintask.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.maintask.model.database.dao.*
import com.example.maintask.model.database.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [TaskEntity::class, ActionEntity::class, TaskActionRelationEntity::class],
version = 1, exportSchema = true)
abstract class TaskRoomDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun actionDao(): ActionDao
    abstract fun taskActionRelationDao(): TaskActionRelationDao

    private class TaskDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.taskDao(),
                        database.actionDao(),
                        database.taskActionRelationDao(),
                    )
                }
            }
        }

        suspend fun populateDatabase(
            taskDao: TaskDao,
            actionDao: ActionDao,
            taskActionDao: TaskActionRelationDao) {
            taskDao.deleteAll()
            actionDao.deleteAll()
            taskDao.deleteAll()

            taskDao.insert(
                TaskEntity(
                    1,
                    "teste",
                    "2020-01-01",
                    0,
                    true,
                    "Carlos Eduardo",
                    "O carro passou por um buraco e fucrou o pneu. Há urgência nessa solicitação pois precisamos do quanto antes que ele eteja em boas condições para que possamos executar as tarefas do dia",
                    "Estepe, macaco, chave L",
                )
            )

            actionDao.insert(ActionEntity("acao 1", 0, "00:00:00"))
            actionDao.insert(ActionEntity("acao 2", 1, "00:00:00"))

            taskActionDao.insert(TaskActionRelationEntity(1, 1))
            taskActionDao.insert(TaskActionRelationEntity( 1, 2))
        }


    }

    companion object{
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TaskRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_database"
                )
                    .addCallback(TaskDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}