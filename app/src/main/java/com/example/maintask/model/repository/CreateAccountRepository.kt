package com.example.maintask.model.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CreateAccountRepository(private val application: Application) {
    val userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Funcao que realiza o registro no firebase
    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(application.mainExecutor) { task ->
                if (task.isSuccessful) {
                    userMutableLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        application,
                        "Registration Failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
    }
}