package com.example.maintask.model.stopwatch

abstract class StopwatchCalculator {
    var currentState: StopwatchState = StopwatchState.Paused(INIT_TIME)

    companion object {
        const val INIT_TIME: Long = 0
    }

    fun calculateRunningState() {
        when (currentState) {
            is StopwatchState.Running -> currentState
            is StopwatchState.Paused -> {
                currentState = StopwatchState.Running(
                    startTime = getSeconds(),
                    elapsedTime = (currentState as StopwatchState.Paused).elapsedTime
                )
            }
        }
    }

    fun calculatePausedState() {
        when (currentState) {
            is StopwatchState.Running -> {
                val elapsedTime = elapsedTimeCalculate(currentState as StopwatchState.Running)
                currentState = StopwatchState.Paused(elapsedTime = elapsedTime)
            }
            is StopwatchState.Paused -> currentState
        }
    }

    fun elapsedTimeCalculate(state: StopwatchState.Running): Long {
        val currentTimestamp = getSeconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            INIT_TIME
        }
        return timePassedSinceStart + state.elapsedTime
    }

    private fun getSeconds(): Long {
        return System.currentTimeMillis()/1000
    }
}