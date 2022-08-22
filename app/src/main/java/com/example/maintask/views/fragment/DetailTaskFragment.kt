package com.example.maintask.views.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.CurrentTaskEntity
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.viewmodel.DetailTaskViewModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import java.time.LocalDate

class DetailTaskFragment : Fragment() {
    private lateinit var scrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var taskTitle: TextView
    private lateinit var deadline: TextView
    private lateinit var taskAuthor: TextView
    private lateinit var taskDescription: TextView
    private lateinit var taskActions: TextView
    private lateinit var taskTools: TextView
    private lateinit var goToTimerFragmentButton: Button
    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private var bundleExtra: IntArray = emptyArray<Int>().toIntArray()
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
        detailTaskViewModel = ViewModelProvider(this)[DetailTaskViewModel::class.java]
        detailTaskViewModel.loadData {
            getCurrentTask()
            getCurrentActionList()
        }
        Log.i("teste", "onCreate")
    }

    private fun getCurrentTask() {
        roomViewModel.currentTask.observe(requireActivity()) { taskList ->
            if(taskList.isNotEmpty())
                detailTaskViewModel.setCurrentTask(taskList[0])
        }
    }

    private fun getCurrentActionList() {
        roomViewModel.currentAction.observe(requireActivity()) { actionList ->
            if (actionList.isNotEmpty()) {
                val actionStringList = mutableListOf<String>()
                for (action in actionList)
                    actionStringList.add(action.action)
                detailTaskViewModel.setActionStringList(actionStringList)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("teste", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_detail_task, container, false)
        initializeVariables(view)
        detailTaskViewModel.progressBar.observe(requireActivity()) { isLoadingData ->
            if (!isLoadingData) {
                setFragmentInformation()
                changeToTimerFragment()
                ableVisibility()
            }
        }
        return view
    }

    private fun changeToTimerFragment() {
        goToTimerFragmentButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putIntArray("actions_id", bundleExtra)
            findNavController()
                .navigate(R.id.action_detailTaskFragment_to_timerFragment, bundle)
        }
    }

    private fun initializeVariables(view: View) {
        taskTitle = view.findViewById(R.id.details_task_title)
        deadline = view.findViewById(R.id.details_task_days)
        taskAuthor = view.findViewById(R.id.details_task_author)
        taskDescription = view.findViewById(R.id.details_task_description)
        taskActions = view.findViewById(R.id.details_task_actions)
        taskTools = view.findViewById(R.id.details_task_tools)
        goToTimerFragmentButton = view.findViewById(R.id.details_task_start_os)
        progressBar = view.findViewById(R.id.detail_fragment_progress_bar)
        scrollView = view.findViewById(R.id.detail_fragment_scroll_view)
    }

    private fun setFragmentInformation() {
        detailTaskViewModel.currentTask.observe(requireActivity()) { task ->
            detailTaskViewModel.actionStringList.observe(requireActivity()) { stringList ->
                setAllText(task, stringList.joinToString(", "))
            }
        }
    }

    private fun ableVisibility() {
        progressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun setAllText(currentTask: CurrentTaskEntity, actionsString: String){
        taskTitle.text = currentTask.title
        deadline.text = daysLeft(LocalDate.parse(currentTask.date).dayOfYear )
        taskAuthor.text = "Autor: ${currentTask.author}"
        taskDescription.text = "Descrição: ${currentTask.description}"
        taskActions.text = "Ações: $actionsString"
        taskTools.text = "Ferramentas: ${currentTask.tools}"
    }

    private fun daysLeft(days: Int): String {
        val today = LocalDate.now().dayOfYear
        val daysLeft = days - today

        return when {
            daysLeft == 0 -> "Para hoje"
            daysLeft > 0 -> "Faltam ${daysLeft} dias"
            else -> "Esta à ${daysLeft * (-1)} dia(s) atrasado"
        }
    }
}