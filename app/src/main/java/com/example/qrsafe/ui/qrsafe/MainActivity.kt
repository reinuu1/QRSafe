package com.example.qrsafe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qrsafe.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.qrsafe.ui.qrsafe.SettingsDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)

        // click pe butonul Settings
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_settings -> {
                    SettingsDialog().show(supportFragmentManager, "settings")
                    false
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }
}
