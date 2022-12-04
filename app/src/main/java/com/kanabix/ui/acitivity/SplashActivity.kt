package com.kanabix.ui.acitivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.complyany.entity.permission.RequestPermission
import com.kanabix.databinding.ActivitySplashBinding
import com.kanabix.utils.SavedPrefManager
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)



        lifecycleScope.launchWhenCreated {
            delay(3000L)

            Intent(this@SplashActivity, RoleSelectScreen::class.java).also {
                startActivity(it)
                finish()
            }
        }


    }
}