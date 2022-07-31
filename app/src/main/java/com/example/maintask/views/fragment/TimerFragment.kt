package com.example.maintask.views.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.model.adapters.TimerAdapter
import com.example.maintask.model.task.TaskModel
import com.example.maintask.viewmodel.TimerViewModel


class TimerFragment : Fragment() {
    private var callbacks: MainActivityCallbacks? = null
    private lateinit var timerRecyclerView: RecyclerView
    private val timerViewModel = TimerViewModel(Application())
    private lateinit var selectedTask: TaskModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as MainActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(callbacks?.selectedTask != null)
            selectedTask = callbacks!!.selectedTask!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val timerFragment = inflater.inflate(R.layout.fragment_timer, container, false)
        createTheRecyclerViewAndSetAdapter(timerFragment)
        return timerFragment
    }

    private fun createTheRecyclerViewAndSetAdapter(fragment: View){
        timerRecyclerView = fragment.findViewById(R.id.timer_action_recycler_view)
        timerRecyclerView.layoutManager = LinearLayoutManager(context)
        timerRecyclerView.hasFixedSize()
        val mutableListOfActions = timerViewModel.stringToMutableTaskActionModel(selectedTask.actions)
        val timerAdapter = TimerAdapter(requireContext(), mutableListOfActions)
        timerRecyclerView.adapter = timerAdapter
    }


}