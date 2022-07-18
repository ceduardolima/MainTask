package com.example.maintask.views.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.maintask.R
import com.example.maintask.views.activity.LoginActivity

class Splash : AppCompatActivity() {

    val SPLASH_SCREEN = 2700

    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation

    private lateinit var imagemView: ImageView
    private lateinit var title_txt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)


        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        imagemView = findViewById(R.id.image_splash)
        title_txt = findViewById(R.id.text_splash)

        imagemView.animation = topAnimation
        title_txt.animation = bottomAnimation

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN.toLong())
    }
}