package com.example.maintask.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.maintask.R

class NavbarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        perfilBt.setOnClickListener { findNavController().navigate(R.id.action_global_perfil)}
        hometBt.setOnClickListener { findNavController().navigate(R.id.action_global_home)}

        return view
    }


}