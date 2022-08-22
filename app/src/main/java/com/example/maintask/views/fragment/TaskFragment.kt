package com.example.maintask.views.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.model.adapters.TaskAdapter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.task.TaskModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import com.example.maintask.viewmodel.TaskViewModel

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
        initializeDatabase(tasks[0])
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
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        lateButton.setOnClickListener {changeListVisibility(arrowLate, recyclerView)}
        return view
    }

    private fun initializeDatabase(task: TaskModel){
        val taskEntity = taskViewModel.getTaskEntity(listOf(task))
        val actionEntity = taskViewModel.getActionEntity(task.actions)
        val relationEntity = taskViewModel.getTaskActionRelationEntity(taskEntity, actionEntity)

        roomViewModel.populateDatabase(taskEntity, actionEntity, relationEntity)
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