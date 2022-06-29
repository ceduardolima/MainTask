package com.example.maintask.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.maintask.R

class LoginActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        // Armazena o navHost
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_login_navHostFragment) as NavHostFragment
        // Define o navController a partir do navHost
        navController = navHostFragment.navController
    }
}