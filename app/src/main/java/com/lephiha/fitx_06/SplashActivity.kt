package com.lephiha.fitx_06

import android.app.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Khởi tạo views
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvTagline = findViewById<TextView>(R.id.tvTagline)

        // Animation cho logo - fade in + scale
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        val scaleAnim = ScaleAnimation(
            0.8f, 1f, 0.8f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            fillAfter = true
        }

        val logoAnimationSet = AnimationSet(true).apply {
            addAnimation(fadeIn)
            addAnimation(scaleAnim)
        }

        ivLogo?.startAnimation(logoAnimationSet)

        // Animation cho tên app - fade in với delay 300ms
        val appNameFadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            startOffset = 300
            fillAfter = true
        }
        tvAppName?.startAnimation(appNameFadeIn)

        // Animation cho tagline - fade in với delay 600ms
        val taglineFadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            startOffset = 600
            fillAfter = true
        }
        tvTagline?.startAnimation(taglineFadeIn)

        // Chuyển sang màn Onboarding sau 2.5 giây
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2500)
    }
}

