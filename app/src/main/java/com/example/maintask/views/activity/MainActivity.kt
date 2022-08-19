package com.example.maintask.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.maintask.R
import com.example.maintask.callbacks.MainActivityCallbacks
import com.example.maintask.model.task.TaskModel

class MainActivity() : AppCompatActivity(), MainActivityCallbacks{
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override lateinit var toolbarTitle: TextView
    override var selectedTask: TaskModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        toolbarTitle = findViewById(R.id.toolbar_menu_title)
    }
}