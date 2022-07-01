package com.example.maintask.model.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginRepository (private val application: Application) {
    val userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Funcao que faz o login
    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(application.mainExecutor) { task ->
                if( task.isSuccessful ) {
                    userMutableLiveData.postValue(firebaseAuth.currentUser)
                }
                else {
                    Toast.makeText(application, "Email, ou senha, inválido", Toast.LENGTH_LONG).show()
                }
            }
    }
}

