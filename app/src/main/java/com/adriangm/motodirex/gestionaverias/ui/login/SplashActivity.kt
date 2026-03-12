package com.adriangm.motodirex.gestionaverias.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.adriangm.motodirex.gestionaverias.databinding.ActivitySplashBinding

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

        // Ocultar la ActionBar en el Splash
        supportActionBar?.hide()

        // Esperar 2.5 segundos y navegar al Login
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Cerramos el Splash para que no pueda volver atrás
        }, 2500)
    }
}