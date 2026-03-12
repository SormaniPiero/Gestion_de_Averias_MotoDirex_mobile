package com.adriangm.motodirex.gestionaverias.model

import java.time.LocalDateTime

/**
 * Representa una avería asignada al técnico.
 *
 * Campos de solo lectura (val) → el técnico no los modifica:
 *   - codigoAveria, descInicAveria, fechaInicioAver,
 *     fechaAsigTecnico, usuarioTecnicoFK, maquinariaFK, tipoAveriaFK
 *
 * Campos modificables (var) → el técnico los actualiza desde la app:
 *   - fechaAcepTecnico    → se asigna al Aceptar (CU03)
 *   - procRealizadoTecnico → se rellena al Registrar intervención (CU04)
 *   - fechaFinalizTecnico  → se asigna al Finalizar (CU06)
 *   - estadoAveria         → cambia según las acciones del técnico
 */

data class Averia(
    val codigoAveria: Int,
    val descInicAveria: String,
    val fechaInicioAver: LocalDateTime,
    val fechaAsigTecnico: LocalDateTime,
    val usuarioTecnicoFK: Int,
    val maquinaria: Maquinaria,
    val tipoAveria: TipoAveria,

    // Campos que el técnico va rellenando
    var fechaAcepTecnico: LocalDateTime? = null,       // null = aún no aceptada
    var procRealizadoTecnico: String? = null,           // null = sin intervención
    var fechaFinalizTecnico: LocalDateTime? = null,     // null = no finalizada
    var estadoAveria: EstadoAveria = EstadoAveria.ASIGNADA
)