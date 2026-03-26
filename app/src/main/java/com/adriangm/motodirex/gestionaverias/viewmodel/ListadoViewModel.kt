package com.adriangm.motodirex.gestionaverias.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adriangm.motodirex.gestionaverias.data.AveriaRepository
import com.adriangm.motodirex.gestionaverias.data.network.dto.AveriaDto
import com.adriangm.motodirex.gestionaverias.utils.SessionManager
import kotlinx.coroutines.launch

class ListadoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AveriaRepository()

    private val _averias = MutableLiveData<List<AveriaDto>>()
    val averias: LiveData<List<AveriaDto>> = _averias

    private val _tabActivo = MutableLiveData<Int>(0)
    val tabActivo: LiveData<Int> = _tabActivo

    private val _conteoNuevas = MutableLiveData<Int>(0)
    val conteoNuevas: LiveData<Int> = _conteoNuevas

    private val _conteoRecibidas = MutableLiveData<Int>(0)
    val conteoRecibidas: LiveData<Int> = _conteoRecibidas

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var todasLasAverias: List<AveriaDto> = emptyList()
    private var textoBusqueda: String = ""

    init {
        cargarAverias()
        iniciarRefrescoAutomatico()

    }

    private fun iniciarRefrescoAutomatico() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(10_000) // 10 segundos
                cargarAverias()
            }
        }
    }
    fun cargarAverias() {
        val token = SessionManager.getToken(getApplication()) ?: return

        _cargando.value = true

        viewModelScope.launch {
            val resultado = repository.getAverias(token)
            _cargando.value = false

            resultado.fold(
                onSuccess = { lista ->
                    todasLasAverias = lista
                    _conteoNuevas.value = lista.count{it.estado == "ASIGNADA"}
                    _conteoRecibidas.value = lista.count{it.estado == "EN_PROCESO"}
                    if(_tabActivo.value == 1) {
                        cargarAveriasRecibidas()
                    }else{
                        cargarAveriasNuevas()
                    }
                },
                onFailure = { error ->
                    _error.value = "Error al cargar averías: ${error.message}"
                }
            )
        }
    }

    fun cargarAveriasNuevas() {
        _tabActivo.value = 0
        textoBusqueda = ""
        aplicarFiltro(todasLasAverias.filter { it.estado == "ASIGNADA" })
    }

    fun cargarAveriasRecibidas() {
        _tabActivo.value = 1
        textoBusqueda = ""
        aplicarFiltro(todasLasAverias.filter { it.estado == "EN_PROCESO" })
    }

    fun buscar(texto: String) {
        textoBusqueda = texto.trim().lowercase()
        val listaBase = when (_tabActivo.value) {
            0 -> todasLasAverias.filter { it.estado == "ASIGNADA" }
            1 -> todasLasAverias.filter { it.estado == "EN_PROCESO" }
            else -> todasLasAverias
        }
        aplicarFiltro(listaBase)
    }

    private fun aplicarFiltro(lista: List<AveriaDto>) {
        if (textoBusqueda.isEmpty()) {
            _averias.value = lista
            return
        }
        _averias.value = lista.filter { averia ->
            averia.descripcion.lowercase().contains(textoBusqueda) ||
                    averia.codigoAveria.toString().contains(textoBusqueda)
        }
    }

    fun getContadorNuevas(): Int = todasLasAverias.count { it.estado == "ASIGNADA" }
    fun getContadorRecibidas(): Int = todasLasAverias.count { it.estado == "EN_PROCESO" }

    fun errorMostrado() { _error.value = null }
}