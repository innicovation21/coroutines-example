package com.kotlinproject.coroutineskotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UnserAdapter: RecyclerView.Adapter<UnserAdapter.ViewHolder>() {

    private var unsereListe: List<Int> = emptyList()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rec_view_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  unsereListe.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvRecViewItem)
            .text = unsereListe[position].toString()
    }

    fun updateUnsereListe(neueListe: ArrayList<Int>){
        unsereListe = neueListe
        notifyDataSetChanged()
    }
}
