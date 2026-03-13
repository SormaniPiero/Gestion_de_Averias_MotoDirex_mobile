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

        val averiaId = arguments?.getInt("averiaId", -1) ?: -1
        viewModel.cargarAveria(averiaId)

        configurarBotones()
        observarViewModel()
    }

    private fun configurarBotones() {

        binding.btnAceptar.setOnClickListener {
            DialogUtils.mostrarConfirmacion(
                context      = requireContext(),
                titulo       = getString(R.string.dialogo_aceptar_titulo),
                mensaje      = getString(R.string.dialogo_aceptar_mensaje),
                textoAceptar = getString(R.string.dialogo_aceptar_boton)
            ) {
                viewModel.aceptarAveria()
            }
        }

        binding.btnIntervencion.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("averiaId", arguments?.getInt("averiaId", -1) ?: -1)
            }
            findNavController().navigate(R.id.action_detalle_to_intervencion, bundle)
        }

        binding.btnCambiarEstado.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("averiaId", arguments?.getInt("averiaId", -1) ?: -1)
            }
            findNavController().navigate(R.id.action_detalle_to_estado, bundle)
        }

        binding.btnFinalizar.setOnClickListener {
            val av = viewModel.averia.value

            if (av?.procRealizadoTecnico.isNullOrBlank()) {
                Snackbar.make(
                    binding.root,
                    "Debes registrar una intervención antes de finalizar",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (viewModel.estadoMaquinaConfirmado.value != true) {
                Snackbar.make(
                    binding.root,
                    "Debes confirmar el estado de la máquina en 'Cambiar Estado Máquina'",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val resumen = """
                |📋 AVE-${av?.codigoAveria}
                |
                |🔧 Tipo: ${av?.tipoAveria?.descripcionTipo}
                |📍 Ubicación: ${av?.maquinaria?.ubicacion}
                |⚙️ Estado máquina: ${av?.maquinaria?.codigoEstadoFK?.descripcion}
                |
                |📝 Descripción:
                |${av?.descInicAveria}
                |
                |📅 Inicio: ${DateUtils.formatear(av?.fechaInicioAver)}
                |📅 Asignación: ${DateUtils.formatear(av?.fechaAsigTecnico)}
                |📅 Aceptación: ${DateUtils.formatear(av?.fechaAcepTecnico)}
                |📅 Finalización: (ahora)
            """.trimMargin()

            DialogUtils.mostrarConfirmacion(
                context      = requireContext(),
                titulo       = getString(R.string.dialogo_finalizar_titulo),
                mensaje      = "¿Confirmas que has terminado el trabajo? Esta acción no se puede deshacer.\n\n$resumen",
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
        binding.tvCodigo.text            = "AVE-${averia.codigoAveria}"
        binding.tvEstadoBadge.text       = averia.estadoAveria.name
        binding.tvTipoAveria.text        = averia.tipoAveria.descripcionTipo
        binding.tvDescripcion.text       = averia.descInicAveria
        binding.tvNombreMaquina.text     = averia.maquinaria.nombreMaquinaria
        binding.tvModeloMaquina.text     = averia.maquinaria.modeloMaquinaria
        binding.tvUbicacion.text         = averia.maquinaria.ubicacion
        binding.tvFechaInicio.text       = DateUtils.formatear(averia.fechaInicioAver)
        binding.tvFechaAsignacion.text   = DateUtils.formatear(averia.fechaAsigTecnico)
        binding.tvFechaAceptacion.text   = DateUtils.formatear(averia.fechaAcepTecnico)
        binding.tvFechaFinalizacion.text = DateUtils.formatear(averia.fechaFinalizTecnico)

        // Estado de la máquina
        binding.tvEstadoMaquinaDetalle.text = averia.maquinaria.codigoEstadoFK.descripcion
        binding.tvEstadoMaquinaDetalle.backgroundTintList =
            android.content.res.ColorStateList.valueOf(
                requireContext().getColor(R.color.text_secondary)
            )

        val colorEstado = when (averia.estadoAveria) {
            EstadoAveria.ASIGNADA   -> requireContext().getColor(R.color.estado_asignada)
            EstadoAveria.ACEPTADA   -> requireContext().getColor(R.color.estado_aceptada)
            EstadoAveria.FINALIZADA -> requireContext().getColor(R.color.estado_finalizada)
        }
        binding.tvEstadoBadge.backgroundTintList =
            android.content.res.ColorStateList.valueOf(colorEstado)

        if (!averia.procRealizadoTecnico.isNullOrBlank()) {
            binding.cardIntervencion.visibility = View.VISIBLE
            binding.tvIntervencion.text = averia.procRealizadoTecnico
        } else {
            binding.cardIntervencion.visibility = View.GONE
        }
    }

    private fun actualizarBotones(averia: Averia) {
        when (averia.estadoAveria) {
            EstadoAveria.ASIGNADA -> {
                binding.btnAceptar.isEnabled = true
                binding.btnAceptar.alpha     = 1f

                binding.btnIntervencion.isEnabled = false
                binding.btnIntervencion.alpha     = 0.3f
                binding.btnIntervencion.setTextColor(requireContext().getColor(R.color.text_secondary))
                binding.btnIntervencion.strokeColor = android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(R.color.divider))

                binding.btnCambiarEstado.isEnabled = false
                binding.btnCambiarEstado.alpha     = 0.3f
                binding.btnCambiarEstado.setTextColor(requireContext().getColor(R.color.text_secondary))
                binding.btnCambiarEstado.strokeColor = android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(R.color.divider))

                binding.btnFinalizar.isEnabled = false
                binding.btnFinalizar.alpha     = 0.3f
            }

            EstadoAveria.ACEPTADA -> {
                binding.btnAceptar.isEnabled = false
                binding.btnAceptar.alpha     = 0.3f

                binding.btnIntervencion.isEnabled = true
                binding.btnIntervencion.alpha     = 1f
                binding.btnIntervencion.setTextColor(requireContext().getColor(R.color.primary))
                binding.btnIntervencion.strokeColor = android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(R.color.primary))

                binding.btnCambiarEstado.isEnabled = true
                binding.btnCambiarEstado.alpha     = 1f
                binding.btnCambiarEstado.setTextColor(requireContext().getColor(R.color.primary))
                binding.btnCambiarEstado.strokeColor = android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(R.color.primary))

                binding.btnFinalizar.isEnabled = true
                binding.btnFinalizar.alpha     = 1f
            }

            EstadoAveria.FINALIZADA -> {
                binding.btnAceptar.isEnabled = false
                binding.btnAceptar.alpha     = 0.3f

                binding.btnIntervencion.isEnabled = false
                binding.btnIntervencion.alpha     = 0.3f
                binding.btnIntervencion.setTextColor(requireContext().getColor(R.color.text_secondary))
                binding.btnIntervencion.strokeColor = android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(R.color.divider))

                binding.btnCambiarEstado.isEnabled = false
                binding.btnCambiarEstado.alpha     = 0.3f
                binding.btnCambiarEstado.setTextColor(requireContext().getColor(R.color.text_secondary))
                binding.btnCambiarEstado.strokeColor = android.content.res.ColorStateList.valueOf(
                    requireContext().getColor(R.color.divider))

                binding.btnFinalizar.isEnabled = false
                binding.btnFinalizar.alpha     = 0.3f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}