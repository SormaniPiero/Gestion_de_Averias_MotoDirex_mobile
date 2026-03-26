package com.adriangm.motodirex.gestionaverias.data.network.dto

import com.adriangm.motodirex.gestionaverias.model.EstadoMaquinaria
import com.adriangm.motodirex.gestionaverias.model.TipoAveria
import com.google.gson.annotations.SerializedName

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
    val id: Int,
   // val asignadas: Int,
    val aceptadas: Int,
    val finalizadas: Int,
    @SerializedName("total_asignadas") val asignadas: Int
)

data class MensajeResponse(
    val mensaje: String? = null,
    val error: String? = null
)

data class AveriaDto(
    val codigoAveria: Int = 0,
    val descripcion: String = "",
    val estado: String = "",
    val maquinariaFK: Int = 0,
    @SerializedName("nombreTipoAveria") val tipoAveria: String? = null,
    @SerializedName("nombreMaquina") val nombreMaquina: String? = null,
    //val estadoMaquinaria: EstadoMaquinaria?

)

