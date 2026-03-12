package com.adriangm.motodirex.gestionaverias.ui.listado

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adriangm.motodirex.gestionaverias.R
import com.adriangm.motodirex.gestionaverias.databinding.ItemAveriaBinding
import com.adriangm.motodirex.gestionaverias.model.Averia
import com.adriangm.motodirex.gestionaverias.model.EstadoAveria
import com.adriangm.motodirex.gestionaverias.utils.DateUtils

/**
 * Adapter para el RecyclerView del listado de averías.
 * Recibe una lista de averías y un listener para cuando
 * el usuario pulsa sobre una tarjeta.
 */
class AveriaAdapter(
    private var averias: List<Averia>,
    private val onAveriaClick: (Averia) -> Unit
) : RecyclerView.Adapter<AveriaAdapter.AveriaViewHolder>() {

    /**
     * ViewHolder: representa una sola tarjeta en la lista.
     * Contiene las referencias a los elementos visuales del item_averia.xml
     */
    inner class AveriaViewHolder(
        private val binding: ItemAveriaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(averia: Averia) {
            // Rellenar los datos en la tarjeta
            binding.tvCodigoAveria.text = "AVE-${averia.codigoAveria}"
            binding.tvDescripcion.text  = averia.descInicAveria
            binding.tvMaquina.text      = averia.maquinaria.nombreMaquinaria
            binding.tvFecha.text        = DateUtils.formatear(averia.fechaAsigTecnico)
            binding.tvEstado.text       = averia.estadoAveria.name

            // Color del badge según el estado
            val colorEstado = when (averia.estadoAveria) {
                EstadoAveria.ASIGNADA   ->
                    itemView.context.getColor(R.color.estado_asignada)
                EstadoAveria.ACEPTADA   ->
                    itemView.context.getColor(R.color.estado_aceptada)
                EstadoAveria.FINALIZADA ->
                    itemView.context.getColor(R.color.estado_finalizada)
            }
            binding.tvEstado.backgroundTintList =
                android.content.res.ColorStateList.valueOf(colorEstado)

            // Click en la tarjeta → notificar al Fragment
            binding.root.setOnClickListener { onAveriaClick(averia) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AveriaViewHolder {
        val binding = ItemAveriaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AveriaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AveriaViewHolder, position: Int) {
        holder.bind(averias[position])
    }

    override fun getItemCount(): Int = averias.size

    /**
     * Actualiza la lista cuando cambian los datos
     * (al cambiar de pestaña Nuevas/Recibidas)
     */
    fun actualizarLista(nuevaLista: List<Averia>) {
        averias = nuevaLista
        notifyDataSetChanged()
    }
}