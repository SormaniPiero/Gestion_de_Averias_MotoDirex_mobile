package com.adriangm.motodirex.gestionaverias.ui.listado

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adriangm.motodirex.gestionaverias.R
import com.adriangm.motodirex.gestionaverias.data.network.dto.AveriaDto
import com.adriangm.motodirex.gestionaverias.databinding.ItemAveriaBinding

class AveriaAdapter(
    private var averias: List<AveriaDto>,
    private val onAveriaClick: (AveriaDto) -> Unit
) : RecyclerView.Adapter<AveriaAdapter.AveriaViewHolder>() {

    inner class AveriaViewHolder(
        private val binding: ItemAveriaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(averia: AveriaDto) {
            binding.tvCodigoAveria.text = "AVE-${averia.codigoAveria}"
            binding.tvDescripcion.text  = averia.descripcion
            binding.tvMaquina.text      = "Máquina #${averia.maquinariaFK}"
            binding.tvFecha.text        = ""
            binding.tvEstado.text       = averia.estado

            val colorEstado = when (averia.estado) {
                "ASIGNADA"   -> itemView.context.getColor(R.color.estado_asignada)
                "EN_PROCESO" -> itemView.context.getColor(R.color.estado_aceptada)
                "FINALIZADA" -> itemView.context.getColor(R.color.estado_finalizada)
                else         -> itemView.context.getColor(R.color.estado_asignada)
            }
            binding.tvEstado.backgroundTintList =
                android.content.res.ColorStateList.valueOf(colorEstado)

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

    fun actualizarLista(nuevaLista: List<AveriaDto>) {
        averias = nuevaLista
        notifyDataSetChanged()
    }
}