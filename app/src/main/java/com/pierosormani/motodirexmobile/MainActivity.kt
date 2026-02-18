package com.pierosormani.motodirexmobile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pierosormani.motodirexmobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val toast = Toast.makeText(this, "No hay averías finalizadas todavía", Toast.LENGTH_SHORT)

        setContentView(binding.root)

        binding.fin.setOnClickListener {
            toast.show()
        }
    }
}