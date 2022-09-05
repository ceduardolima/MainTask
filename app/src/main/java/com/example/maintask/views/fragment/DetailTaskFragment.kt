package com.example.maintask.views.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.viewmodel.DetailTaskViewModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.properties.Delegates

class DetailTaskFragment : Fragment() {
    private var taskId by Delegates.notNull<Int>()
    private lateinit var scrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var taskTitle: TextView
    private lateinit var deadline: TextView
    private lateinit var taskAuthor: TextView
    private lateinit var taskDescription: TextView
    private lateinit var taskActions: TextView
    private lateinit var taskTools: TextView
    private lateinit var goToTimerFragmentButton: Button
    private val detailTaskViewModel: DetailTaskViewModel by viewModels()
    private val roomViewModel: RoomViewModel by viewModels {
        val roomApplication = (requireActivity().application as RoomApplication)
        RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = requireArguments().getInt("task_id")
        detailTaskViewModel.loadData {
            getCurrentTask()
            getCurrentActionList()
        }
    }

    private fun getCurrentTask() {
        roomViewModel.allTasks.observe(requireActivity()) { taskList ->
            detailTaskViewModel.findAndSetTask(taskId, taskList)
        }
    }

    private fun getCurrentActionList() {
        roomViewModel.getActionByTaskId(taskId).observe(requireActivity()) { actionList ->
            val actionStringList = mutableListOf<String>()
            detailTaskViewModel.setActionList(actionList)
            for (action in actionList)
                actionStringList.add(action.action)
            detailTaskViewModel.setActionStringList(actionStringList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_task, container, false)
        initializeVariables(view)
        setupFragmentInformation()
        return view
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

    private fun setupFragmentInformation() {
        detailTaskViewModel.progressBar.observe(requireActivity()) { isLoadingData ->
            if (!isLoadingData) {
                setFragmentInformation()
                changeToTimerFragment()
                ableVisibility()
            }
        }
    }

    private fun setFragmentInformation() {
        detailTaskViewModel.currentTask.observe(requireActivity()) { task ->
            detailTaskViewModel.actionStringList.observe(requireActivity()) { stringList ->
                setAllText(task, stringList.joinToString(", "))
            }
        }
    }

    private fun changeToTimerFragment() {
        goToTimerFragmentButton.setOnClickListener {
            val extras = Bundle()
            extras.putInt("task_id", taskId)
            findNavController()
                .navigate(R.id.action_detailTaskFragment_to_timerFragment, extras)
        }
    }

    private fun ableVisibility() {
        progressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
    }

    private fun disableVisibility() {
        progressBar.visibility = View.VISIBLE
        scrollView.visibility = View.INVISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun setAllText(currentTask: TaskEntity, actionsString: String) {
        taskTitle.text = currentTask.title
        deadline.text = daysLeft(LocalDate.parse(currentTask.date).dayOfYear)
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

    override fun onDestroyView() {
        disableVisibility()
        super.onDestroyView()
    }

    fun getBackAndResetActionTimer() {
        val actionList = detailTaskViewModel.actionList
        val navController = findNavController()
        CoroutineScope(Main).launch {
            roomViewModel.resetElapsedTime(actionList)
            navController.navigate(R.id.action_detailTaskFragment_to_taskFragment)
        }
    }
}