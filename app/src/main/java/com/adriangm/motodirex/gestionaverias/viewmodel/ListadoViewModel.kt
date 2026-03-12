package com.adriangm.motodirex.gestionaverias.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adriangm.motodirex.gestionaverias.data.FakeDataSource
import com.adriangm.motodirex.gestionaverias.model.Averia

/**
 * ViewModel del listado de averías.
 * Gestiona pestañas, búsqueda y filtrado.
 *
 * ⚠️ TODO FASE 2: Sustituir FakeDataSource por llamadas Retrofit
 */

class ListadoViewModel : ViewModel() {

    private val _averias = MutableLiveData<List<Averia>>()
    val averias: LiveData<List<Averia>> = _averias

    private val _tabActivo = MutableLiveData<Int>(0)
    val tabActivo: LiveData<Int> = _tabActivo

    // Guardamos el texto de búsqueda actual
    private var textoBusqueda: String = ""

    init {
        cargarAveriasNuevas()
    }

    fun cargarAveriasNuevas() {
        _tabActivo.value = 0
        textoBusqueda    = ""
        aplicarFiltro(FakeDataSource.getAveriasNuevas())
    }

    fun cargarAveriasRecibidas() {
        _tabActivo.value = 1
        textoBusqueda    = ""
        aplicarFiltro(FakeDataSource.getAveriasRecibidas())
    }

    /**
     * Filtra la lista actual según el texto de búsqueda.
     * Busca en: descripción, código de avería y nombre de máquina.
     */
    fun buscar(texto: String) {
        textoBusqueda = texto.trim().lowercase()

        val listaBase = when (_tabActivo.value) {
            0    -> FakeDataSource.getAveriasNuevas()
            1    -> FakeDataSource.getAveriasRecibidas()
            else -> FakeDataSource.getAveriasNuevas()
        }

        aplicarFiltro(listaBase)
    }

    private fun aplicarFiltro(lista: List<Averia>) {
        if (textoBusqueda.isEmpty()) {
            _averias.value = lista
            return
        }

        _averias.value = lista.filter { averia ->
            averia.descInicAveria.lowercase().contains(textoBusqueda) ||
                    averia.codigoAveria.toString().contains(textoBusqueda)    ||
                    averia.maquinaria.nombreMaquinaria.lowercase().contains(textoBusqueda) ||
                    averia.tipoAveria.descripcionTipo.lowercase().contains(textoBusqueda)
        }
    }

    fun getContadorNuevas(): Int    = FakeDataSource.getAveriasNuevas().size
    fun getContadorRecibidas(): Int = FakeDataSource.getAveriasRecibidas().size

    fun getAveriaPorId(id: Int): Averia? = FakeDataSource.getAveriaPorId(id)
}