package com.example.maintask.model.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository(private val application: Application) {
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _task = MutableLiveData<List<DocumentSnapshot>>()
    val task: LiveData<List<DocumentSnapshot>>
        get() = _task

    fun getTasks() {
        fireStore.collection("task")
            .get()
            .addOnSuccessListener { result ->
                _task.value = result.documents
            }
            .addOnFailureListener { _ ->
                Toast.makeText(
                    application,
                    "Erro ao consultar banco de dados.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}