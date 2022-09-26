package com.example.maintask.views.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.maintask.R
import com.example.maintask.model.database.application.RoomApplication
import com.example.maintask.model.database.entity.EmployeeEntity
import com.example.maintask.model.database.entity.TeamMemberEntity
import com.example.maintask.model.task.TaskModel
import com.example.maintask.viewmodel.RoomViewModel
import com.example.maintask.viewmodel.RoomViewModelFactory
import com.example.maintask.viewmodel.TaskViewModel
import com.example.maintask.views.activity.LoginActivity

class Splash : AppCompatActivity() {

    val SPLASH_SCREEN = 2700

    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var imagemView: ImageView
    private lateinit var title_txt: TextView

    private val roomViewModel: RoomViewModel by viewModels {
        val roomApplication = (application as RoomApplication)
        RoomViewModelFactory(
            roomApplication.taskRepository,
            roomApplication.actionRepository,
            roomApplication.taskActionRepository,
            roomApplication.teamMemberRepository,
            roomApplication.employeeRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        imagemView = findViewById(R.id.image_splash)
        title_txt = findViewById(R.id.text_splash)

        imagemView.animation = topAnimation
        title_txt.animation = bottomAnimation
        initializeDatabase(taskViewModel.getTaskList()[0])
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN.toLong())
    }

    private fun initializeDatabase(task: TaskModel){
        val taskEntity = taskViewModel.getTaskEntity(listOf(task))
        val actionEntity = taskViewModel.getActionEntity(task.actions)
        val relationEntity = taskViewModel.getTaskActionRelationEntity(taskEntity, actionEntity)
        val team = mutableListOf<EmployeeEntity>()
        team.add(EmployeeEntity(0, "Carlos Eduardo", ""))
        team.add(EmployeeEntity(1, "Davi Pereira", ""))
        team.add(EmployeeEntity(2, "Heraldo", ""))
        roomViewModel.populateDatabase(taskEntity, actionEntity, relationEntity, team)
    }
}