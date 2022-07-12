package com.example.maintask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.maintask.model.TaskModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskViewModel(application: Application) : AndroidViewModel(application) {


    fun getTaskList(): MutableList<TaskModel> {
        // Simulando retorno do servidor
        return mutableListOf(
            TaskModel(
                "Titulo",
                LocalDate.parse("2022-09-02", DateTimeFormatter.ISO_DATE),
                0,
                true,
                "Carlos Eduardo",
                "oste explodiu e tal e tal e tal e tal e tal e tal e tal e tal e tal e tal e tal",
                "....",
                "....",
            )
        )
    }
}