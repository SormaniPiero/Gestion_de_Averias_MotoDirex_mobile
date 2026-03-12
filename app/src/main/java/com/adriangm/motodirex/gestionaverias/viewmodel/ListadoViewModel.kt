package com.adriangm.motodirex.gestionaverias.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adriangm.motodirex.gestionaverias.data.FakeDataSource
import com.adriangm.motodirex.gestionaverias.model.Averia

/**
 * ViewModel del listado de averías.
 * Gestiona qué pestaña está activa y qué averías mostrar.
 *
 * ⚠️ TODO FASE 2: Sustituir FakeDataSource por llamadas Retrofit
 */

class ListadoViewModel : ViewModel() {

    // Lista de averías actualmente visible
    private val _averias = MutableLiveData<List<Averia>>()
    val averias: LiveData<List<Averia>> = _averias

    // Pestaña activa: 0 = Nuevas, 1 = Recibidas
    private val _tabActivo = MutableLiveData<Int>(0)
    val tabActivo: LiveData<Int> = _tabActivo

    init {
        // Al crear el ViewModel cargamos las averías nuevas por defecto
        cargarAveriasNuevas()
    }

    fun cargarAveriasNuevas() {
        _tabActivo.value = 0
        _averias.value = FakeDataSource.getAveriasNuevas()
    }

    fun cargarAveriasRecibidas() {
        _tabActivo.value = 1
        _averias.value = FakeDataSource.getAveriasRecibidas()
    }

    fun getAveriaPorId(id: Int): Averia? {
        return FakeDataSource.getAveriaPorId(id)
    }
}