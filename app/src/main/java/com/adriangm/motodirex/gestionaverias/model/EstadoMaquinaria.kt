package com.adriangm.motodirex.gestionaverias.model

enum class EstadoMaquinaria(val descripcion: String) {
    MOTO_OPERATIVO("MotoOperativo"),
    MOTO_AVERIADA("MotoAveriada"),
    MOTO_OBSOLETA("MotoObsoleta"),
    MOTO_INUTILIZABLE("MotoInutilizable"),
    MOTO_PARADA("MotoParada")
}