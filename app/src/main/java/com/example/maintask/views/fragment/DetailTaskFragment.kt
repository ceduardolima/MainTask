package com.example.maintask.views.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import java.time.LocalDate

class DetailTaskFragment : Fragment() {
    private var callbacks: MainActivityCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as MainActivityCallbacks
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val task = callbacks?.selectedTask
        val view = inflater.inflate(R.layout.fragment_detail_task, container, false)
        val taskTitle = view.findViewById<TextView>(R.id.details_task_title)
        val deadline = view.findViewById<TextView>(R.id.details_task_days)
        val taskAuthor = view.findViewById<TextView>(R.id.details_task_author)
        val taskDescription = view.findViewById<TextView>(R.id.details_task_description)
        val taskActions = view.findViewById<TextView>(R.id.details_task_actions)
        val taskTools = view.findViewById<TextView>(R.id.details_task_tools)
        val goToTimerFragmentButton = view.findViewById<Button>(R.id.details_task_start_os)

        taskTitle.text = task?.title
        deadline.text = daysLeft(task?.date?.dayOfYear ?: 0)
        taskAuthor.text = "Autor: ${task?.author}"
        taskDescription.text = "Descrição: ${task?.description}"
        taskActions.text = "Ações: ${task?.actions}"
        taskTools.text = "Ferramentas: ${task?.tools}"

        goToTimerFragmentButton.setOnClickListener { findNavController().navigate(R.id.action_detailTaskFragment_to_timerFragment) }

        return view
    }

    private fun daysLeft(days: Int): String {
        val today = LocalDate.now().dayOfYear
        val daysLeft = days - today

        return when {
            daysLeft == 0 -> "Para hoje"
            daysLeft > 0 -> "Faltam ${daysLeft} dias"
            else -> "Esta a ${daysLeft * (-1)} dias atrasado"
        }
    }

}