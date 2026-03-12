package com.adriangm.motodirex.gestionaverias.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Funciones auxiliares para formatear fechas en la app.
 */

object DateUtils {

    // Formato legible para mostrar en pantalla: "15/03/2025 09:30"
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    /**
     * Convierte un LocalDateTime a String legible.
     * Si es null devuelve "—" (sin dato).
     */

    fun formatear(fecha: LocalDateTime?): String {
        return fecha?.format(formatter) ?: "—"
    }

    /**
     * Devuelve la fecha y hora actual.
     */

    fun ahora(): LocalDateTime = LocalDateTime.now()
}