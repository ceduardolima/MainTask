package com.example.maintask.views.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.adapters.TimerAdapter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import com.example.maintask.viewmodel.TimerViewModel

class TimerFragment : Fragment() {
    private lateinit var timerRecyclerView: RecyclerView
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var finishButton: Button
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
        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        timerViewModel.loadData {
            getCurrentActionList()
        }
    }

    private fun getCurrentActionList() {
        roomViewModel.currentAction.observe(requireActivity()) { actionList ->
            if (actionList.isNotEmpty()) {
                timerViewModel.setActionList(actionList)
                roomViewModel.currentAction.removeObservers(requireActivity())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val timerFragment = inflater.inflate(R.layout.fragment_timer, container, false)
        initView(timerFragment)
        setupTimerViewModelObserver()
        progressBarObserver()
        completedActionsObserver()

        timerViewModel.currentAction.observe(requireActivity()) { pair ->
            roomViewModel.updateElapsedTime(pair.first, pair.second)
        }
        changeToCompletedActionsFragment()
        return timerFragment
    }

    private fun initView(view: View) {
        progressBar = view.findViewById(R.id.timer_progress_bar)
        timerRecyclerView = view.findViewById(R.id.timer_action_recycler_view)
        finishButton = view.findViewById(R.id.timer_finish_actions)
    }

    private fun setupTimerViewModelObserver(){
        timerViewModel.actionList.observe(viewLifecycleOwner){ actionList ->
            createTheRecyclerViewAndSetAdapter(actionList.toMutableList())
        }
    }

    private fun createTheRecyclerViewAndSetAdapter(actionModel: MutableList<TaskActionModel>){
        val timerAdapter = TimerAdapter(requireContext(), requireActivity(), actionModel, timerViewModel)
        timerRecyclerView.layoutManager = LinearLayoutManager(context)
        timerRecyclerView.hasFixedSize()
        timerRecyclerView.adapter = timerAdapter
    }

    private fun progressBarObserver() {
        timerViewModel.progressBar.observe(requireActivity()) { isLoadingData ->
            if (!isLoadingData)
                ableToShowView()
        }
    }

    private fun ableToShowView() {
        progressBar.visibility = View.GONE
        timerRecyclerView.visibility = View.VISIBLE
        finishButton.visibility = View.VISIBLE
    }

    private fun completedActionsObserver() {
        timerViewModel.completedActions.observe(requireActivity()) { wasCompleted ->
            Log.i("completa", "foi completa? $wasCompleted")
            if (wasCompleted) {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.default_button_shape, null)
                finishButton.background = drawable
                finishButton.isEnabled = true
            }
            else {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.button_color_grey, null)
                finishButton.background = drawable
                finishButton.isEnabled = false
            }
        }
    }

    private fun changeToCompletedActionsFragment(){
        finishButton.setOnClickListener {
            if(finishButton.isEnabled && (timerViewModel.completedActions.value == true)){
                findNavController().navigate(R.id.action_timerFragment_to_completedActionsFragment)
            }
        }
    }

}