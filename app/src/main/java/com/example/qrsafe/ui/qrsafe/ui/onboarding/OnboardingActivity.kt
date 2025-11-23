package com.example.qrsafe.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.qrsafe.MainActivity
import com.example.qrsafe.R
import com.example.qrsafe.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplica animația de pulsare buton
        val pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse)
        binding.startButton.startAnimation(pulseAnim)

        // Navighează spre MainActivity cu o tranziție
        binding.startButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }
    }
}
