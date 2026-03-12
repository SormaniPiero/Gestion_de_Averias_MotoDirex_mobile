package com.adriangm.motodirex.gestionaverias.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adriangm.motodirex.gestionaverias.data.FakeDataSource

/**
 * ViewModel del Login.
 * Contiene toda la lógica: validar campos, verificar usuario, etc.
 * La Activity solo observa el resultado y reacciona.
 *
 * ⚠️ TODO FASE 2: Sustituir FakeDataSource.login() por llamada Retrofit
 */

class LoginViewModel : ViewModel() {

    enum class ResultadoLogin {
        EXITO,
        ERROR_CAMPOS_VACIOS,
        ERROR_CREDENCIALES,
        ERROR_INACTIVO
    }

    // MutableLiveData es privado (solo el ViewModel escribe)
    // LiveData es público (la Activity solo lee)

    private val _loginResultado = MutableLiveData<ResultadoLogin>()
    val loginResultado: LiveData<ResultadoLogin> = _loginResultado

    fun login(email: String, password: String) {

        // 1. Validar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            _loginResultado.value = ResultadoLogin.ERROR_CAMPOS_VACIOS
            return
        }

        // 2. Buscar el usuario en FakeDataSource
        val usuario = FakeDataSource.login(email, password)

        // 3. Evaluar el resultado
        when {
            usuario == null -> {
                _loginResultado.value = ResultadoLogin.ERROR_CREDENCIALES
            }
            !usuario.activo -> {
                _loginResultado.value = ResultadoLogin.ERROR_INACTIVO
            }
            else -> {
                _loginResultado.value = ResultadoLogin.EXITO
            }
        }
    }
}