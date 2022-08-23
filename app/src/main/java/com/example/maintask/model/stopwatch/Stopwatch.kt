package com.example.maintask.model.stopwatch

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow

class Stopwatch(private val initTime: String) {
    private val initTimeLong = getInitialTime()
    private val stopwatchStateHolder = StopwatchStateHolder(initTimeLong, initTime)
    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(initTime)
    private val scope = CoroutineScope(Main)

    companion object {
        private const val JOB_DELAY: Long = 100
    }

    private fun getInitialTime(): Long {
        val array = initTime.split(":")
        val second = array[2].toLong()
        val minute = array[1].toLong() * 60
        val hour = array[0].toLong() * 3600
        return (second + minute + hour)
    }

    fun startAndWriteOnTextView(textView: TextView) {
        if (job == null){
            startJobAndExecuteSomeFunc {
                textView.text = mutableTicker.value
            }
        }
        stopwatchStateHolder.start()
    }

    private fun startJobAndExecuteSomeFunc(function: () -> Unit) {
        scope.launch {
            while (isActive) {
                mutableTicker.value = stopwatchStateHolder.getStringTimeRepresentation()
                function()
                delay(JOB_DELAY)
            }
        }
    }

    fun pause() {
        stopwatchStateHolder.pause()
        stopJob()
    }

    fun stop() {
        stopwatchStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun clearValue() {
        mutableTicker.value = StopwatchStateHolder.DEFAULT_TIME
    }

    private fun initialValue() {
        mutableTicker.value = initTime
    }

    fun getElapsedTime() = mutableTicker.value

    fun isRunning(): Boolean = stopwatchStateHolder.isRunning()
}