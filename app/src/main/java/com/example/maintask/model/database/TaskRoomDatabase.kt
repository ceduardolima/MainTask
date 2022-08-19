package com.example.maintask.model.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.maintask.model.database.dao.ActionDao
import com.example.maintask.model.database.dao.TaskActionRelationDao
import com.example.maintask.model.database.dao.TaskDao
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskActionRelationEntity
import com.example.maintask.model.database.entity.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Database(entities = [TaskEntity::class, ActionEntity::class, TaskActionRelationEntity::class],
version = 1, exportSchema = false)
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
                        database.taskActionRelationDao()
                    )
                    Log.i("teste-db", "base populada")
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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}