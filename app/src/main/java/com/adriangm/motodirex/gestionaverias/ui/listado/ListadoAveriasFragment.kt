package com.adriangm.motodirex.gestionaverias.ui.listado

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adriangm.motodirex.gestionaverias.R
import com.adriangm.motodirex.gestionaverias.databinding.FragmentListadoAveriasBinding
import com.adriangm.motodirex.gestionaverias.viewmodel.ListadoViewModel
import com.google.android.material.tabs.TabLayout

class ListadoAveriasFragment : Fragment() {

    private var _binding: FragmentListadoAveriasBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListadoViewModel
    private lateinit var adapter: AveriaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListadoAveriasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ListadoViewModel::class.java]

        configurarRecyclerView()
        configurarTabs()
        configurarBuscador()
        observarViewModel()

        // Restaurar la pestaña activa al volver del detalle
        viewModel.tabActivo.value?.let { tabPos ->
            binding.tabLayout.getTabAt(tabPos)?.select()
        }
    }

    private fun configurarRecyclerView() {
        adapter = AveriaAdapter(emptyList()) { averia ->
            val bundle = Bundle().apply {
                putInt("averiaId", averia.codigoAveria)
            }
            findNavController().navigate(R.id.action_listado_to_detalle, bundle)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun configurarTabs() {
        actualizarContadores()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Limpiar buscador al cambiar de pestaña
                binding.etBusqueda.setText("")
                when (tab?.position) {
                    0 -> viewModel.cargarAveriasNuevas()
                    1 -> viewModel.cargarAveriasRecibidas()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun configurarBuscador() {
        binding.etBusqueda.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.buscar(s.toString())
            }
        })
    }

    private fun actualizarContadores() {
        val contadorNuevas    = viewModel.getContadorNuevas()
        val contadorRecibidas = viewModel.getContadorRecibidas()
        binding.tabLayout.getTabAt(0)?.text = "Nuevas ($contadorNuevas)"
        binding.tabLayout.getTabAt(1)?.text = "Recibidas ($contadorRecibidas)"
    }

    private fun observarViewModel() {
        viewModel.averias.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.tvSinAverias.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvSinAverias.visibility = View.GONE
                adapter.actualizarLista(lista)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}