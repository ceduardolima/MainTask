package com.example.maintask.model.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.maintask.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CreateAccountRepository(private val application: Application) {
    val userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val emptyUri = Uri.parse("")

    // Funcao que realiza o registro no firebase
    fun register(name: String, email: String, password: String, path: Uri?) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(application.mainExecutor) { task ->
                if (task.isSuccessful) {

                    val filename = UUID.randomUUID().toString()

                    if(path != null)
                        saveUserInFireStore(name, filename, path)
                    else saveUserInFireStore(name, filename, emptyUri)

                    userMutableLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        application,
                        "Registration Failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener() { e ->
                e.message?.let { Log.i("teste", it) }
            }
    }

    private fun saveUserInFireStore(name: String, filename: String, path: Uri) {
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        // Envia a imagem para o firebase
        ref.putFile(path)
            .addOnCompleteListener {
                // Apos o upload, ele obtem o uri da imagem
                ref.downloadUrl
                    .addOnCompleteListener { uri ->
                        Log.i("teste", "path: ${uri.toString()}")
                        // Cria o modelo de usuario
                        val user = UserModel(
                            firebaseAuth.uid,
                            name,
                            firebaseAuth.currentUser?.email,
                            uri.toString()
                        )
                        // Após enviar e obter o uri da image, usa o firestore para armazenar os dados
                        FirebaseFirestore.getInstance().collection("users")
                            .add(user)
                            .addOnSuccessListener { document ->
                                Log.i("teste", "id do documento: ${document.id}")
                            }
                            .addOnFailureListener { e ->
                                e.message?.let { Log.i("teste", "Falha ao criar o usuário no") }
                            }
                    }
                    .addOnFailureListener() { e ->
                        Log.i("teste", "falha no download do uri: ${e.message}")
                    }
            }
            .addOnFailureListener() { e ->
                e.message?.let { Log.e("teste", "falha no envio: $it") }
            }
    }
}