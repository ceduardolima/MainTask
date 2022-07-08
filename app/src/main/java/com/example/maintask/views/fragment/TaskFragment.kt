package com.example.maintask.views.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.example.maintask.R
import com.example.maintask.model.TaskModel
import com.example.maintask.model.adapters.TaskAdapter
import com.example.maintask.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskFragment : Fragment() {
    private val taskViewModel = TaskViewModel(Application())
    private val taskMutableList: MutableList<TaskModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        val lateButton = view.findViewById<Button>(R.id.task_late_bt)
        val arrowLate = view.findViewById<ImageView>(R.id.task_late_arrow)
        val adapter = TaskAdapter(requireContext(), taskMutableList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.task_late_recycler)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        taskMutableList.addAll(taskViewModel.getTaskList())

        changeLayoutParams(recyclerView)

        lateButton.setOnClickListener {changeListVisibility(arrowLate, recyclerView)}


        return view
    }

    private fun changeLayoutParams(recyclerView: RecyclerView) =
        // Muda o tamanho do recycler view de acordo com a quantidade de task. Maximo 3 tasks
        if(recyclerView.size < 300) {
            val params = recyclerView.layoutParams
            params.height = LayoutParams.WRAP_CONTENT
            recyclerView.layoutParams = params
        } else {
            val params = recyclerView.layoutParams
            params.height = 300
            recyclerView.layoutParams = params
        }

    private fun changeListVisibility(arrow: ImageView, recyclerView: RecyclerView) =
        // Muda a visibilidade do recycler viu sempre q o Arrow foi apertado
        if (recyclerView.isVisible) {
            recyclerView.visibility = View.GONE
            arrow.rotation = 0f
        } else {
            recyclerView.visibility = View.VISIBLE
            arrow.rotation = 90f
        }

}