package com.example.qrsafe.ui.qrsafe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qrsafe.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

// Importurile necesare
import com.example.qrsafe.ui.qrsafe.RegisterActivity
import com.example.qrsafe.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ---------------------------
        // 🔐 AUTO-LOGIN (skip login)
        // ---------------------------
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // ---------------------------
        // ⬇️ Setup UI
        // ---------------------------
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login button
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completează email și parolă", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message ?: "Eroare la login", Toast.LENGTH_LONG).show()
                }
        }

        // Navigare către înregistrare
        binding.goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
