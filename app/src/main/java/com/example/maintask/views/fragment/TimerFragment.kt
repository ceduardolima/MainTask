package com.example.maintask.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.adapters.TimerAdapter
import com.example.maintask.viewmodel.SharedDataViewModel
import com.example.maintask.viewmodel.TimerViewModel

class TimerFragment : Fragment() {
    private lateinit var timerRecyclerView: RecyclerView
    private val timerViewModel: TimerViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var finishButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timerViewModel.loadData {
            observerSetTaskActionList()
        }
    }

    private fun observerSetTaskActionList(){
        sharedDataViewModel.actionList.observe(requireActivity()) { actionList ->
            if(actionList.isNotEmpty()) {
                timerViewModel.setTaskActionList(actionList)
                sharedDataViewModel.actionList.removeObservers(requireActivity())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val timerFragment = inflater.inflate(R.layout.fragment_timer, container, false)
        initializeViews(timerFragment)
        createTheRecyclerViewAndSetAdapter()
        observerDataWasLoaded()
        observerCompletedActions()
        observerUpdateTaskActionFromSharedViewModel()
        changeActivity()
        return timerFragment
    }

    private fun initializeViews(view: View) {
        progressBar = view.findViewById(R.id.timer_progress_bar)
        timerRecyclerView = view.findViewById(R.id.timer_action_recycler_view)
        finishButton = view.findViewById(R.id.timer_finish_actions)
    }

    private fun createTheRecyclerViewAndSetAdapter() {
        timerViewModel.taskActionList.observe(requireActivity()) { taskActionList ->
            if (taskActionList.isNotEmpty()) {
                val timerAdapter = TimerAdapter(
                    requireContext(),
                    taskActionList.toMutableList(),
                    timerViewModel
                )
                timerRecyclerView.layoutManager = LinearLayoutManager(context)
                timerRecyclerView.hasFixedSize()
                timerRecyclerView.adapter = timerAdapter
            }
        }
    }

    private fun observerDataWasLoaded() {
        timerViewModel.dataWasLoaded.observe(requireActivity()) { wasLoaded ->
            if(wasLoaded)
                ableViewVisibility()
        }
    }

    private fun ableViewVisibility() {
        progressBar.visibility = View.GONE
        timerRecyclerView.visibility = View.VISIBLE
        finishButton.visibility = View.VISIBLE
    }

    private fun observerCompletedActions() {
        timerViewModel.completedActions.observe(requireActivity()) {
                wasCompleted -> changeButtonDrawable(wasCompleted)
        }
    }

    private fun observerUpdateTaskActionFromSharedViewModel() {
        timerViewModel.taskActionList.observe(requireActivity()) { taskActionList ->
            if(taskActionList.isNotEmpty())
                sharedDataViewModel.setTaskActionList(taskActionList)
        }
    }

    private fun changeButtonDrawable(wasCompleted: Boolean){
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

    private fun changeActivity(){
        finishButton.setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_completedActionsFragment)
        }
    }

    fun getBack() {
        findNavController().navigate(R.id.action_timerFragment_to_detailTaskFragment)
    }
}