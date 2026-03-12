package com.adriangm.motodirex.gestionaverias.ui.detalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.adriangm.motodirex.gestionaverias.R
import com.adriangm.motodirex.gestionaverias.databinding.FragmentDetalleAveriaBinding
import com.adriangm.motodirex.gestionaverias.model.Averia
import com.adriangm.motodirex.gestionaverias.model.EstadoAveria
import com.adriangm.motodirex.gestionaverias.utils.DateUtils
import com.adriangm.motodirex.gestionaverias.viewmodel.DetalleViewModel
import com.google.android.material.snackbar.Snackbar

class DetalleAveriaFragment : Fragment() {

    private var _binding: FragmentDetalleAveriaBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetalleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleAveriaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DetalleViewModel::class.java]

        // Recuperar el ID de la avería que nos pasó el listado
        val averiaId = arguments?.getInt("averiaId", -1) ?: -1
        viewModel.cargarAveria(averiaId)

        configurarBotones()
        observarViewModel()
    }

    private fun configurarBotones() {
        // CU03 - Aceptar avería
        binding.btnAceptar.setOnClickListener {
            viewModel.aceptarAveria()
        }

        // CU04 - Registrar intervención → navegar al Fragment de intervención
        binding.btnIntervencion.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("averiaId", arguments?.getInt("averiaId", -1) ?: -1)
            }
            findNavController().navigate(R.id.action_detalle_to_intervencion, bundle)
        }

        // CU05 - Cambiar estado → navegar al Fragment de estado
        binding.btnCambiarEstado.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("averiaId", arguments?.getInt("averiaId", -1) ?: -1)
            }
            findNavController().navigate(R.id.action_detalle_to_estado, bundle)
        }

        // CU06 - Finalizar avería
        binding.btnFinalizar.setOnClickListener {
            viewModel.finalizarAveria()
        }
    }

    private fun observarViewModel() {
        viewModel.averia.observe(viewLifecycleOwner) { averia ->
            mostrarDatos(averia)
            actualizarBotones(averia)
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (!mensaje.isNullOrBlank()) {
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                viewModel.mensajeMostrado()
            }
        }
    }

    private fun mostrarDatos(averia: Averia) {
        binding.tvCodigo.text          = "AVE-${averia.codigoAveria}"
        binding.tvEstadoBadge.text     = averia.estadoAveria.name
        binding.tvTipoAveria.text      = averia.tipoAveria.descripcionTipo
        binding.tvDescripcion.text     = averia.descInicAveria
        binding.tvNombreMaquina.text   = averia.maquinaria.nombreMaquinaria
        binding.tvModeloMaquina.text   = averia.maquinaria.modeloMaquinaria
        binding.tvUbicacion.text       = averia.maquinaria.ubicacion
        binding.tvFechaInicio.text     = DateUtils.formatear(averia.fechaInicioAver)
        binding.tvFechaAsignacion.text = DateUtils.formatear(averia.fechaAsigTecnico)
        binding.tvFechaAceptacion.text = DateUtils.formatear(averia.fechaAcepTecnico)
        binding.tvFechaFinalizacion.text = DateUtils.formatear(averia.fechaFinalizTecnico)

        // Color del badge de estado
        val colorEstado = when (averia.estadoAveria) {
            EstadoAveria.ASIGNADA   ->
                requireContext().getColor(R.color.estado_asignada)
            EstadoAveria.ACEPTADA   ->
                requireContext().getColor(R.color.estado_aceptada)
            EstadoAveria.FINALIZADA ->
                requireContext().getColor(R.color.estado_finalizada)
        }
        binding.tvEstadoBadge.backgroundTintList =
            android.content.res.ColorStateList.valueOf(colorEstado)

        // Mostrar tarjeta de intervención solo si existe
        if (!averia.procRealizadoTecnico.isNullOrBlank()) {
            binding.cardIntervencion.visibility = View.VISIBLE
            binding.tvIntervencion.text = averia.procRealizadoTecnico
        } else {
            binding.cardIntervencion.visibility = View.GONE
        }
    }

    /**
     * Activa o desactiva los botones según el estado actual de la avería.
     * Sigue exactamente las reglas del documento de Casos de Uso.
     */
    private fun actualizarBotones(averia: Averia) {
        when (averia.estadoAveria) {
            EstadoAveria.ASIGNADA -> {
                // Solo se puede aceptar
                binding.btnAceptar.isEnabled      = true
                binding.btnIntervencion.isEnabled = false
                binding.btnCambiarEstado.isEnabled = false
                binding.btnFinalizar.isEnabled    = false
            }
            EstadoAveria.ACEPTADA -> {
                // Ya aceptada: no se puede aceptar de nuevo
                // Sí se puede intervenir, cambiar estado y finalizar
                binding.btnAceptar.isEnabled      = false
                binding.btnIntervencion.isEnabled = true
                binding.btnCambiarEstado.isEnabled = true
                binding.btnFinalizar.isEnabled    = true
            }
            EstadoAveria.FINALIZADA -> {
                // Finalizada: todos los botones desactivados
                binding.btnAceptar.isEnabled      = false
                binding.btnIntervencion.isEnabled = false
                binding.btnCambiarEstado.isEnabled = false
                binding.btnFinalizar.isEnabled    = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}