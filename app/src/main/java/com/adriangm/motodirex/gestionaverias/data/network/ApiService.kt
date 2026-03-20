package com.adriangm.motodirex.gestionaverias.data.network

import com.adriangm.motodirex.gestionaverias.data.network.dto.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService{
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("api/averias")
    suspend fun getAverias(@Header("Authorization") token: String): Response<List<AveriaDto>>

    @PUT("api/averias/aceptar/{id}")
    suspend fun aceptarAveria(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MensajeResponse>

    @PUT("api/averias/finalizar/{id}")
    suspend fun finalizarAveria(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MensajeResponse>

    @POST("api/intervenciones")
    suspend fun registrarIntervencion(
        @Header("Authorization") token: String,
        @Body body: IntervencionRequest
    ): Response<MensajeResponse>

    @PUT("api/estado/{id}")
    suspend fun cambiarEstado(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: EstadoRequest
    ): Response<MensajeResponse>

}