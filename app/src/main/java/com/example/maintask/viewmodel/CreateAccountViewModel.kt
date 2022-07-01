package com.example.maintask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.maintask.model.repository.CreateAccountRepository

class CreateAccountViewModel(application: Application) : AndroidViewModel(application) {
    private val createAccountRepository = CreateAccountRepository(application)
    val userMutableLiveData = createAccountRepository.userMutableLiveData

    fun register(email: String, password: String) {
        createAccountRepository.register(email,password)
    }

    interface Callbacks {
        fun verifyGalleryPermition()
    }
}