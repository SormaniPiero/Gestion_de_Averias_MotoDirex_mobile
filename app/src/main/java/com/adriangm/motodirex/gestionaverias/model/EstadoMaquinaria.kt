package com.adriangm.motodirex.gestionaverias.model

/**
 * Estados posibles de una máquina.
 * El técnico puede cambiar este estado desde la app.
 */

enum class EstadoMaquinaria(val descripcion: String) {
    OPERATIVA("Operativa"),
    EN_REPARACION("En reparación"),
    FUERA_DE_SERVICIO("Fuera de servicio"),
    EN_REVISION("En revisión")
}