package com.example.maintask.model.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class ManualMigration {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE team_member (" +
                        "id INTEGER PRIMARY KEY NOT NULL," +
                        "name TEXT NOT NULL," +
                        "photoPath TEXT NOT NULL," +
                        "workTime INTEGER NOT NULL DEFAULT 0)")
            }

        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE employee (" +
                        "id INTEGER PRIMARY KEY NOT NULL," +
                        "name TEXT NOT NULL," +
                        "photoPath TEXT NOT NULL)")
            }

        }
    }
}