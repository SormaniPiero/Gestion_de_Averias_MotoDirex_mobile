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
import com.adriangm.motodirex.gestionaverias.utils.DialogUtils

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

        viewModel = ViewModelProvider(requireActivity())[DetalleViewModel::class.java]

        // Recuperar el ID de la avería que nos pasó el listado
        val averiaId = arguments?.getInt("averiaId", -1) ?: -1
        viewModel.cargarAveria(averiaId)

        configurarBotones()
        observarViewModel()
    }

    private fun configurarBotones() {

        // CU03 - Aceptar avería con confirmación
        binding.btnAceptar.setOnClickListener {
            DialogUtils.mostrarConfirmacion(
                context    = requireContext(),
                titulo     = getString(R.string.dialogo_aceptar_titulo),
                mensaje    = getString(R.string.dialogo_aceptar_mensaje),
                textoAceptar = getString(R.string.dialogo_aceptar_boton)
            ) {
                viewModel.aceptarAveria()
            }
        }

        // CU04 - Registrar intervención → navegar al Fragment
        binding.btnIntervencion.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("averiaId", arguments?.getInt("averiaId", -1) ?: -1)
            }
            findNavController().navigate(R.id.action_detalle_to_intervencion, bundle)
        }

        // CU05 - Cambiar estado → navegar al Fragment
        binding.btnCambiarEstado.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("averiaId", arguments?.getInt("averiaId", -1) ?: -1)
            }
            findNavController().navigate(R.id.action_detalle_to_estado, bundle)
        }

        // CU06 - Finalizar avería con confirmación
        binding.btnFinalizar.setOnClickListener {
            DialogUtils.mostrarConfirmacion(
                context      = requireContext(),
                titulo       = getString(R.string.dialogo_finalizar_titulo),
                mensaje      = getString(R.string.dialogo_finalizar_mensaje),
                textoAceptar = getString(R.string.dialogo_finalizar_boton)
            ) {
                viewModel.finalizarAveria()
            }
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
                // Aceptar: activo y rojo
                binding.btnAceptar.isEnabled = true
                binding.btnAceptar.alpha = 1f

                // Intervención: desactivado visualmente
                binding.btnIntervencion.isEnabled = false
                binding.btnIntervencion.alpha = 0.3f
                binding.btnIntervencion.setTextColor(
                    requireContext().getColor(R.color.text_secondary)
                )
                binding.btnIntervencion.strokeColor =
                    android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.divider)
                    )

                // Cambiar estado: desactivado visualmente
                binding.btnCambiarEstado.isEnabled = false
                binding.btnCambiarEstado.alpha = 0.3f
                binding.btnCambiarEstado.setTextColor(
                    requireContext().getColor(R.color.text_secondary)
                )
                binding.btnCambiarEstado.strokeColor =
                    android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.divider)
                    )

                // Finalizar: desactivado visualmente
                binding.btnFinalizar.isEnabled = false
                binding.btnFinalizar.alpha = 0.3f
            }

            EstadoAveria.ACEPTADA -> {
                // Aceptar: desactivado
                binding.btnAceptar.isEnabled = false
                binding.btnAceptar.alpha = 0.3f

                // Intervención: activo y rojo
                binding.btnIntervencion.isEnabled = true
                binding.btnIntervencion.alpha = 1f
                binding.btnIntervencion.setTextColor(
                    requireContext().getColor(R.color.primary)
                )
                binding.btnIntervencion.strokeColor =
                    android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.primary)
                    )

                // Cambiar estado: activo y rojo
                binding.btnCambiarEstado.isEnabled = true
                binding.btnCambiarEstado.alpha = 1f
                binding.btnCambiarEstado.setTextColor(
                    requireContext().getColor(R.color.primary)
                )
                binding.btnCambiarEstado.strokeColor =
                    android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.primary)
                    )

                // Finalizar: activo y verde
                binding.btnFinalizar.isEnabled = true
                binding.btnFinalizar.alpha = 1f
            }

            EstadoAveria.FINALIZADA -> {
                // Todos desactivados
                binding.btnAceptar.isEnabled = false
                binding.btnAceptar.alpha = 0.3f

                binding.btnIntervencion.isEnabled = false
                binding.btnIntervencion.alpha = 0.3f
                binding.btnIntervencion.setTextColor(
                    requireContext().getColor(R.color.text_secondary)
                )
                binding.btnIntervencion.strokeColor =
                    android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.divider)
                    )

                binding.btnCambiarEstado.isEnabled = false
                binding.btnCambiarEstado.alpha = 0.3f
                binding.btnCambiarEstado.setTextColor(
                    requireContext().getColor(R.color.text_secondary)
                )
                binding.btnCambiarEstado.strokeColor =
                    android.content.res.ColorStateList.valueOf(
                        requireContext().getColor(R.color.divider)
                    )

                binding.btnFinalizar.isEnabled = false
                binding.btnFinalizar.alpha = 0.3f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}