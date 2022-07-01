package com.example.maintask.views.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.maintask.R
import com.example.maintask.viewmodel.CreateAccountViewModel
import com.google.android.material.textfield.TextInputLayout

class CreateAccountFragment : Fragment() {
    private lateinit var createAccountViewModel: CreateAccountViewModel
    private var callbacks: CreateAccountViewModel.Callbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAccountViewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)
        createAccountViewModel.userMutableLiveData.observe(this) { firebaseUser ->
            if( firebaseUser != null) {
                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
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
        val imgPhoto = view.findViewById<ImageView>(R.id.create_account_photo)
        val imgPhotoBt = view.findViewById<ImageButton>(R.id.create_account_add_photo_bt)

        imgPhotoBt.setOnClickListener { callbacks?.verifyGalleryPermition() }

        return view
    }
}