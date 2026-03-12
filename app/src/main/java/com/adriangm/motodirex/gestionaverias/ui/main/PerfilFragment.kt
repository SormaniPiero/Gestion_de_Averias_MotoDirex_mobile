package com.adriangm.motodirex.gestionaverias.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adriangm.motodirex.gestionaverias.data.FakeDataSource
import com.adriangm.motodirex.gestionaverias.databinding.FragmentPerfilBinding
import com.adriangm.motodirex.gestionaverias.ui.login.LoginActivity
import com.adriangm.motodirex.gestionaverias.utils.DialogUtils
import com.adriangm.motodirex.gestionaverias.utils.SessionManager

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mostrarDatosPerfil()
        mostrarEstadisticas()
        configurarBotones()
    }

    private fun mostrarDatosPerfil() {
        val usuario = SessionManager.usuarioActual ?: return

        val nombreCompleto = "${usuario.nombre} ${usuario.apellidos}".trim()
        binding.tvNombreCompleto.text = nombreCompleto
        binding.tvEmail.text          = usuario.email
        binding.tvTelefono.text       = usuario.telefono
        binding.tvEstadoUsuario.text  = if (usuario.activo) "Activo" else "Inactivo"

        // Inicial del nombre para el avatar
        binding.tvAvatar.text = usuario.nombre
            .firstOrNull()?.uppercaseChar()?.toString() ?: "T"
    }

    private fun mostrarEstadisticas() {
        binding.tvContadorNuevas.text      =
            FakeDataSource.getAveriasNuevas().size.toString()
        binding.tvContadorRecibidas.text   =
            FakeDataSource.getAveriasRecibidas().size.toString()
        binding.tvContadorFinalizadas.text =
            FakeDataSource.listaAverias
                .count { it.fechaFinalizTecnico != null }.toString()
    }

    private fun configurarBotones() {
        binding.btnCerrarSesion.setOnClickListener {
            DialogUtils.mostrarConfirmacion(
                context      = requireContext(),
                titulo       = "Cerrar sesión",
                mensaje      = "¿Estás seguro de que quieres cerrar sesión?",
                textoAceptar = "Sí, salir"
            ) {
                SessionManager.cerrarSesion()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}