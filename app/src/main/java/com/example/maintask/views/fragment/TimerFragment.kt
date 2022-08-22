package com.example.maintask.views.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.model.adapters.TimerAdapter
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.task.TaskActionModel
import com.example.maintask.model.task.TaskModel
import com.example.maintask.viewmodel.LoginViewModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import com.example.maintask.viewmodel.TimerViewModel
import kotlin.concurrent.timer


class TimerFragment : Fragment() {
    private lateinit var timerRecyclerView: RecyclerView
    private lateinit var actionsId: IntArray
    private lateinit var timerViewModel: TimerViewModel
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
        actionsId = requireArguments().getIntArray("actions_id")!!
        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        setupRoomActionObserver()
    }

    private fun setupRoomActionObserver(){
        roomViewModel.allActions.observe(requireActivity()) { actions ->
            val actionsEntityList = mutableListOf<ActionEntity>()
            for(action in actions)
                if(actionsId.contains(action.id))
                    actionsEntityList.add(action)
            timerViewModel.setActionEntityList(actionsEntityList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val timerFragment = inflater.inflate(R.layout.fragment_timer, container, false)
        setupTimerViewModelObserver(timerFragment)
        return timerFragment
    }

    private fun setupTimerViewModelObserver(view: View){
        timerViewModel.actionModel.observe(viewLifecycleOwner){ actionModelList ->
            createTheRecyclerViewAndSetAdapter(view, actionModelList.toMutableList())
        }
    }

    private fun createTheRecyclerViewAndSetAdapter(fragment: View, actionModel: MutableList<TaskActionModel>){
        val timerAdapter = TimerAdapter(requireContext(), actionModel)
        timerRecyclerView = fragment.findViewById(R.id.timer_action_recycler_view)
        timerRecyclerView.layoutManager = LinearLayoutManager(context)
        timerRecyclerView.hasFixedSize()
        timerRecyclerView.adapter = timerAdapter
    }

}