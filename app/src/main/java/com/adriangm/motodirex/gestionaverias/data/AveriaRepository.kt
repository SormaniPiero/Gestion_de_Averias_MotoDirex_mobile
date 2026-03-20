package com.adriangm.motodirex.gestionaverias.data

import com.adriangm.motodirex.gestionaverias.data.network.RetrofitClient
import com.adriangm.motodirex.gestionaverias.data.network.dto.*


class AveriaRepository{
    private val api = RetrofitClient.api

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAverias(token: String): Result<List<AveriaDto>> {
        return try {
            val response = api.getAverias("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun aceptarAveria(token: String, id: Int): Result<String> {
        return try {
            val response = api.aceptarAveria("Bearer $token", id)
            if (response.isSuccessful) {
                Result.success(response.body()?.mensaje ?: "Avería aceptada")
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun finalizarAveria(token: String, id: Int): Result<String> {
        return try {
            val response = api.finalizarAveria("Bearer $token", id)
            if (response.isSuccessful) {
                Result.success(response.body()?.mensaje ?: "Avería finalizada")
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrarIntervencion(token: String, averiaId: Int, descripcion: String): Result<String> {
        return try {
            val response = api.registrarIntervencion(
                "Bearer $token",
                IntervencionRequest(averiaId, descripcion)
            )
            if (response.isSuccessful) {
                Result.success(response.body()?.mensaje ?: "Intervención registrada")
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cambiarEstado(token: String, idMaquinaria: Int, estado: String): Result<String> {
        return try {
            val response = api.cambiarEstado(
                "Bearer $token",
                idMaquinaria,
                EstadoRequest(estado)
            )
            if (response.isSuccessful) {
                Result.success(response.body()?.mensaje ?: "Estado actualizado")
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}