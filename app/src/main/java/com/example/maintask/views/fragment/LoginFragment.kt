package com.example.maintask.views.fragment

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.utils.NetworkChecker
import com.example.maintask.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
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

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.userMutableLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                Toast.makeText(context, "Login feito com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailEditText = view.findViewById<TextInputLayout>(R.id.login_textedit_user)
        val passwordEditText = view.findViewById<TextInputLayout>(R.id.login_textedit_pwd)
        val loginButton = view.findViewById<Button>(R.id.login_bt)
        val createAccountBt = view.findViewById<TextView>(R.id.login_create_account_bt)

        loginButton.setOnClickListener {
            val email = emailEditText.editText?.text.toString()
            val password = passwordEditText.editText?.text.toString()
            // Verifica se ha conexao com a internet
            networkChecker.performActionIfConnected(context) {
                loginViewModel.login(email, password)
            }
        }

        createAccountBt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }

        return view
    }
}
