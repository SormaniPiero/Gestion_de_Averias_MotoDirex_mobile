package com.adriangm.motodirex.gestionaverias.utils

import com.adriangm.motodirex.gestionaverias.model.Usuario

/**
 * Gestiona la sesión del técnico logueado.
 * En Fase 2 esto se sustituirá por el token JWT de la API.
 *
 * ⚠️ TODO FASE 2: Guardar y gestionar el token JWT aquí
 */

object SessionManager {

    private var _usuarioActual: Usuario? = null

    val usuarioActual: Usuario?
        get() = _usuarioActual

    fun iniciarSesion(usuario: Usuario) {
        _usuarioActual = usuario
    }

    fun cerrarSesion() {
        _usuarioActual = null
    }

    fun haySesionActiva(): Boolean = _usuarioActual != null
}