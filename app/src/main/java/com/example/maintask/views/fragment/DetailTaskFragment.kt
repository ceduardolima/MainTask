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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.model.daysLeft.DaysLeft
import com.example.maintask.viewmodel.TimerCounterProcessViewModel
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
    private val timerCounterProcessViewModel: TimerCounterProcessViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId = requireArguments().getInt("task_id")
        timerCounterProcessViewModel.loadData {
            timerCounterProcessViewModel.loadTaskAndActions(requireActivity(), taskId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_task, container, false)
        initializeViews(view)
        setInformationWhenDataLoad()
        getButtonClick()
        changeToTimerFragment()
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

    private fun setInformationWhenDataLoad() {
        timerCounterProcessViewModel.observeIfWasDataLoaded(requireActivity()) {
            setFragmentInformation()
            ableVisibility()
        }
    }

    private fun setFragmentInformation() {
        val actionTitleList = timerCounterProcessViewModel.actionTitleList
        val currentTask = timerCounterProcessViewModel.currentTask
        currentTask.observe(requireActivity()) { task ->
            if(task != null) {
                actionTitleList.observe(requireActivity()) { titleList ->
                    setAllText(task, titleList.joinToString(", "))
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

    private fun getButtonClick() {
        goToTimerFragmentButton.setOnClickListener {
            timerCounterProcessViewModel.setButtonClick(true)
        }
    }

    private fun changeToTimerFragment() {
        timerCounterProcessViewModel
            .observerDetailFragmentButtonClickAndNavigate(requireActivity()) {
                findNavController()
                    .navigate(R.id.action_detailTaskFragment_to_timerFragment)
            }
    }

    private fun ableVisibility() {
        progressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        disableVisibility()
        super.onDestroyView()
    }

    private fun disableVisibility() {
        progressBar.visibility = View.VISIBLE
        scrollView.visibility = View.INVISIBLE
    }

    fun getBackAndResetActionTimer() {
        val navController = findNavController()
        navController.navigate(R.id.action_detailTaskFragment_to_taskFragment)
    }
}