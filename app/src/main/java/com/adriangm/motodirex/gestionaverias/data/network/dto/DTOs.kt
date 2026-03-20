package com.adriangm.motodirex.gestionaverias.data.network.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class IntervencionRequest(
    val averia_id: Int,
    val descripcion: String
)

data class EstadoRequest(
    val estado: String
)

data class LoginResponse(
    val token: String,
    val nombre: String,
    val id: Int
)

data class MensajeResponse(
    val mensaje: String? = null,
    val error: String? = null
)

data class AveriaDto(
    val codigoAveria: Int = 0,
    val descripcion: String = "",
    val estado: String = "",
    val maquinariaFK: Int = 0
)

