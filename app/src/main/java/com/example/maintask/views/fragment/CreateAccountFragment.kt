package com.example.maintask.views.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.utils.NetworkChecker
import com.example.maintask.viewmodel.CreateAccountViewModel
import com.google.android.material.textfield.TextInputLayout

class CreateAccountFragment : Fragment() {
    private lateinit var createAccountViewModel: CreateAccountViewModel
    private var callbacks: CreateAccountViewModel.Callbacks? = null
    private val networkChecker by lazy {
        NetworkChecker(
            ContextCompat.getSystemService(
                requireContext(),
                ConnectivityManager::class.java
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAccountViewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)
        createAccountViewModel.userMutableLiveData.observe(this) { firebaseUser ->
            if( firebaseUser != null) {
                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CreateAccountViewModel.Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_account, container, false)

        val nameTextEdit = view.findViewById<TextInputLayout>(R.id.create_account_textedit_name)
        val emailTextEdit = view.findViewById<TextInputLayout>(R.id.create_account_textedit_email)
        val pwdTextEdit = view.findViewById<TextInputLayout>(R.id.create_account_textedit_pwd)
        val pwdConfirmTextEdit = view.findViewById<TextInputLayout>(R.id.create_account_textedit_confirm)
        val imgPhotoBt = view.findViewById<ImageButton>(R.id.create_account_add_photo_bt)
        val registerBt = view.findViewById<Button>(R.id.create_account_register_bt)

        imgPhotoBt.setOnClickListener {
            imgPhotoBt.isEnabled = false
            callbacks?.verifyGalleryPermition()
            imgPhotoBt.isEnabled = true
        }

        registerBt.setOnClickListener {
            registerBt.isEnabled = false
            val email = emailTextEdit.editText?.text.toString()
            val password = pwdTextEdit.editText?.text.toString()
            val name = nameTextEdit.editText?.text.toString()
            val pwdConfirm = pwdConfirmTextEdit.editText?.text.toString()

            // Verifica se há conexao com a internet
            networkChecker.performActionIfConnected(requireContext()) {
                callbacks?.let { callback ->
                    createAccountViewModel.register(
                        name,
                        email,
                        password,
                        pwdConfirm,
                        callback.selectedPhotoPath
                    )
                }
            }
        }

        return view
    }
}