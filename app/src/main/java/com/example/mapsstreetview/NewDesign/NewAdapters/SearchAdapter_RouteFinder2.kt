package com.example.mapsstreetview.NewDesign.NewAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsstreetview.NewDesign.RoutFinder2
import com.example.mapsstreetview.Models.AutoCompleteModel
import com.example.mapsstreetview.R
import com.google.android.gms.maps.model.LatLng

class SearchAdapter_RouteFinder2(val context: RoutFinder2, var dataSet:ArrayList<AutoCompleteModel>) :
    RecyclerView.Adapter<SearchAdapter_RouteFinder2.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.autocomplete_itemview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title= dataSet?.get(position)!!.address.split(",")
        holder.title.text=title[0]
        holder.subtitle.text= dataSet.get(position).address
        val lat= dataSet.get(position).latlng.latitude
        val long= dataSet.get(position).latlng.longitude
        val latlng= LatLng(lat,long)
        holder.container.setOnClickListener {
            val returnIntent= Intent()
            returnIntent.putExtra("latlng",latlng)
            context.newCameraPosition(latlng)
            dataSet.clear()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size!!
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView =view.findViewById(R.id.title_autocomplete)
        val subtitle: TextView =view.findViewById(R.id.subtitle_autocomplete)
        val container: ConstraintLayout =view.findViewById(R.id.container_autocomplete)

    }

}


