package com.adriangm.motodirex.gestionaverias.model

/**
 * Representa al técnico/mecánico que usa la app.
 *
 * - codigoUsuario  → identificador único
 * - email          → usado para el login
 * - password       → contraseña (en Fase 2 nunca se guarda en local)
 * - activo         → si es false, el login debe ser denegado
 * - codigoRolFK    → debe ser rol TECNICO para poder entrar
 */

data class Usuario(
    val codigoUsuario: Int,
    val email: String,
    val password: String,
    val activo: Boolean,
    val codigoRolFK: Int,
    val nombre: String = "",
    val apellidos: String = "",
    val telefono: String = ""
)