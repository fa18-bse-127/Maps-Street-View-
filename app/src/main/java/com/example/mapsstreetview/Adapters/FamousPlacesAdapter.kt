package com.example.mapsstreetview.Adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsstreetview.Models.ItemsViewModel
import com.example.mapsstreetview.R
import com.google.android.gms.maps.model.LatLng

class FamousPlacesAdapter(val context: Context,private var mList: List<ItemsViewModel>) : RecyclerView.Adapter<FamousPlacesAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.name

        holder.parent.setOnClickListener {

                val latlng = LatLng(ItemsViewModel.latLng.latitude, ItemsViewModel.latLng.longitude)
                showDialog(latlng)


        }
    }

    fun showDialog(latLng: LatLng) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog)
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_no) as Button
        yesBtn.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.streetview:cbll=${latLng.latitude},${latLng.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
            dialog.dismiss()
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    fun filterList(filteredlist: ArrayList<ItemsViewModel>) {

        mList = filteredlist!!
        Log.d("Sho__", "filterList: ")
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()

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

        if (!gps_enabled || !network_enabled) {
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

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.famousPlaceImg)
        val textView: TextView = itemView.findViewById(R.id.famousPlaceName)
        val parent: ImageView=itemView.findViewById(R.id.famousPlaceImg)
    }
}