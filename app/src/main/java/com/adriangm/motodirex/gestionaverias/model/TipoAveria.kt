package com.adriangm.motodirex.gestionaverias.model

/**
 * Categoría o tipo de la avería.
 * Ejemplos: "Eléctrica", "Mecánica", "Hidráulica"
 */

data class TipoAveria(
    val codigoTipoAveria: Int,
    val descripcionTipo: String
)