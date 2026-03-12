package com.adriangm.motodirex.gestionaverias.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.adriangm.motodirex.gestionaverias.R


/**
 * Utilidad para mostrar diálogos de confirmación
 * con el estilo visual de MotoDirex
 */

object DialogUtils {

    /**
     * Muestra un diálogo de confirmación genérico.
     *
     * @param context     Contexto de la Activity o Fragment
     * @param titulo      Título del diálogo
     * @param mensaje     Mensaje descriptivo
     * @param textoAceptar Texto del botón positivo
     * @param onConfirmar Acción a ejecutar si el usuario confirma
     */

    fun mostrarConfirmacion(
        context: Context,
        titulo: String,
        mensaje: String,
        textoAceptar: String = "Confirmar",
        onConfirmar: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context, R.style.MotoDirex_Dialog)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(textoAceptar) { dialog, _ ->
                dialog.dismiss()
                onConfirmar()
            }
            .show()
    }
}