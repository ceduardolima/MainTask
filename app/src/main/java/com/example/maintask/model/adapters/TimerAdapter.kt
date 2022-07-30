package com.example.maintask.model.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maintask.R

class TimerAdapter(
    val context: Context,
    val actions: MutableList<String>
    ) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

        inner class TimerViewHolder(
            val timerRecyclerView: View
            ): RecyclerView.ViewHolder(timerRecyclerView) {
            val action = timerRecyclerView.findViewById<TextView>(R.id.timer_item_action)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val timerRecyclerView = LayoutInflater.from(context).inflate(R.layout.timer_item, parent, false)
        return TimerViewHolder(timerRecyclerView)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.action.text = actions[position]
    }

    override fun getItemCount() = actions.size

}