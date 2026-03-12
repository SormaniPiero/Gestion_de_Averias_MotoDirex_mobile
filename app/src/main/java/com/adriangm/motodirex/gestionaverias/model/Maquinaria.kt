package com.adriangm.motodirex.gestionaverias.model

/**
 * Representa la máquina asociada a una avería.
 *
 * - codigoMaquinaria    → identificador único
 * - nombreMaquinaria    → nombre descriptivo de la máquina
 * - modeloMaquinaria    → modelo técnico
 * - ubicacion           → dónde está físicamente
 * - codigoEstadoFK      → estado actual (el técnico puede modificarlo)
 */

data class Maquinaria(
    val codigoMaquinaria: Int,
    val nombreMaquinaria: String,
    val modeloMaquinaria: String,
    val ubicacion: String,
    var codigoEstadoFK: EstadoMaquinaria   // 'var' porque el técnico puede cambiarlo
)