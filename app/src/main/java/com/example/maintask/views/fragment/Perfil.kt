package com.example.maintask.views.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.maintask.R
import com.example.maintask.databinding.FragmentPerfilBinding
import com.google.android.material.textfield.TextInputLayout


class Perfil : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val emailPerfilEditText = view.findViewById<TextInputLayout>(R.id.email_perfil_edit)
        val nomeEditText = view.findViewById<TextInputLayout>(R.id.nome_perfil_edit)
        val cargoEditText = view.findViewById<TextInputLayout>(R.id.cargo_perfil_edit)
        val cidadeEditText = view.findViewById<TextInputLayout>(R.id.cidade_perfil_edit)
        val salvarButton = view.findViewById<Button>(R.id.btn_perfil_salvar)

        salvarButton.setOnClickListener {
            salvarButton.isEnabled = false
            val email = emailPerfilEditText.editText?.text.toString()
            val password = nomeEditText.editText?.text.toString()
            val cargo = cargoEditText.editText?.text.toString()
            val cidade = cidadeEditText.editText?.text.toString()

        }




