package com.adriangm.motodirex.gestionaverias.model

/**
 * Estados posibles de una avería.
 * Asignada  → el sistema la asigna al técnico (aún no la ha visto)
 * Aceptada  → el técnico la ha aceptado y está trabajando en ella
 * Finalizada → el técnico ha terminado el trabajo
 */

enum class EstadoAveria {
    ASIGNADA,
    ACEPTADA,
    FINALIZADA
}