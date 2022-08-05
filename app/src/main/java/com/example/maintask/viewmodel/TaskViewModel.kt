package com.example.maintask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.task.TaskModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskViewModel(application: Application) : AndroidViewModel(application) {


    fun getTaskList(): MutableList<TaskModel> {
        // Simulando retorno do servidor
        return mutableListOf(
            TaskModel(
                "Trocar o pneu do carro",
                LocalDate.parse("2022-09-02", DateTimeFormatter.ISO_DATE),
                0,
                true,
                "Carlos Eduardo",
                "O carro passou por um buraco e fucrou o pneu. Há urgência nessa solicitação pois precisamos do quanto antes que ele eteja em boas condições para que possamos executar as tarefas do dia",
                "Estepe, macaco, chave L",
                generateActionList()
            )
        )
    }
    private fun generateActionList(): MutableList<TaskActionModel>{
        val mutableList: MutableList<TaskActionModel> = mutableListOf()
        for(n in 1..11) {
            val action = TaskActionModel(
                "Acao $n",
                n
            )
            mutableList.add(action)
        }
        return mutableList
    }
}