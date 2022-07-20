package com.example.maintask.views.fragment

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


class TimerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var callbacks: MainActivityCallbacks? = null
    private lateinit var timerRecyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as MainActivityCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val timerFragment = inflater.inflate(R.layout.fragment_timer, container, false)
        val titulo = timerFragment.findViewById<TextView>(R.id.timer_fragment_title)
        titulo.text = callbacks?.selectedTask?.title

        timerRecyclerView = timerFragment.findViewById(R.id.timer_action_recycler_view)
        timerRecyclerView.layoutManager = LinearLayoutManager(context)
        timerRecyclerView.hasFixedSize()

        val multableListOfActions = callbacks?.selectedTask?.actions?.split(", ")?.toMutableList()

        val timerAdapter = multableListOfActions?.let { TimerAdapter(requireContext(), it) }
        
        timerRecyclerView.adapter = timerAdapter
        return timerFragment
    }


}