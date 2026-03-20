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
import com.adriangm.motodirex.gestionaverias.R

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

        viewModel = ViewModelProvider(requireActivity())[DetalleViewModel::class.java]

        mostrarDatosAveria()
        configurarBotones()
        observarViewModel()
    }

    private fun mostrarDatosAveria() {
        viewModel.averia.value?.let { averia ->
            binding.tvCodigoAveria.text      = "AVE-${averia.codigoAveria}"
            binding.tvDescripcionAveria.text = averia.descripcion

            // La API no guarda el texto de intervención en el DTO
            // así que siempre mostramos el formulario limpio
            binding.bannerEdicion.visibility = View.GONE
            binding.btnGuardar.text = getString(R.string.intervencion_btn_guardar)
        }
    }

    private fun configurarBotones() {
        binding.btnGuardar.setOnClickListener {
            val descripcion = binding.etDescripcion.text.toString().trim()

            when {
                descripcion.isEmpty() -> {
                    binding.tvError.text       = "La descripción es obligatoria"
                    binding.tvError.visibility = View.VISIBLE
                }
                descripcion.length < 20 -> {
                    binding.tvError.text       = "La descripción debe tener al menos 20 caracteres (${descripcion.length}/20)"
                    binding.tvError.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvError.visibility = View.GONE
                    viewModel.registrarIntervencion(descripcion)
                }
            }
        }
    }

    private fun observarViewModel() {
        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (!mensaje.isNullOrBlank()) {
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