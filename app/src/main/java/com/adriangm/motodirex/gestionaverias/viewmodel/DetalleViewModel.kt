package com.adriangm.motodirex.gestionaverias.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adriangm.motodirex.gestionaverias.data.AveriaRepository
import com.adriangm.motodirex.gestionaverias.data.network.dto.AveriaDto
import com.adriangm.motodirex.gestionaverias.utils.SessionManager
import kotlinx.coroutines.launch

class DetalleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AveriaRepository()

    private val _averia = MutableLiveData<AveriaDto>()
    val averia: LiveData<AveriaDto> = _averia

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    private val _estadoMaquinaConfirmado = MutableLiveData<Boolean>(false)
    val estadoMaquinaConfirmado: LiveData<Boolean> = _estadoMaquinaConfirmado

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    fun confirmarEstadoMaquina(confirmado: Boolean) {
        _estadoMaquinaConfirmado.value = confirmado
    }

    fun cargarAveria(id: Int) {
        val token = SessionManager.getToken(getApplication()) ?: return
        if (_averia.value?.codigoAveria == id) return

        _estadoMaquinaConfirmado.value = false
        _cargando.value = true

        viewModelScope.launch {
            val resultado = repository.getAverias(token)
            _cargando.value = false
            resultado.fold(
                onSuccess = { lista ->
                    lista.find { it.codigoAveria == id }?.let { encontrada ->
                        _averia.value = encontrada
                    }

                },
                onFailure = { _mensaje.value = "Error al cargar la avería" }
            )

        }
    }

    fun aceptarAveria() {
        val av = _averia.value ?: return
        val token = SessionManager.getToken(getApplication()) ?: return

        viewModelScope.launch {
            val resultado = repository.aceptarAveria(token, av.codigoAveria)
            resultado.fold(
                onSuccess = {
                    _averia.value = av.copy(estado = "EN_PROCESO")
                    _mensaje.value = "Avería aceptada correctamente"
                },
                onFailure = { _mensaje.value = "Error al aceptar la avería" }
            )
        }
    }

    fun registrarIntervencion(descripcion: String) {
        val av = _averia.value ?: return
        val token = SessionManager.getToken(getApplication()) ?: return

        viewModelScope.launch {
            val resultado = repository.registrarIntervencion(token, av.codigoAveria, descripcion)
            resultado.fold(
                onSuccess = {
                    _averia.value = av.copy()
                    _mensaje.value = "Intervención registrada correctamente"
                },
                onFailure = { _mensaje.value = "Error al registrar la intervención" }
            )
        }
    }

    fun cambiarEstadoMaquinaria(nuevoEstado: String) {
        val av = _averia.value ?: return
        val token = SessionManager.getToken(getApplication()) ?: return

        viewModelScope.launch {
            val resultado = repository.cambiarEstado(token, av.maquinariaFK, nuevoEstado)
            resultado.fold(
                onSuccess = {
                    _estadoMaquinaConfirmado.value = true
                    _mensaje.value = "Estado de máquina actualizado correctamente"
                },
                onFailure = { _mensaje.value = "Error al cambiar el estado" }
            )
        }
    }

    fun finalizarAveria() {
        val av = _averia.value ?: return
        val token = SessionManager.getToken(getApplication()) ?: return

        viewModelScope.launch {
            val resultado = repository.finalizarAveria(token, av.codigoAveria)
            resultado.fold(
                onSuccess = {
                    _averia.value = av.copy(estado = "FINALIZADA")
                    _mensaje.value = "Avería finalizada correctamente"
                },
                onFailure = { _mensaje.value = "Error al finalizar la avería" }
            )
        }
    }


    fun mensajeMostrado() { _mensaje.value = null }
}