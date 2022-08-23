package com.example.maintask.model.stopwatch

class StopwatchStateHolder(private val initTime: Long, private val initTimeStr: String): StopwatchCalculator(initTime) {
    companion object {
        const val DEFAULT_TIME = "00:00:00"
    }

    fun start() {
        calculateRunningState()
    }

    fun pause() {
        calculatePausedState()
    }

    fun stop() {
        currentState = StopwatchState.Paused(initTime)
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Running -> elapsedTimeCalculate(currentState)
        }

        return format(elapsedTime)
    }

    private fun format(timestamp: Long): String {
        val secondsFormatted = (timestamp % 60).pad(2)
        val minutes = timestamp / 60
        val minutesFormatted = (minutes % 60).pad(2)
        val hoursFormatted = (minutes / 60).pad(2)
        return when {
            (timestamp > 0) -> "$hoursFormatted:$minutesFormatted:$secondsFormatted"
            (timestamp.compareTo(0) == 0) -> DEFAULT_TIME
            else -> initTimeStr
            }
        }


    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')

    fun isRunning(): Boolean =
        when(currentState){
            is StopwatchState.Running -> true
            is StopwatchState.Paused -> false
        }
}