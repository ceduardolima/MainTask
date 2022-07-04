package com.example.maintask.viewmodel
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.maintask.model.repository.LoginRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository = LoginRepository(application)
    val userMutableLiveData = loginRepository.userMutableLiveData

    fun login(email: String, password: String, action: () -> Unit) {
        when {
            (email.isEmpty() || password.isEmpty()) -> Toast.makeText(getApplication(), "Todos os campos devem ser preenchidos", Toast.LENGTH_LONG).show()

            else -> loginRepository.login(email.trim(), password.trim()) { action() }
        }
    }

    fun keepLogin(action: () -> Unit) {
        loginRepository.keepLogin { action() }
    }
}