package com.example.maintask.model.task

import android.widget.TextView
import com.example.maintask.model.stopwatch.Stopwatch

data class TaskActionModel(
    val id: Int,
    val action: String,
    val order: Int,
    var time: String = "00:00:00"
){

    var stopwatch = Stopwatch(time)
        private set

    fun startStopwatch(textView: TextView) = stopwatch.startAndWriteOnTextView(textView)

    fun pauseStopwatch(){
        stopwatch.pause()
        time = elapsedTime()
    }

    fun resetStopwatch() {
        stopwatch.stop()
        time = elapsedTime()
    }

    fun elapsedTime() = stopwatch.getElapsedTime()

    fun isStopwatchRunning() = stopwatch.isRunning()
}
