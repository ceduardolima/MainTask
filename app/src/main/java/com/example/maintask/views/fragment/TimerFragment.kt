package com.example.maintask.views.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.model.adapters.TimerAdapter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.task.TaskModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory


class TimerFragment : Fragment() {
    private lateinit var timerRecyclerView: RecyclerView
    private lateinit var selectedTask: TaskModel
    private lateinit var actionsId: IntArray
    private val actionsEntityList = mutableListOf<ActionEntity>()
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
        actionsId = requireArguments().getIntArray("actions_id")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val timerFragment = inflater.inflate(R.layout.fragment_timer, container, false)
        roomViewModel.allActions.observe(requireActivity()) { actions ->
            for(action in actions)
                if(actionsId.contains(action.id))
                    actionsEntityList.add(action)
            createTheRecyclerViewAndSetAdapter(timerFragment)
        }


        return timerFragment
    }

    private fun createTheRecyclerViewAndSetAdapter(fragment: View){
        timerRecyclerView = fragment.findViewById(R.id.timer_action_recycler_view)
        timerRecyclerView.layoutManager = LinearLayoutManager(context)
        timerRecyclerView.hasFixedSize()
        val actionModel = actionEntityToTaskActionModel()
        val timerAdapter = TimerAdapter(requireContext(), actionModel)
        timerRecyclerView.adapter = timerAdapter
    }

    private fun actionEntityToTaskActionModel(): MutableList<TaskActionModel>{
        val actionModel = mutableListOf<TaskActionModel>()
        for(action in actionsEntityList){
            actionModel.add(
                TaskActionModel(
                    action.action,
                    action.order
                )
            )
        }
        return actionModel
    }

}