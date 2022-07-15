package com.example.maintask.views.activity

import android.app.Notification
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.maintask.R
import com.example.maintask.viewmodel.NavbarViewModel
import com.example.maintask.views.fragment.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity() : AppCompatActivity(), NavbarViewModel.Callbacks{
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override lateinit var toolbarTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        // Define o navController a partir do navHost
        navController = navHostFragment.navController

        toolbarTitle = findViewById(R.id.toolbar_menu_title)
    }
}