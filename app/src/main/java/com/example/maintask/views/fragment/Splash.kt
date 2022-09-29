package com.example.maintask.views.fragment

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.maintask.R
import com.example.maintask.model.database.entity.ActionEntity
import com.example.maintask.model.database.entity.TaskEntity
import com.example.maintask.utils.NetworkChecker
import com.example.maintask.viewmodel.SplashViewModel
import com.example.maintask.views.activity.LoginActivity

class Splash : AppCompatActivity() {
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private val splashViewModel: SplashViewModel by viewModels()
    private val networkChecker by lazy {
        NetworkChecker(
            ContextCompat.getSystemService(
                this,
                ConnectivityManager::class.java
            )
        )
    }

    override fun onStart() {
        super.onStart()
        splashViewModel.deleteDatabaseData()
        verifyInternetAndLaunchData()
    }

    private fun verifyInternetAndLaunchData() {
        networkChecker.performActionIfConnected(this) {
            splashViewModel.launchFirebaseData()
            observerIfDataWasLaunched()
        }
    }

    private fun observerIfDataWasLaunched() {
        splashViewModel.observerIfTaskWasLaunched(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initializeViews()
        setAnimation()
        setAnimationListener()
        observerAnimationHasEnded()
        observerTaskActionListAndInsert()
    }

    private fun initializeViews() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        imageView = findViewById(R.id.image_splash)
        titleTextView = findViewById(R.id.text_splash)
    }

    private fun setAnimation() {
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        imageView.animation = topAnimation
        titleTextView.animation = bottomAnimation
    }

    private fun setAnimationListener() {
        topAnimation.setAnimationListener( object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                splashViewModel.setHasAnimationEnded(false)
            }
            override fun onAnimationEnd(p0: Animation?) {
                splashViewModel.setHasAnimationEnded(true)
            }
            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }

    private fun observerAnimationHasEnded() {
        splashViewModel.hasAnimationEnded.observe(this) { hasEnded ->
            if (hasEnded) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun observerTaskActionListAndInsert() {
        splashViewModel.taskActionList.observe(this) { list ->
            if( !list.isNullOrEmpty() ) {
                for (taskAction in list)
                    insertTaskDataToDatabase(taskAction)
            }
        }
    }

    private fun insertTaskDataToDatabase(taskAction: Pair<TaskEntity, List<ActionEntity>>) {
        val taskEntity = taskAction.first
        val actionList = taskAction.second
        val relationEntity = splashViewModel
            .getTaskActionRelationEntity(taskEntity, actionList)
        splashViewModel.insertData(taskEntity, actionList, relationEntity)
    }
}