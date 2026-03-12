package com.adriangm.motodirex.gestionaverias.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adriangm.motodirex.gestionaverias.data.FakeDataSource
import com.adriangm.motodirex.gestionaverias.model.Averia
import com.adriangm.motodirex.gestionaverias.model.EstadoAveria
import com.adriangm.motodirex.gestionaverias.utils.DateUtils

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

    /**
     * Carga la avería por su ID desde FakeDataSource
     */
    fun cargarAveria(id: Int) {
        val encontrada = FakeDataSource.getAveriaPorId(id)
        if (encontrada != null) {
            _averia.value = encontrada!!
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
     * CU06 - Finalizar avería
     * Solo se puede finalizar si existe una intervención documentada
     */
    fun finalizarAveria() {
        val av = _averia.value ?: return

        if (av.procRealizadoTecnico.isNullOrBlank()) {
            _mensaje.value = "Debes registrar una intervención antes de finalizar"
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