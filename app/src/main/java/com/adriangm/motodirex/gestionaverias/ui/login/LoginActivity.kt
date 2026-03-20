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

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        configurarBotones()
        observarViewModel()
    }

    private fun configurarBotones() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            binding.tvError.visibility = View.GONE
            viewModel.login(username, password)
        }
    }

    private fun observarViewModel() {

        viewModel.cargando.observe(this) { cargando ->
            binding.btnLogin.isEnabled = !cargando
            // Si tienes un ProgressBar en el layout puedes mostrarlo aquí:
            // binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
        }

        viewModel.loginResultado.observe(this) { resultado ->
            when (resultado) {
                LoginViewModel.ResultadoLogin.EXITO -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                LoginViewModel.ResultadoLogin.ERROR_CREDENCIALES -> {
                    mostrarError(getString(R.string.login_error_credenciales))
                }
                LoginViewModel.ResultadoLogin.ERROR_CAMPOS_VACIOS -> {
                    mostrarError(getString(R.string.login_error_campos))
                }
                LoginViewModel.ResultadoLogin.ERROR_RED -> {
                    mostrarError("No se pudo conectar con el servidor. Comprueba tu conexión.")
                }
                null -> { }
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        binding.tvError.text = mensaje
        binding.tvError.visibility = View.VISIBLE
    }
}