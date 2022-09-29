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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.model.converter.TaskActionConverter
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.daysLeft.DaysLeft
import com.example.maintask.viewmodel.DetailViewModel
import com.example.maintask.viewmodel.SharedDataViewModel
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
    private val detailViewModel: DetailViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId = arguments?.getInt("task_id")
        if(taskId == null){
            getBack()
        } else {
            detailViewModel.loadData {
                loadTaskAndActionList(taskId)
            }
        }
    }

    private fun loadTaskAndActionList(taskId: Int) {
        sharedDataViewModel.loadTask(requireActivity(), taskId)
        sharedDataViewModel.loadActionList(requireActivity(), taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_task, container, false)
        initializeViews(view)
        observerIfDataWasLoaded()
        getButtonClick()
        observerChangeActivityButtonClick()
        return view
    }

    private fun initializeViews(view: View) {
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

    private fun observerIfDataWasLoaded() {
        detailViewModel.dataWasLoaded.observe(requireActivity()) { wasLoaded ->
            if (wasLoaded) {
                setFragmentInformation()
                ableVisibility()
            }
        }
    }

    private fun setFragmentInformation() {
        sharedDataViewModel.currentTask.observe(requireActivity()) { task ->
            sharedDataViewModel.actionList.observe(requireActivity()) { actionList ->
                if (task != null) {
                    Log.i("task-teste", "$task")
                    val actionTitleList = TaskActionConverter().toActionTitleList(actionList)
                    setAllText(task, actionTitleList.joinToString(", "))
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllText(currentTask: TaskEntity, actionsString: String) {
        taskTitle.text = currentTask.title
        deadline.text = DaysLeft
            .getDaysLeftString(LocalDate.parse(currentTask.date).dayOfYear)
        taskAuthor.text = "Autor: ${currentTask.author}"
        taskDescription.text = "Descrição: ${currentTask.description}"
        taskActions.text = "Ações: $actionsString"
        taskTools.text = "Ferramentas: ${currentTask.tools}"
    }

    private fun ableVisibility() {
        progressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
        goToTimerFragmentButton.visibility = View.VISIBLE
    }

    private fun getButtonClick() {
        goToTimerFragmentButton.setOnClickListener {
            detailViewModel.setButtonClick(true)
        }
    }

    private fun observerChangeActivityButtonClick() {
        detailViewModel.buttonClick.observe(requireActivity()) { click ->
            if(click) {
                findNavController().navigate(R.id.action_detailTaskFragment_to_timerFragment)
                detailViewModel.setButtonClick(false)
            }
        }
    }

    override fun onDestroyView() {
        disableVisibility()
        super.onDestroyView()
    }

    private fun disableVisibility() {
        progressBar.visibility = View.VISIBLE
        scrollView.visibility = View.INVISIBLE
        goToTimerFragmentButton.visibility = View.INVISIBLE
    }

    fun getBack() {
        val navController = findNavController()
        navController.navigate(R.id.action_detailTaskFragment_to_taskFragment)
    }
}