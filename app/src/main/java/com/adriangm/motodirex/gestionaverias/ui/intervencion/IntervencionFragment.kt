package com.adriangm.motodirex.gestionaverias.ui.intervencion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.adriangm.motodirex.gestionaverias.databinding.FragmentIntervencionBinding
import com.adriangm.motodirex.gestionaverias.viewmodel.DetalleViewModel

class IntervencionFragment : Fragment() {

    private var _binding: FragmentIntervencionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetalleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntervencionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Compartimos el mismo ViewModel que el Detalle
        // así los cambios se reflejan automáticamente al volver
        viewModel = ViewModelProvider(requireActivity())[DetalleViewModel::class.java]

        mostrarDatosAveria()
        configurarBotones()
        observarViewModel()
    }

    private fun mostrarDatosAveria() {
        viewModel.averia.value?.let { averia ->
            binding.tvCodigoAveria.text     = "AVE-${averia.codigoAveria}"
            binding.tvDescripcionAveria.text = averia.descInicAveria

            // Si ya había una intervención previa la mostramos en el campo
            if (!averia.procRealizadoTecnico.isNullOrBlank()) {
                binding.etDescripcion.setText(averia.procRealizadoTecnico)
            }
        }
    }

    private fun configurarBotones() {
        binding.btnGuardar.setOnClickListener {
            val descripcion = binding.etDescripcion.text.toString().trim()

            if (descripcion.isEmpty()) {
                binding.tvError.text       = "La descripción es obligatoria"
                binding.tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            binding.tvError.visibility = View.GONE
            viewModel.registrarIntervencion(descripcion)
        }
    }

    private fun observarViewModel() {
        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (!mensaje.isNullOrBlank()) {
                // Volver al detalle automáticamente al guardar
                findNavController().popBackStack()
                viewModel.mensajeMostrado()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}