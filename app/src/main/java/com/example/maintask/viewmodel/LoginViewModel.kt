package com.example.maintask.viewmodel
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.maintask.model.repository.LoginRepository
import com.example.maintask.utils.NetworkChecker
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository = LoginRepository(application)
    val userMutableLiveData = loginRepository.userMutableLiveData

    fun login(email: String, password: String) {
        when {
            (email.isEmpty() || password.isEmpty()) -> Toast.makeText(getApplication(), "Todos os campos devem ser preenchidos", Toast.LENGTH_LONG).show()

            else -> loginRepository.login(email.trim(), password.trim())
        }
    }

    interface Callbacks {
        val networkChecker: NetworkChecker
    }

}