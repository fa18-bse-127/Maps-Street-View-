package com.example.mapsstreetview.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsstreetview.DatabaseHandler.RecordEntity
import com.example.mapsstreetview.DatabaseHandler.RoomAppDb
import com.example.mapsstreetview.R
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class Adapter(val context: Context, val list: ArrayList<RecordEntity>):RecyclerView.Adapter<Adapter.ViewHolder>() {

    val database=RoomAppDb.getAppDatabase(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_route_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val listCursor = list[position]


        // sets the image to the imageview from our itemHolder class
        holder.dateTime.setText(listCursor.dateTime)

        // sets the text to the textview from our itemHolder class
        holder.source.text = listCursor.src

        holder.destination.text=listCursor.des

        holder.mapBtn.setOnClickListener {

            showDialog(listCursor)

        }

        holder.deleteBtn.setOnClickListener {

           /* database= RoomAppDb.getAppDatabase(context)!!*/

            GlobalScope.launch {
                database?.recordDao()
                    ?.deleteRecord(listCursor.id)
                Log.d("thisss", "deleted")

            }
            updateList(position)

        }

        holder.shareBtn.setOnClickListener {
            val src=listCursor.src
            val des=listCursor.des
            val srcAddress=getLocationFromAddress(src)
            val desAddress=getLocationFromAddress(des)
            val uri = "http://maps.google.com/maps?saddr=${srcAddress?.latitude},${srcAddress?.longitude}&daddr=${desAddress?.latitude},${desAddress?.longitude}"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
            context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
    }

    fun showDialog(listCursor:RecordEntity) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog)
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_no) as Button
        yesBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=${listCursor.src}&daddr=${listCursor.des}")
            )
            context.startActivity(intent)

        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(position:Int) {
        list.removeAt(position)
        this.notifyDataSetChanged()
    }

    fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            location.getLatitude()
            location.getLongitude()
            p1 = GeoPoint(
                (location.getLatitude() ) ,
                (location.getLongitude() )
            )
            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val dateTime: TextView = itemView.findViewById(R.id.dateTimeTv)
        val source: TextView = itemView.findViewById(R.id.sourceAdressTv)
        val destination: TextView =itemView.findViewById(R.id.destinationAddress)
        val shareBtn:ImageView=itemView.findViewById(R.id.sharebtn_savedRoutes)
        val deleteBtn:ImageView=itemView.findViewById(R.id.deleteBtn_savedRoutes)
        val mapBtn:ImageView=itemView.findViewById(R.id.mapbtn_savedRoutes)

    }
}