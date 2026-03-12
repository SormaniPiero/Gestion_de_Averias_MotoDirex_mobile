package com.adriangm.motodirex.gestionaverias.ui.estado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.adriangm.motodirex.gestionaverias.databinding.FragmentCambiarEstadoBinding
import com.adriangm.motodirex.gestionaverias.model.EstadoMaquinaria
import com.adriangm.motodirex.gestionaverias.viewmodel.DetalleViewModel
import com.google.android.material.snackbar.Snackbar
import com.adriangm.motodirex.gestionaverias.R

class CambiarEstadoFragment : Fragment() {

    private var _binding: FragmentCambiarEstadoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetalleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCambiarEstadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Compartimos el mismo ViewModel que el Detalle
        viewModel = ViewModelProvider(requireActivity())[DetalleViewModel::class.java]

        configurarSpinner()
        mostrarDatosMaquina()
        configurarBotones()
        observarViewModel()
    }

    private fun configurarSpinner() {
        val estados = EstadoMaquinaria.values().map { it.descripcion }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_estado,
            estados
        )
        adapter.setDropDownViewResource(R.layout.item_spinner_estado)
        binding.spinnerEstado.adapter = adapter

        // Seleccionar el estado actual de la máquina
        viewModel.averia.value?.let { averia ->
            val posicion = EstadoMaquinaria.values()
                .indexOfFirst { it == averia.maquinaria.codigoEstadoFK }
            if (posicion >= 0) binding.spinnerEstado.setSelection(posicion)
        }
    }

    private fun mostrarDatosMaquina() {
        viewModel.averia.value?.let { averia ->
            binding.tvNombreMaquina.text = averia.maquinaria.nombreMaquinaria
            binding.tvEstadoActual.text  = averia.maquinaria.codigoEstadoFK.descripcion
        }
    }

    private fun configurarBotones() {
        binding.btnActualizar.setOnClickListener {
            val posicion      = binding.spinnerEstado.selectedItemPosition
            val estadoNuevo   = EstadoMaquinaria.values()[posicion]

            viewModel.cambiarEstadoMaquinaria(estadoNuevo)
        }
    }

    private fun observarViewModel() {
        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (!mensaje.isNullOrBlank()) {
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
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