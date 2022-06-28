package com.example.maintask.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.maintask.model.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository = LoginRepository(application)
    val userMutableLiveData = loginRepository.userMutableLiveData

    fun login(email: String, password: String) {
        loginRepository.login(email, password)
    }

}