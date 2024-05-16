package com.jarproductions.projecte_teapptre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarproductions.projecte_teapptre.databinding.ObraItemBinding

class ObraAdapter(
    private var obraList: List<Obra>,
    private val listener: OnObraClickListener
) : RecyclerView.Adapter<ObraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ObraItemBinding.inflate(inflater, parent, false)
        return ObraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ObraViewHolder, position: Int) {
        val obra = obraList[position]
        holder.bind(obra)
        holder.itemView.setOnClickListener {
            listener.onObraClick(obra)
        }
    }

    override fun getItemCount(): Int {
        return obraList.size
    }

    fun setData(newObraList: List<Obra>) {
        obraList = newObraList
        notifyDataSetChanged()
    }
    interface OnObraClickListener {
        fun onObraClick(obra: Obra)
    }

}


