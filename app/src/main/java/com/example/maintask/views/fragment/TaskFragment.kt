package com.example.maintask.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.example.maintask.R
import com.example.maintask.model.adapters.TaskAdapter
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.task.StatusCode
import com.example.maintask.viewmodel.TaskViewModel

class TaskFragment : Fragment() {
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var container: ConstraintLayout
    private lateinit var lateButton: Button
    private lateinit var completedButton: Button
    private lateinit var arrowLate: ImageView
    private lateinit var arrowCompleted: ImageView
    private lateinit var lateRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var completedRecyclerView: RecyclerView
    private lateinit var lateAdapter: TaskAdapter
    private lateinit var completedAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.loadData {}
        taskViewModel.observerButtonClickAndNavigate(requireActivity()) { id ->
            navigateToDetailTask(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        initializeViews(view)
        setVisibility()
        initializeRecyclerView()
        changeLayoutParams(lateRecyclerView)
        changeLayoutParams(completedRecyclerView)
        changeAllListVisibility()
        return view
    }

    private fun initializeViews(view: View) {
        lateButton = view.findViewById(R.id.task_late_bt)
        completedButton = view.findViewById(R.id.task_completed_bt)
        arrowLate = view.findViewById(R.id.task_late_arrow)
        arrowCompleted = view.findViewById(R.id.task_completed_arrow)
        lateRecyclerView = view.findViewById(R.id.task_late_recycler)
        progressBar = view.findViewById(R.id.task_progress_bar)
        container = view.findViewById(R.id.task_view_container)
        completedRecyclerView = view.findViewById(R.id.task_completed_recycler)
        lateAdapter = TaskAdapter(taskViewModel)
        completedAdapter = TaskAdapter(taskViewModel)
    }

    private fun setVisibility() {
        taskViewModel.loadDataStatus.observe(requireActivity()) { wasLoaded ->
            if (wasLoaded) {
                ableViewVisibility()
            }
        }
    }

    private fun ableViewVisibility() {
        container.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun initializeRecyclerView() {
        taskViewModel.observerTaskListAndCreateRecyclerView(requireActivity()) {
                    taskList -> createAllRecyclerViews(taskList)
            }
    }

    private fun createAllRecyclerViews(taskList: List<TaskEntity>) {
        val taskMap = organizeTasks(taskList)
        createRecyclerView(lateRecyclerView, taskMap[StatusCode.LATE]!!)
        createRecyclerView(completedRecyclerView, taskMap[StatusCode.COMPLETED]!!)
    }

    private fun createRecyclerView(recyclerView: RecyclerView, taskList: List<TaskEntity>) {
        val adapter = TaskAdapter(taskViewModel)
        adapter.submitList(taskList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun organizeTasks(
        taskList: List<TaskEntity>
    ): Map<Int, MutableList<TaskEntity>> {
        val taskMap = createTaskMap()
        for (task in taskList) {
            val status = task.status
            taskMap[status]!!.add(task)
        }
        return taskMap
    }

    private fun createTaskMap(): Map<Int, MutableList<TaskEntity>> {
        val taskMap = mutableMapOf<Int, MutableList<TaskEntity>>()
        taskMap[StatusCode.LATE] = mutableListOf()
        taskMap[StatusCode.COMPLETED] = mutableListOf()
        taskMap[StatusCode.TODO] = mutableListOf()
        return taskMap
    }

    private fun navigateToDetailTask(id: Int) {
        val extras = Bundle()
        extras.putInt("task_id", id)
        findNavController()
            .navigate(R.id.action_taskFragment_to_detailTaskFragment, extras)
        taskViewModel.setButtonClick(false)
    }

    private fun changeLayoutParams(recyclerView: RecyclerView) =
        if (recyclerView.size < 300) {
            val params = recyclerView.layoutParams
            params.height = LayoutParams.WRAP_CONTENT
            recyclerView.layoutParams = params
        } else {
            val params = recyclerView.layoutParams
            params.height = 300
            recyclerView.layoutParams = params
        }

    private fun changeAllListVisibility() {
        completedButtonListener(lateButton, arrowLate, lateRecyclerView)
        completedButtonListener(completedButton, arrowCompleted, completedRecyclerView)
    }

    private fun completedButtonListener(button: Button, arrow: ImageView, recyclerView: RecyclerView) {
        button.setOnClickListener { changeListVisibility(arrow, recyclerView) }
    }

    private fun changeListVisibility(arrow: ImageView, recyclerView: RecyclerView) =
        if (recyclerView.isVisible) {
            recyclerView.visibility = View.GONE
            arrow.rotation = 0f
        } else {
            recyclerView.visibility = View.VISIBLE
            arrow.rotation = 90f
        }

    override fun onDestroyView() {
        disableViewVisibility()
        super.onDestroyView()
    }

    private fun disableViewVisibility() {
        container.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }
}