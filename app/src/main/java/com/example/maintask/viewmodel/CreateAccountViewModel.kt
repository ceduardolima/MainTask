package com.example.maintask.viewmodel

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.maintask.model.repository.CreateAccountRepository

class CreateAccountViewModel(application: Application) : AndroidViewModel(application) {
    private val createAccountRepository = CreateAccountRepository(application)
    val userMutableLiveData = createAccountRepository.userMutableLiveData

    fun register(name: String, email: String, password: String, confirmPwd: String, path: Uri?) {

        when {
            (email.isEmpty() || password.isEmpty() || name.isEmpty()) -> Toast.makeText(getApplication(), "Todos os campos devem ser preenchidos", Toast.LENGTH_LONG).show()

            (password != confirmPwd) -> Toast.makeText(getApplication(), "Senhas diferentes", Toast.LENGTH_LONG).show()

            (password.length < 6) -> Toast.makeText(getApplication(), "A senha precisa ter no mínimo 6 caracteres", Toast.LENGTH_LONG).show()

            else -> createAccountRepository.register(name.trim(), email.trim(), password.trim(), path)
        }
    }

    interface Callbacks {
        fun verifyGalleryPermition()
        var selectedPhotoPath: Uri?
    }
}