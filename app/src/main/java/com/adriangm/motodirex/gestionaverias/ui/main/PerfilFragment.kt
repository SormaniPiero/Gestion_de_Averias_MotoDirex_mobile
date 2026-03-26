package com.adriangm.motodirex.gestionaverias.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        configurarBotones()
    }

    private fun mostrarDatosPerfil() {
        // 1. Usamos requireContext() para asegurar que el contexto no sea nulo
        val context = requireContext()

        // 2. Recuperamos los datos (solo una vez)
        val nombre = SessionManager.getNombre(context) ?: "Técnico"

        // IMPORTANTE: Asegúrate de tener getEmail en tu SessionManager,
        // de lo contrario usa getNombre temporalmente para probar.
        val email = SessionManager.getNombre(context) ?: "correo@ejemplo.com"

        // 3. Asignamos los datos a la interfaz
        binding.tvNombreCompleto.text = nombre
        binding.tvAvatar.text = nombre.firstOrNull()?.uppercaseChar()?.toString() ?: "T"

        // 4. CORRECCIÓN DE ERRORES:
        // Para el email, primero asignamos el TEXTO
        binding.tvEmail.text = email
        // Y luego ponemos la VISIBILIDAD como un Int (View.VISIBLE)
        binding.tvEmail.visibility = View.VISIBLE

        // El teléfono se mantiene oculto
        binding.tvTelefono.visibility = View.GONE
    }

    private fun configurarBotones() {
        binding.btnCerrarSesion.setOnClickListener {
            DialogUtils.mostrarConfirmacion(
                context      = requireContext(),
                titulo       = "Cerrar sesión",
                mensaje      = "¿Estás seguro de que quieres cerrar sesión?",
                textoAceptar = "Sí, salir"
            ) {
                SessionManager.cerrarSesion(requireContext())
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