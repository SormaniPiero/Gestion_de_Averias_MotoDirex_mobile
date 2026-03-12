package com.adriangm.motodirex.gestionaverias.data

import com.adriangm.motodirex.gestionaverias.model.*
import java.time.LocalDateTime

/**
 * Fuente de datos simulada para la Fase 1.
 * Sustituye completamente a la API REST.
 *
 * 'object' en Kotlin significa que es un Singleton:
 * solo existe una instancia en toda la app, igual que
 * si fuera un servidor real devolviendo siempre los mismos datos.
 *
 * ⚠️ TODO FASE 2: Eliminar este archivo y conectar Retrofit.
 */

object FakeDataSource {

    // ─────────────────────────────────────────
    // USUARIO SIMULADO (el técnico que inicia sesión)
    // ─────────────────────────────────────────

    val usuarioActivo = Usuario(
        codigoUsuario = 1,
        email = "tecnico@motodirex.com",
        password = "1234",
        activo = true,
        codigoRolFK = 2       // 2 = Técnico
    )

    val usuarioInactivo = Usuario(
        codigoUsuario = 2,
        email = "inactivo@motodirex.com",
        password = "1234",
        activo = false,        // ← este usuario NO puede entrar
        codigoRolFK = 2
    )

    // ─────────────────────────────────────────
    // TIPOS DE AVERÍA SIMULADOS
    // ─────────────────────────────────────────

    private val tipoElectrica = TipoAveria(1, "Eléctrica")
    private val tipoMecanica  = TipoAveria(2, "Mecánica")
    private val tipoHidraulica = TipoAveria(3, "Hidráulica")

    // ─────────────────────────────────────────
    // MÁQUINAS SIMULADAS
    // ─────────────────────────────────────────

    private val maquina1 = Maquinaria(
        codigoMaquinaria = 101,
        nombreMaquinaria = "Torno CNC Alpha",
        modeloMaquinaria = "CNC-3200X",
        ubicacion = "Nave A - Sector 2",
        codigoEstadoFK = EstadoMaquinaria.EN_REPARACION
    )

    private val maquina2 = Maquinaria(
        codigoMaquinaria = 102,
        nombreMaquinaria = "Fresadora Industrial Beta",
        modeloMaquinaria = "FRES-800",
        ubicacion = "Nave B - Sector 1",
        codigoEstadoFK = EstadoMaquinaria.FUERA_DE_SERVICIO
    )

    private val maquina3 = Maquinaria(
        codigoMaquinaria = 103,
        nombreMaquinaria = "Compresor Hidráulico Delta",
        modeloMaquinaria = "HD-2200",
        ubicacion = "Nave A - Sector 5",
        codigoEstadoFK = EstadoMaquinaria.EN_REVISION
    )

    private val maquina4 = Maquinaria(
        codigoMaquinaria = 104,
        nombreMaquinaria = "Soldadora Automática Gamma",
        modeloMaquinaria = "SOL-450",
        ubicacion = "Nave C - Sector 3",
        codigoEstadoFK = EstadoMaquinaria.EN_REPARACION
    )

    // ─────────────────────────────────────────
    // AVERÍAS SIMULADAS
    // ─────────────────────────────────────────
    //
    // Tenemos 4 averías:
    //   - 2 NUEVAS     (fechaAcepTecnico = null) → aparecen en pestaña "Nuevas"
    //   - 2 RECIBIDAS  (fechaAcepTecnico != null, fechaFinalizTecnico = null)
    //                                            → aparecen en pestaña "Recibidas"
    // ─────────────────────────────────────────

    val listaAverias = mutableListOf(

        // ── AVERÍA 1: NUEVA ──
        Averia(
            codigoAveria = 1001,
            descInicAveria = "El torno CNC no arranca al iniciar el turno. " +
                    "Se escucha un ruido extraño en el motor principal.",
            fechaInicioAver = LocalDateTime.now().minusDays(2),
            fechaAsigTecnico = LocalDateTime.now().minusHours(5),
            usuarioTecnicoFK = 1,
            maquinaria = maquina1,
            tipoAveria = tipoElectrica
        ),

        // ── AVERÍA 2: NUEVA ──
        Averia(
            codigoAveria = 1002,
            descInicAveria = "La fresadora presenta vibraciones anómalas " +
                    "durante el proceso de fresado. Posible desalineación del eje.",
            fechaInicioAver = LocalDateTime.now().minusDays(1),
            fechaAsigTecnico = LocalDateTime.now().minusHours(3),
            usuarioTecnicoFK = 1,
            maquinaria = maquina2,
            tipoAveria = tipoMecanica
        ),

        // ── AVERÍA 3: NUEVA ──
        Averia(
            codigoAveria = 1005,
            descInicAveria = "El robot de soldadura no responde a los comandos " +
                    "de inicio. Panel de control sin respuesta.",
            fechaInicioAver = LocalDateTime.now().minusHours(8),
            fechaAsigTecnico = LocalDateTime.now().minusHours(2),
            usuarioTecnicoFK = 1,
            maquinaria = Maquinaria(
                codigoMaquinaria = 105,
                nombreMaquinaria = "Robot Soldadura Épsilon",
                modeloMaquinaria = "ROB-S200",
                ubicacion = "Nave B - Sector 4",
                codigoEstadoFK = EstadoMaquinaria.FUERA_DE_SERVICIO
            ),
            tipoAveria = tipoElectrica
        ),

        // ── AVERÍA 4: NUEVA ──
        Averia(
            codigoAveria = 1006,
            descInicAveria = "La cinta transportadora se detiene cada 10 minutos " +
                    "de forma intermitente. Posible fallo en el variador de frecuencia.",
            fechaInicioAver = LocalDateTime.now().minusHours(6),
            fechaAsigTecnico = LocalDateTime.now().minusHours(1),
            usuarioTecnicoFK = 1,
            maquinaria = Maquinaria(
                codigoMaquinaria = 106,
                nombreMaquinaria = "Cinta Transportadora Zeta",
                modeloMaquinaria = "CTZ-1500",
                ubicacion = "Nave A - Sector 1",
                codigoEstadoFK = EstadoMaquinaria.EN_REVISION
            ),
            tipoAveria = tipoMecanica
        ),

        // ── AVERÍA 5: NUEVA ──
        Averia(
            codigoAveria = 1007,
            descInicAveria = "La prensa hidráulica pierde presión progresivamente " +
                    "durante el ciclo de trabajo. Se sospecha fuga interna.",
            fechaInicioAver = LocalDateTime.now().minusHours(4),
            fechaAsigTecnico = LocalDateTime.now().minusMinutes(45),
            usuarioTecnicoFK = 1,
            maquinaria = Maquinaria(
                codigoMaquinaria = 107,
                nombreMaquinaria = "Prensa Hidráulica Eta",
                modeloMaquinaria = "PHE-800T",
                ubicacion = "Nave C - Sector 1",
                codigoEstadoFK = EstadoMaquinaria.EN_REPARACION
            ),
            tipoAveria = tipoHidraulica
        ),

        // ── AVERÍA 6: RECIBIDA (aceptada, sin finalizar) ──
        Averia(
            codigoAveria = 1003,
            descInicAveria = "Pérdida de presión en el circuito hidráulico principal. " +
                    "Se detecta posible fuga en la válvula de control.",
            fechaInicioAver = LocalDateTime.now().minusDays(4),
            fechaAsigTecnico = LocalDateTime.now().minusDays(3),
            usuarioTecnicoFK = 1,
            maquinaria = maquina3,
            tipoAveria = tipoHidraulica,
            fechaAcepTecnico = LocalDateTime.now().minusDays(2),
            estadoAveria = EstadoAveria.ACEPTADA
        ),

        // ── AVERÍA 7: RECIBIDA (aceptada, con intervención, sin finalizar) ──
        Averia(
            codigoAveria = 1004,
            descInicAveria = "La soldadora automática detiene el proceso " +
                    "a mitad de ciclo. El panel muestra el error E-42.",
            fechaInicioAver = LocalDateTime.now().minusDays(5),
            fechaAsigTecnico = LocalDateTime.now().minusDays(4),
            usuarioTecnicoFK = 1,
            maquinaria = maquina4,
            tipoAveria = tipoElectrica,
            fechaAcepTecnico = LocalDateTime.now().minusDays(3),
            procRealizadoTecnico = "Revisado el módulo de control. " +
                    "Se ha reemplazado el sensor de posición defectuoso.",
            estadoAveria = EstadoAveria.ACEPTADA
        ),

        // ── AVERÍA 8: RECIBIDA (aceptada, sin intervención) ──
        Averia(
            codigoAveria = 1008,
            descInicAveria = "El horno de tratamiento térmico no alcanza " +
                    "la temperatura objetivo. Se queda estancado en 650°C.",
            fechaInicioAver = LocalDateTime.now().minusDays(3),
            fechaAsigTecnico = LocalDateTime.now().minusDays(2),
            usuarioTecnicoFK = 1,
            maquinaria = Maquinaria(
                codigoMaquinaria = 108,
                nombreMaquinaria = "Horno Tratamiento Térmico Theta",
                modeloMaquinaria = "HTT-1200",
                ubicacion = "Nave D - Sector 2",
                codigoEstadoFK = EstadoMaquinaria.EN_REPARACION
            ),
            tipoAveria = tipoElectrica,
            fechaAcepTecnico = LocalDateTime.now().minusDays(1),
            estadoAveria = EstadoAveria.ACEPTADA
        ),

        // ── AVERÍA 9: FINALIZADA ──
        Averia(
            codigoAveria = 1009,
            descInicAveria = "El compresor de aire comprimido genera ruido " +
                    "excesivo y vibración en la tubería principal.",
            fechaInicioAver = LocalDateTime.now().minusDays(7),
            fechaAsigTecnico = LocalDateTime.now().minusDays(6),
            usuarioTecnicoFK = 1,
            maquinaria = Maquinaria(
                codigoMaquinaria = 109,
                nombreMaquinaria = "Compresor Aire Iota",
                modeloMaquinaria = "CAI-500",
                ubicacion = "Nave A - Sector 3",
                codigoEstadoFK = EstadoMaquinaria.OPERATIVA
            ),
            tipoAveria = tipoMecanica,
            fechaAcepTecnico = LocalDateTime.now().minusDays(5),
            procRealizadoTecnico = "Sustituidas las juntas de la tubería principal " +
                    "y reajustados los tornillos de anclaje del compresor.",
            fechaFinalizTecnico = LocalDateTime.now().minusDays(4),
            estadoAveria = EstadoAveria.FINALIZADA
        )
    )

    // ─────────────────────────────────────────
    // FUNCIONES DE CONSULTA
    // ─────────────────────────────────────────

    /**
     * Devuelve las averías NUEVAS del técnico.
     * Nueva = fechaAcepTecnico es null (aún no las ha aceptado)
     */

    fun getAveriasNuevas(): List<Averia> {
        return listaAverias.filter { it.fechaAcepTecnico == null }
    }

    /**
     * Devuelve las averías RECIBIDAS del técnico.
     * Recibida = aceptada pero no finalizada todavía
     */

    fun getAveriasRecibidas(): List<Averia> {
        return listaAverias.filter {
            it.fechaAcepTecnico != null && it.fechaFinalizTecnico == null
        }
    }

    /**
     * Busca una avería concreta por su código.
     * Devuelve null si no existe.
     */

    fun getAveriaPorId(codigoAveria: Int): Averia? {
        return listaAverias.find { it.codigoAveria == codigoAveria }
    }

    /**
     * Simula el login.
     * Devuelve el usuario si email y password coinciden, null si no.
     */

    fun login(email: String, password: String): Usuario? {
        val todosLosUsuarios = listOf(usuarioActivo, usuarioInactivo)
        return todosLosUsuarios.find {
            it.email == email && it.password == password
        }
    }
}