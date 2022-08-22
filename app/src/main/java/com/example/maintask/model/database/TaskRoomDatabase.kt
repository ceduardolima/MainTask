package com.example.maintask.model.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.maintask.model.database.dao.*
import com.example.maintask.model.database.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [TaskEntity::class, ActionEntity::class, TaskActionRelationEntity::class,
    CurrentTaskEntity::class, CurrentActionEntity::class],
version = 3, exportSchema = true)
abstract class TaskRoomDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun actionDao(): ActionDao
    abstract fun taskActionRelationDao(): TaskActionRelationDao
    abstract fun currentTaskDao(): CurrentTaskDao
    abstract fun currentActionDao(): CurrentActionDao




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

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE 'current_task' (" +
                        "id INTEGER NOT NULL PRIMARY KEY," +
                        "title TEXT NOT NULL," +
                        "date TEXT NOT NULL," +
                        "status INTEGER NOT NULL," +
                        "is_emergency INTEGER NOT NULL," +
                        "author TEXT NOT NULL," +
                        "description TEXT NOT NULL," +
                        "tools TEXT NOT NULL)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE 'current_action' (" +
                        "'action' TEXT NOT NULL," +
                        "'order' INTEGER NOT NULL," +
                        "elapsed_time TEXT NOT NULL," +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): TaskRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_database"
                )
                    .addCallback(TaskDatabaseCallback(scope))
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}