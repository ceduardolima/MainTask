package com.example.maintask.views.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.viewmodel.CreateAccountViewModel
import com.example.maintask.viewmodel.LoginViewModel
import com.example.maintask.viewmodel.NavbarViewModel

class NavbarFragment : Fragment() {
    private lateinit var callbacks: MainActivityCallbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as MainActivityCallbacks
    }

    override fun onDetach() {
        super.onDetach()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_navbar, container, false)
        val hometBt = view.findViewById<ImageButton>(R.id.navbar_home)
        val perfilBt = view.findViewById<ImageButton>(R.id.navbar_perfil)
        val settingsBt = view.findViewById<ImageButton>(R.id.navbar_settings)
        val helpBt = view.findViewById<ImageButton>(R.id.navbar_help)

        // Faz a navegação global para as determinadas telas
        perfilBt.setOnClickListener {
            findNavController().navigate(R.id.action_global_perfil)
            callbacks.toolbarTitle.text = "Perfil"
        }
        hometBt.setOnClickListener {
            findNavController().navigate(R.id.action_global_home)
            callbacks.toolbarTitle.text = "Sua Agenda"
        }

        helpBt.setOnClickListener {
            findNavController().navigate(R.id.action_global_ajuda)
            callbacks.toolbarTitle.text = "Ajuda"
        }
        return view
    }


}