package com.example.maintask.model.stopwatch

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow

class Stopwatch {
    private val stopwatchStateHolder = StopwatchStateHolder()
    private var job: Job? = null
    private val mutableTicker = MutableStateFlow("00:00:00")
    private val scope = CoroutineScope(Main)

    companion object {
        private const val JOB_DELAY: Long = 100
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
            Log.i("stopwatch", "isActive: ${isActive}")
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
        mutableTicker.value = ""
    }

    fun getElipsedTime() = mutableTicker.value

    fun isRunning(): Boolean = stopwatchStateHolder.isRunning()
}