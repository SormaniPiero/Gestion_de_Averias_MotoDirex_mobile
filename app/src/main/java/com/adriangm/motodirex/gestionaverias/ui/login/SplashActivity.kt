package com.adriangm.motodirex.gestionaverias.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.adriangm.motodirex.gestionaverias.databinding.ActivitySplashBinding
import android.view.animation.AnimationUtils

/**
 * Pantalla de presentación de MotoDirex.
 * Se muestra 2.5 segundos al arrancar la app y
 * luego navega automáticamente al Login.
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Animación de fade in en el contenido
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1000
        binding.root.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            // Animación de transición al Login
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2500)
    }
}