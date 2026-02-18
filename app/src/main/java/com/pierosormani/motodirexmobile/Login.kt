package com.pierosormani.motodirexmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pierosormani.motodirexmobile.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
//Prueba para git
        val texto = "Bienvenido"

        val duracion = Toast.LENGTH_SHORT

        val toast = Toast.makeText(this, texto, duracion)


        binding.btnAcceder.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)

            toast.show()

            startActivity(intent)
        }
    }
}