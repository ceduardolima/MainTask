package com.example.maintask.model.task

import android.widget.TextView
import com.example.maintask.model.stopwatch.Stopwatch

data class TaskActionModel(
    val action: String,
    val order: Int
){
    var stopwatch = Stopwatch()
        private set
    var seconds = elapsedTime().split(":")[2].toInt()
        private set
    var minutes = elapsedTime().split(":")[1].toInt()
        private set

    fun startStopwatch(textView: TextView) = stopwatch.startAndWriteOnTextView(textView)

    fun pauseStopwatch() = stopwatch.pause()

    fun resetStopwatch() = stopwatch.stop()

    fun elapsedTime() = stopwatch.getElapsedTime()

    fun isStopwatchRunning() = stopwatch.isRunning()
}
