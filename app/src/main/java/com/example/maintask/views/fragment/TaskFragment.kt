package com.example.maintask.views.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.model.task.TaskModel
import com.example.maintask.model.adapters.TaskAdapter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import com.example.maintask.viewmodel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TaskFragment : Fragment(){
    private val taskViewModel = TaskViewModel()
    private var callbacks: MainActivityCallbacks? = null
    private val roomViewModel: RoomViewModel by viewModels {
        val roomApplication = (requireActivity().application as RoomApplication)
        RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as MainActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tasks = taskViewModel.getTaskList()
        callbacks?.selectedTask = tasks[0]

        val taskEntity = taskViewModel.getTaskEntity(tasks)
        val actionEntity = taskViewModel.getActionEntity(tasks[0].actions)
        val relationEntity = taskViewModel.getTaskActionRelationEntity(taskEntity, actionEntity)

        roomViewModel.deleteTaskTable()
        roomViewModel.insertTaskList(taskEntity)
        roomViewModel.insertActionList(actionEntity)
        roomViewModel.insertRelationList(relationEntity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        val lateButton = view.findViewById<Button>(R.id.task_late_bt)
        val arrowLate = view.findViewById<ImageView>(R.id.task_late_arrow)
        val recyclerView = view.findViewById<RecyclerView>(R.id.task_late_recycler)

        val adapter = TaskAdapter(findNavController())
        roomViewModel.allTasks.observe(viewLifecycleOwner){task ->
            task.let { adapter.submitList(task) }
            Log.i("teste-db", task.toString())
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
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