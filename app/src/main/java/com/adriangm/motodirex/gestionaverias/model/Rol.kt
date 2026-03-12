package com.adriangm.motodirex.gestionaverias.model

/**
 * Rol del usuario en el sistema.
 * En nuestra app solo entran usuarios con rol TECNICO.
 * Esta clase existe para ser coherente con la BD real.
 */

data class Rol(
    val codigoRol: Int,
    val descripcionRol: String   // Ej: "Tecnico", "Administrador"
)