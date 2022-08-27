package com.example.maintask.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.maintask.R
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.CurrentActionEntity
import com.example.maintask.viewmodel.CompletedActionsViewModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory

class CompletedActionsFragment : Fragment() {
    private lateinit var completedActionsViewModel: CompletedActionsViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: NestedScrollView
    private lateinit var elapsedTime: TextView
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
        completedActionsViewModel =
            ViewModelProvider(requireActivity())[CompletedActionsViewModel::class.java]
        completedActionsViewModel.loadDatabase {
            loadActions()
        }
    }

    private fun loadActions() {
        roomViewModel.currentAction.observe(requireActivity()) { actions ->
            if(actions.isNotEmpty()){
                completedActionsViewModel.setActions(actions)
                calculateElapsedTime(actions, 0)
            }
        }
    }

    private fun calculateElapsedTime(actions: List<CurrentActionEntity>, index: Int) {
        if(index >= actions.size)
            return
        val seconds = getSeconds(actions[index].elapsedTime)
        val minutes = getMinutes(actions[index].elapsedTime)
        val hours = getHours(actions[index].elapsedTime)
        val timeList = mutableListOf(hours, minutes, seconds)
        val newIndex = index + 1
        completedActionsViewModel.addElapsedTime(timeList)
        calculateElapsedTime(actions, newIndex)
    }

    private fun getSeconds(time: String) = time.split(":")[2].toInt()

    private fun getMinutes(time: String) = time.split(":")[1].toInt()

    private fun getHours(time: String) = time.split(":")[0].toInt()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val completedActionsFragment = inflater.inflate(
            R.layout.fragment_completed_actions,
            container,
            false
        )
        init(completedActionsFragment)
        setupProgressBarObserver()
        setupElapsedTimeObserver()
        return completedActionsFragment
    }

    private fun init(view: View) {
        progressBar =  view.findViewById(R.id.completed_actions_progress_bar)
        scrollView = view.findViewById(R.id.completed_actions_scrollview)
        elapsedTime = view.findViewById(R.id.completed_actions_time_spend)
    }

    private fun setupProgressBarObserver() {
        completedActionsViewModel.progressBar.observe(requireActivity()) { isComplete ->
            if (isComplete)
                ableVisibility()
        }
    }

    private fun ableVisibility() {
        progressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
    }

    private fun setupElapsedTimeObserver() {
        completedActionsViewModel.elapsedTime.observe(requireActivity()) { actions ->
            elapsedTime.text = completedActionsViewModel.getElapsedTime()
        }
    }

    override fun onDestroyView() {
        completedActionsViewModel.resetElapsedTime()
        super.onDestroyView()
    }



}