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
import com.example.maintask.viewmodel.TaskViewModel

class TaskFragment : Fragment() {
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var container: ConstraintLayout
    private lateinit var lateButton: Button
    private lateinit var arrowLate: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

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
        lateButton.setOnClickListener { changeListVisibility(arrowLate, recyclerView) }
        return view
    }

    private fun initializeViews(view: View) {
        lateButton = view.findViewById(R.id.task_late_bt)
        arrowLate = view.findViewById(R.id.task_late_arrow)
        recyclerView = view.findViewById(R.id.task_late_recycler)
        progressBar = view.findViewById(R.id.task_progress_bar)
        container = view.findViewById(R.id.task_view_container)
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
        taskViewModel
            .observerTaskListAndCreateRecyclerView(requireActivity()) { taskList ->
                createRecyclerView(taskList)
            }
    }

    private fun createRecyclerView(taskList: List<TaskEntity>) {
        val adapter = TaskAdapter(taskViewModel)
        adapter.submitList(taskList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun navigateToDetailTask(id: Int) {
        val extras = Bundle()
        extras.putInt("task_id", id)
        findNavController()
            .navigate(R.id.action_taskFragment_to_detailTaskFragment, extras)
        taskViewModel.setButtonClick(false)
    }

    private fun changeLayoutParams(recyclerView: RecyclerView) =
        // Muda o tamanho do recycler view de acordo com a quantidade de task. Maximo 3 tasks
        if (recyclerView.size < 300) {
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

    override fun onDestroyView() {
        disableViewVisibility()
        super.onDestroyView()
    }

    private fun disableViewVisibility() {
        container.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }
}