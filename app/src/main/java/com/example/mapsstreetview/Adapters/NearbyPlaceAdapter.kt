package com.example.mapsstreetview.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mapsstreetview.R
import java.util.*
import android.widget.Filter;
import com.example.mapsstreetview.Models.NearbyPlaceViewModel
import kotlin.collections.ArrayList

class NearbyPlaceAdapter(
    // on below line we are creating two
    // variables for course list and context
    public var placeList: ArrayList<NearbyPlaceViewModel>,
    private val context: Context
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var placeTv: TextView
    private lateinit var placeIv: ImageView

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return placeList.size
    }

    fun filterList(filterlist: ArrayList<NearbyPlaceViewModel>?) {
        // below line is to add our filtered
        // list in our course array list.
        placeList = filterlist!!
        Log.d("Sho__", "filterList: ")
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.nearby_places_item_view, null)
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        placeIv = convertView!!.findViewById(R.id.nearbyPlaceImage)
        placeTv = convertView!!.findViewById(R.id.nearbyPlaceName)
        // on below line we are setting image for our course image view.
        placeIv.setImageResource(placeList.get(position).img)
        // on below line we are setting text in our course text view.
        placeTv.setText(placeList.get(position).name)
        // at last we are returning our convert view.
        return convertView
    }


    fun getFilter(): Filter {
        return Searched_Filter
    }
     var abc=0;
     var efg=0;
    val filteredList: ArrayList<NearbyPlaceViewModel> = ArrayList()
    private val Searched_Filter: Filter = object : Filter() {
         override fun performFiltering(constraint: CharSequence?): FilterResults? {

            if (constraint!!.isEmpty() || constraint.isEmpty()) {
                filteredList.clear()
//                filteredList.addAll(placeList)
                this@NearbyPlaceAdapter.notifyDataSetChanged()
                Log.d("abc__", "performFiltering: if condition is==== "+ abc++)

            } else {
                filteredList.clear()
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in placeList) {
                    if (item.name.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(item)

                    }

                    this@NearbyPlaceAdapter.notifyDataSetChanged()
                }
                Log.d("abc__", "performFiltering: else condition is===="+ efg++)
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults) {
            placeList.clear()
            placeList.addAll(filteredList)
            this@NearbyPlaceAdapter.notifyDataSetChanged()
        }

        /*    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
               placeList.clear()
               placeList.addAll(results.values as ArrayList<NearbyPlaceViewModel>)
               this@NearbyPlaceAdapter.notifyDataSetChanged()
           }*/
    }
}