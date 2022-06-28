package com.example.maintask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CreateAcountViewModel(application: Application) : AndroidViewModel(application) {
    private val createAcountRepository = CreateAcountViewModel(application)

    fun register(email: String, password: String) {
        createAcountRepository.register(email,password)
    }
}