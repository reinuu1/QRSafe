package com.example.qrsafe.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.qrsafe.R
import com.example.qrsafe.databinding.ActivityOnboardingBinding
import com.example.qrsafe.ui.qrsafe.LoginActivity
import com.example.qrsafe.MainActivity
import com.google.firebase.auth.FirebaseAuth

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SKIP dacă user-ul este logat
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
        binding.startButton.startAnimation(pulse)

        binding.startButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
