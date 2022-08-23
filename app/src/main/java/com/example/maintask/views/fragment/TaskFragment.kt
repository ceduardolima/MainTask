package com.example.maintask.views.fragment

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
import com.example.maintask.model.adapters.TaskAdapter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import com.example.maintask.viewmodel.TaskViewModel

class TaskFragment : Fragment() {
    private val taskViewModel = TaskViewModel()
    private lateinit var lateButton: Button
    private lateinit var arrowLate: ImageView
    private lateinit var recyclerView: RecyclerView
    private val roomViewModel: RoomViewModel by viewModels {
        val roomApplication = (requireActivity().application as RoomApplication)
        RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository,
            roomApplication.currentTaskRepository,
            roomApplication.currentActionRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserveTaskId()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        initWidgets(view)
        initRecycleView()
        lateButton.setOnClickListener { changeListVisibility(arrowLate, recyclerView) }
        return view
    }

    private fun initWidgets(view: View) {
        lateButton = view.findViewById(R.id.task_late_bt)
        arrowLate = view.findViewById(R.id.task_late_arrow)
        recyclerView = view.findViewById(R.id.task_late_recycler)
    }

    private fun initRecycleView() {
        val adapter = TaskAdapter(findNavController(), taskViewModel)
        roomViewModel.allTasks.observe(viewLifecycleOwner) { task ->
            task.let { adapter.submitList(task) }
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }
    }

    private fun setupObserveTaskId() {
        taskViewModel.taskId.observe(requireActivity()) { taskId ->
            if (taskId != null) {
                setCurrentTask(taskId)
                setupActionIdList(taskId)
                setupActionListAndChangeFragment()
            }
        }
    }

    private fun setCurrentTask(taskId: Int) {
        roomViewModel.allTasks.observe(requireActivity()) { taskList ->
            for (task in taskList)
                if (taskId == task.id) {
                    roomViewModel.setCurrentTask(task)
                    break
                }
        }
    }

    private fun setupActionIdList(taskId: Int) {
        roomViewModel.allTasActionRelations.observe(requireActivity()) { relationList ->
            val actionIdList = mutableListOf<Int>()
            for (relation in relationList)
                if (relation.taskId == taskId && !actionIdList.contains(relation.actionIn))
                    actionIdList.add(relation.actionIn)
            taskViewModel.setActionIdList(actionIdList)
        }
    }

    private fun setupActionListAndChangeFragment() {
        taskViewModel.actionIdList.observe(requireActivity()) { idList ->
            setupRoomObserverActions(idList)
            navigateToDetailTask()
        }
    }

    private fun setupRoomObserverActions(actionIdList: List<Int>) =
        roomViewModel.allActions.observe(requireActivity()) { actionsList ->
            val actionList = mutableListOf<ActionEntity>()
            for (action in actionsList)
                if (actionIdList.contains(action.id))
                    actionList.add(action)
            roomViewModel.setCurrentAction(actionList)
        }

    private fun navigateToDetailTask() {
        taskViewModel.buttonClick.observe(requireActivity()) { click ->
            if (click) {
                findNavController().navigate(R.id.action_taskFragment_to_detailTaskFragment)
                taskViewModel.setButtonClick(false)
            }
        }
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


}