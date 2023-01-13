package com.example.retobootcampmobilesophos2022.viewModel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.retobootcampmobilesophos2022.R
import com.example.retobootcampmobilesophos2022.model.Documents
import com.example.retobootcampmobilesophos2022.viewModel.network.dataItemsResponse

class Adapter (val document: Documents, val onClickListener:  (dataItemsResponse) -> Unit):
    RecyclerView.Adapter<viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
        return viewHolder(view.inflate(R.layout.item_documents, parent, false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val doc = document.Items[position]
        return holder.bindView(doc, onClickListener)
    }

    override fun getItemCount(): Int = document.Items.size

}

class viewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

    private val tvDate : TextView = itemView.findViewById(R.id.date)
    private val tvTypeAttached : TextView = itemView.findViewById(R.id.typeAttached)
    private val tvName : TextView = itemView.findViewById(R.id.name)

        @SuppressLint("SetTextI18n")
        fun bindView(document: dataItemsResponse, onClickListener: (dataItemsResponse) -> Unit){

            tvDate.text = document.Fecha.take(10)
            tvTypeAttached.text = " - " + document.TipoAdjunto
            tvName.text = document.Nombre + " " + document.Apellido

        itemView.setOnClickListener{
            onClickListener(document)
            Toast.makeText(itemView.context, "Seleccionaste a ${document.Fecha.take(10) + " - " + 
                    document.TipoAdjunto + " / " + document.Nombre + " " + document.Apellido}",
                Toast.LENGTH_SHORT).show()
        }
    }

}



