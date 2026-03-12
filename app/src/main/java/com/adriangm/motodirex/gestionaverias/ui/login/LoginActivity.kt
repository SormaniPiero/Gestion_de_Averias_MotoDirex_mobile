package com.adriangm.motodirex.gestionaverias.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.adriangm.motodirex.gestionaverias.R
import com.adriangm.motodirex.gestionaverias.databinding.ActivityLoginBinding
import com.adriangm.motodirex.gestionaverias.ui.main.MainActivity
import com.adriangm.motodirex.gestionaverias.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    // ViewBinding: forma moderna de acceder a los elementos del XML
    // sin usar findViewById
    private lateinit var binding: ActivityLoginBinding

    // El ViewModel contiene toda la lógica, la Activity solo muestra
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout con ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear el ViewModel
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        configurarBotones()
        observarViewModel()
    }

    private fun configurarBotones() {
        binding.btnLogin.setOnClickListener {
            val email    = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Ocultar error anterior
            binding.tvError.visibility = View.GONE

            // Pedir al ViewModel que procese el login
            viewModel.login(email, password)
        }
    }

    private fun observarViewModel() {
        viewModel.loginResultado.observe(this) { resultado ->
            when (resultado) {
                LoginViewModel.ResultadoLogin.EXITO -> {
                    // Login correcto → ir a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                LoginViewModel.ResultadoLogin.ERROR_CREDENCIALES -> {
                    mostrarError(getString(R.string.login_error_credenciales))
                }
                LoginViewModel.ResultadoLogin.ERROR_INACTIVO -> {
                    mostrarError(getString(R.string.login_error_inactivo))
                }
                LoginViewModel.ResultadoLogin.ERROR_CAMPOS_VACIOS -> {
                    mostrarError(getString(R.string.login_error_campos))
                }
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        binding.tvError.text = mensaje
        binding.tvError.visibility = View.VISIBLE
    }
}