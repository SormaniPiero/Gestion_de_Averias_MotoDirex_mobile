package com.adriangm.motodirex.gestionaverias.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adriangm.motodirex.gestionaverias.data.AveriaRepository
import com.adriangm.motodirex.gestionaverias.utils.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    enum class ResultadoLogin {
        EXITO,
        ERROR_CAMPOS_VACIOS,
        ERROR_CREDENCIALES,
        ERROR_RED
    }

    private val repository = AveriaRepository()

    private val _loginResultado = MutableLiveData<ResultadoLogin>()
    val loginResultado: LiveData<ResultadoLogin> = _loginResultado

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    fun login(username: String, password: String) {

        if (username.isEmpty() || password.isEmpty()) {
            _loginResultado.value = ResultadoLogin.ERROR_CAMPOS_VACIOS
            return
        }

        _cargando.value = true

        viewModelScope.launch {
            val resultado = repository.login(username, password)

            _cargando.value = false

            resultado.fold(
                onSuccess = { response ->
                    SessionManager.guardarSesion(
                        context = getApplication(),
                        token   = response.token,
                        nombre  = response.nombre,
                        id      = response.id
                    )
                    _loginResultado.value = ResultadoLogin.EXITO
                },
                onFailure = { error ->
                    if (error.message?.contains("401") == true ||
                        error.message?.contains("403") == true) {
                        _loginResultado.value = ResultadoLogin.ERROR_CREDENCIALES
                    } else {
                        _loginResultado.value = ResultadoLogin.ERROR_RED
                    }
                }
            )
        }
    }
}