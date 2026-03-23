package com.adriangm.motodirex.gestionaverias.utils

import com.adriangm.motodirex.gestionaverias.model.Usuario
import android.content.Context
/**
 * Gestiona la sesión del técnico logueado.
 * En Fase 2 esto se sustituirá por el token JWT de la API.
 *
 *TODO FASE 2: Guardar y gestionar el token JWT aquí
 */

object SessionManager {

    private const val PREF_NAME = "motodirex_session"
    private const val KEY_TOKEN  = "token"
    private const val KEY_NOMBRE = "nombre"
    private const val KEY_ID     = "id"

    fun guardarSesion(context: Context, token: String, nombre: String, id: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NOMBRE, nombre)
            .putInt(KEY_ID, id)
            .apply()
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    fun getNombre(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_NOMBRE, null)
    }

    fun getId(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_ID, -1)
    }

    fun cerrarSesion(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .clear()
            .apply()
    }

    fun haySesion(context: Context): Boolean {
        return getToken(context) != null
    }





}