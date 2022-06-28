package com.example.maintask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.maintask.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var emailEditText: TextInputLayout
    private lateinit var passwordEditText: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var loginViewModel: LoginViewModel

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

        emailEditText = view.findViewById(R.id.login_textedit_user)
        passwordEditText = view.findViewById(R.id.login_textedit_pwd)
        loginButton = view.findViewById(R.id.login_bt)

        loginButton.setOnClickListener {
            val email = emailEditText.editText?.text.toString()
            val password = passwordEditText.editText?.text.toString()

            loginViewModel.login(email, password)
        }

        return view
    }
}
