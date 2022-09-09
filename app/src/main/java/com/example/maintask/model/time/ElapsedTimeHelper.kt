package com.example.maintask.model.time

class ElapsedTimeHelper(private val time: String) {
    fun getSeconds() = time.split(":")[2].toInt()

    fun getMinutes() = time.split(":")[1].toInt()

    fun getHours() = time.split(":")[0].toInt()
}