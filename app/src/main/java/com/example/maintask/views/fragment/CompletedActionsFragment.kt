package com.example.maintask.views.fragment

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.model.adapters.ExecutorAdapter
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.time.ElapsedTimeHelper
import com.example.maintask.viewmodel.CompletedActionsViewModel
import com.example.maintask.viewmodel.SharedDataViewModel

class CompletedActionsFragment : Fragment() {
    private val completedActionsViewModel: CompletedActionsViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: NestedScrollView
    private lateinit var elapsedTime: TextView
    private lateinit var executorRecyclerView: RecyclerView
    private lateinit var adapter: ExecutorAdapter
    private lateinit var finishCompletedActionsButton: Button
    private val sharedDataViewModel: SharedDataViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        completedActionsViewModel.restartElapsedTime()
        completedActionsViewModel.loadData {
            observerSetElapsedTime()
        }
    }

    private fun observerSetElapsedTime() {
        sharedDataViewModel.actionList.observe(requireActivity()) { taskActionList ->
            calculateElapsedTime(taskActionList, 0)
            setTeam(taskActionList)
            sharedDataViewModel.actionList.removeObservers(requireActivity())
        }
    }

    private fun calculateElapsedTime(actions: List<TaskActionModel>, index: Int) {
        if(index >= actions.size)
            return
        val timeHelper = ElapsedTimeHelper(actions[index].time)
        val seconds = timeHelper.getSeconds()
        val minutes = timeHelper.getMinutes()
        val hours = timeHelper.getHours()
        val timeList = mutableListOf(hours, minutes, seconds)
        val newIndex = index + 1
        completedActionsViewModel.addElapsedTime(timeList)
        calculateElapsedTime(actions, newIndex)
    }

    private fun setTeam(taskActionList: List<TaskActionModel>) {
        val team = hashMapOf<Int, TeamMemberEntity>()
        for(action in taskActionList) {
            val worker = action.worker!!.copy()
            val workerId = action.worker!!.id
            if (team[workerId] == null)
                team[workerId] = worker
            else {
                val workTime = worker.workTime + team[workerId]!!.workTime
                team[workerId]!!.workTime = workTime
            }
        }
        completedActionsViewModel.setTeam(team.values.toList())
    }

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
        createRecyclerView()
        observerUpdateRecyclerView()
        finishCompletedActionsButton()
        observerIfTaskWasSetAsDone()
        return completedActionsFragment
    }

    private fun init(view: View) {
        progressBar =  view.findViewById(R.id.completed_actions_progress_bar)
        scrollView = view.findViewById(R.id.completed_actions_scrollview)
        elapsedTime = view.findViewById(R.id.completed_actions_time_spend)
        executorRecyclerView = view.findViewById(R.id.completed_actions_worker_list)
        finishCompletedActionsButton = view.findViewById(R.id.finish_completed_actions_button)
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
        completedActionsViewModel.elapsedTime.observe(requireActivity()) {
            elapsedTime.text = completedActionsViewModel.getElapsedTime()
        }
    }

    private fun createRecyclerView() {
        adapter = ExecutorAdapter()
        executorRecyclerView.layoutManager = LinearLayoutManager(context)
        executorRecyclerView.hasFixedSize()
        executorRecyclerView.adapter = adapter
    }

    private fun observerUpdateRecyclerView() {
        completedActionsViewModel.team.observe(requireActivity()) {
            team -> adapter.submitList(team)
        }
    }

    private fun finishCompletedActionsButton() {
        finishCompletedActionsButton.setOnClickListener {
            sharedDataViewModel.setTaskAsDone()
        }
    }

    private fun observerIfTaskWasSetAsDone() {
        sharedDataViewModel.taskWasUpdated.observe(requireActivity()) { wasUpdated ->
            if (wasUpdated) {
                findNavController().navigate(R.id.action_completedActionsFragment_to_taskFragment)
                sharedDataViewModel.setTaskWasUpdated(false)
            }
        }
    }

    override fun onDestroyView() {
        completedActionsViewModel.restartElapsedTime()
        completedActionsViewModel.setEmptyTeam()
        super.onDestroyView()
    }

    fun getBack() {
        findNavController()
            .navigate(R.id.action_completedActionsFragment_to_timerFragment)
    }
}