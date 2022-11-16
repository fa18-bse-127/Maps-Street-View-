package com.example.mapsstreetview.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsstreetview.DatabaseHandler.MytrackEntity
import com.example.mapsstreetview.NewDesign.MyTrackRecord_Frag
import com.example.mapsstreetview.NewDesign.MyTracks_Frag
import com.example.mapsstreetview.NewDesign.id
import com.example.mapsstreetview.R

class AdapterMyTrack(val context: MyTracks_Frag, private val mList: List<MytrackEntity>) : RecyclerView.Adapter<AdapterMyTrack.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mytrack_itemview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val list = mList[position]

        holder.title.text=list.title
        holder.date.text=list.date
        holder.distance.text=list.distance
        holder.speed.text=list.speed
        holder.time.text=list.time
        holder.duration.text=list.duration
        holder.img.setImageResource(R.drawable.mytrackimg)
        holder.parentview.setOnClickListener {
//            val intent=Intent(context, MyTrackRecord::class.java)
//            intent.putExtra("id",list.id)
//            context.startActivity(intent)
            if (isLocationOrNetworkEnabled(context.requireContext())) {
                val obj = id()
                obj.id = list.id
                context.replaceFragment(MyTrackRecord_Frag(obj.id))
            }

        }


    }



    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title:TextView=itemView.findViewById(R.id.titleTv)
        val duration: TextView=itemView.findViewById(R.id.durationTv)
        val distance: TextView=itemView.findViewById(R.id.distanceTv)
        val speed: TextView=itemView.findViewById(R.id.speedTv)
        val date:TextView=itemView.findViewById(R.id.dateTv)
        val time: TextView=itemView.findViewById(R.id.timeTv)
        val img:ImageView=itemView.findViewById(R.id.myTrackImg)
        val parentview:CardView=itemView.findViewById(R.id.container_mytrack)

    }
    fun isLocationOrNetworkEnabled(context: Context):Boolean{
        val lm = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            network_enabled= cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        } catch (ex: Exception) {
        }

        if (!gps_enabled) {
            // notify user
            AlertDialog.Builder(context)
                .setMessage("Network or Location not enabled")
                .setPositiveButton("Settings",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        context.startActivity(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    })
                .setNegativeButton("Cancel", null)
                .show()
            return false
        }else
            return true


    }
}