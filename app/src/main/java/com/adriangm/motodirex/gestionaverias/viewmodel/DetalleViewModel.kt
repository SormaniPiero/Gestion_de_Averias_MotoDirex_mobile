package com.adriangm.motodirex.gestionaverias.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adriangm.motodirex.gestionaverias.data.FakeDataSource
import com.adriangm.motodirex.gestionaverias.model.Averia
import com.adriangm.motodirex.gestionaverias.model.EstadoAveria
import com.adriangm.motodirex.gestionaverias.utils.DateUtils
import com.adriangm.motodirex.gestionaverias.model.EstadoMaquinaria

/**
 * ViewModel del detalle de avería.
 * Gestiona todas las acciones que puede realizar el técnico:
 * aceptar, registrar intervención, cambiar estado y finalizar.
 *
 * ⚠️ TODO FASE 2: Sustituir FakeDataSource por llamadas Retrofit
 */
class DetalleViewModel : ViewModel() {

    private val _averia = MutableLiveData<Averia>()
    val averia: LiveData<Averia> = _averia

    // Eventos de una sola vez (para mostrar mensajes Snackbar)
    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    private val _estadoMaquinaConfirmado = MutableLiveData<Boolean>(false)
    val estadoMaquinaConfirmado: LiveData<Boolean> = _estadoMaquinaConfirmado

    fun confirmarEstadoMaquina(confirmado: Boolean) {
        _estadoMaquinaConfirmado.value = confirmado
    }

    /**
     * Carga la avería por su ID desde FakeDataSource
     */
    fun cargarAveria(id: Int) {
        if (_averia.value?.codigoAveria != id) {
            _estadoMaquinaConfirmado.value = false
            FakeDataSource.getAveriaPorId(id)?.let { encontrada ->
                _averia.value = encontrada
            }
        }
    }

    /**
     * CU03 - Aceptar avería
     * Registra la fecha de aceptación y cambia el estado a ACEPTADA
     */
    fun aceptarAveria() {
        val av = _averia.value ?: return

        av.fechaAcepTecnico = DateUtils.ahora()
        av.estadoAveria     = EstadoAveria.ACEPTADA

        // Notificar el cambio
        _averia.value = av
        _mensaje.value = "Avería aceptada correctamente"
    }

    /**
     * CU04 - Registrar intervención
     * Guarda la descripción del trabajo realizado
     */
    fun registrarIntervencion(descripcion: String) {
        val av = _averia.value ?: return

        av.procRealizadoTecnico = descripcion

        _averia.value = av
        _mensaje.value = "Intervención registrada correctamente"
    }

    /**
     * CU05 - Cambiar estado de maquinaria
     * Actualiza el estado de la máquina asociada a la avería
     */
    fun cambiarEstadoMaquinaria(nuevoEstado: EstadoMaquinaria) {
        val av = _averia.value ?: return

        av.maquinaria.codigoEstadoFK = nuevoEstado
        _estadoMaquinaConfirmado.value = true

        _averia.value = av
        _mensaje.value = "Estado de máquina actualizado correctamente"
    }

    /**
     * CU06 - Finalizar avería
     * Solo se puede finalizar si existe una intervención documentada
     */
    fun finalizarAveria() {
        val av = _averia.value ?: return

        if (av.procRealizadoTecnico.isNullOrBlank()) {
            _mensaje.value = "Debes registrar una intervención antes de finalizar"
            return
        }

        if (_estadoMaquinaConfirmado.value != true) {
            _mensaje.value = "Debes confirmar el estado de la máquina en 'Cambiar Estado Máquina'"
            return
        }

        av.fechaFinalizTecnico = DateUtils.ahora()
        av.estadoAveria        = EstadoAveria.FINALIZADA

        _averia.value = av
        _mensaje.value = "Avería finalizada correctamente"
    }

    /**
     * Limpia el mensaje después de mostrarlo
     */
    fun mensajeMostrado() {
        _mensaje.value = null
    }
}