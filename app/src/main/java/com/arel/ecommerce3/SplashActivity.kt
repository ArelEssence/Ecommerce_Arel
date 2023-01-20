package com.arel.ecommerce3

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import com.arel.ecommerce3.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    private lateinit var sph : SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sph = SharedPreference(this)

        setContentView(R.layout.activity_splash)
        val handler = Handler()

        setAnimation()

        handler.postDelayed({
            if (sph.getStatusLogin()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }

        }, 3000) //3000 L = 3 detik

    }

    private fun setAnimation() {
        val animation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.top_animation)
        artboard_6_.animation = animation

    }

}