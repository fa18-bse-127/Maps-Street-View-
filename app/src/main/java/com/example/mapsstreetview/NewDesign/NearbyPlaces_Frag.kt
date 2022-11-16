package com.example.mapsstreetview.NewDesign

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import com.example.mapsstreetview.Adapters.NearbyPlaceAdapter
import com.example.mapsstreetview.Models.NearbyPlaceViewModel
import com.example.mapsstreetview.R
import kotlinx.android.synthetic.main.fragment_nearby_places_.*
import java.util.*

class NearbyPlaces_Frag : Fragment(),IOnBackPressed {

    lateinit var NearbyPlaceGridView: GridView
    lateinit var placeList: ArrayList<NearbyPlaceViewModel>
    lateinit var placeAdapter: NearbyPlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby_places_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Nearby Places"
        // initializing variables of grid view with their ids.
        NearbyPlaceGridView = requireView().findViewById(R.id.gridViewNearbyPlaces)
        placeList = ArrayList<NearbyPlaceViewModel>()


        // on below line we are adding data to
        // our course list with image and course name.
        placeList = (placeList + NearbyPlaceViewModel("Hospitals", R.drawable.hospital)) as ArrayList<NearbyPlaceViewModel>
        placeList = (placeList + NearbyPlaceViewModel("Hotels", R.drawable.hotel)) as ArrayList<NearbyPlaceViewModel>
        placeList = (placeList + NearbyPlaceViewModel("Mosques", R.drawable.mosque)) as ArrayList<NearbyPlaceViewModel>
        placeList = (placeList + NearbyPlaceViewModel("Airports", R.drawable.airports)) as ArrayList<NearbyPlaceViewModel>
        placeList = (placeList + NearbyPlaceViewModel("Banks", R.drawable.banks)) as ArrayList<NearbyPlaceViewModel>
        placeList = (placeList + NearbyPlaceViewModel("ATM", R.drawable.atms)) as ArrayList<NearbyPlaceViewModel>
        placeList = (placeList + NearbyPlaceViewModel("Post Office", R.drawable.postoffices)) as ArrayList<NearbyPlaceViewModel>
        placeList = (placeList + NearbyPlaceViewModel("Schools", R.drawable.schools)) as ArrayList<NearbyPlaceViewModel>
        placeList += NearbyPlaceViewModel("University", R.drawable.universitites)
        placeList = (placeList + NearbyPlaceViewModel("Fire Station", R.drawable.firestations)) as ArrayList<NearbyPlaceViewModel>
        placeList += NearbyPlaceViewModel("Police Station", R.drawable.police_station)
        placeList +=  NearbyPlaceViewModel("Parks", R.drawable.parks)
        placeList +=  NearbyPlaceViewModel("Bakery", R.drawable.bakery)
        placeList += NearbyPlaceViewModel("Cafe", R.drawable.cafe)
        placeList +=  NearbyPlaceViewModel("Service Station", R.drawable.service_station)
        placeList +=  NearbyPlaceViewModel("Church", R.drawable.church)
        placeList +=  NearbyPlaceViewModel("Garments Store", R.drawable.garments_store)
        placeList += NearbyPlaceViewModel("Dentist", R.drawable.dentist)
        placeList +=  NearbyPlaceViewModel("Doctor", R.drawable.doctor)
        placeList +=  NearbyPlaceViewModel("Fuel Station", R.drawable.fuel_station)
        placeList += NearbyPlaceViewModel("Beauty Salons", R.drawable.salon)
        placeList += NearbyPlaceViewModel("Jewelry Store", R.drawable.jewelarry_store)
        placeList +=  NearbyPlaceViewModel("Pet Store", R.drawable.per_store)
        placeList +=  NearbyPlaceViewModel("Pharmacy", R.drawable.pharmacy)
        placeList += NearbyPlaceViewModel("Shoe Store", R.drawable.shoe_store)
        placeList += NearbyPlaceViewModel("Shopping Mall", R.drawable.shopping_mall)
        placeList += NearbyPlaceViewModel("Subway Station", R.drawable.subway_station)
        placeList +=   NearbyPlaceViewModel("Zoo", R.drawable.zoo)
        placeList +=  NearbyPlaceViewModel("Stadium", R.drawable.stadiun)
        placeList += NearbyPlaceViewModel("Bus Stop", R.drawable.bus_stop)
        placeList += NearbyPlaceViewModel("Bar", R.drawable.bar)
        placeList +=  NearbyPlaceViewModel("Temple", R.drawable.temple)
        placeList +=  NearbyPlaceViewModel("Cinema", R.drawable.cinema)




        placeAdapter = NearbyPlaceAdapter(placeList as ArrayList<NearbyPlaceViewModel>,requireContext())

        // on below line we are setting adapter to our grid view.
        NearbyPlaceGridView.adapter = placeAdapter



        svSearchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override  fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override  fun onQueryTextChange(newText: String?): Boolean {

                filter(newText)


                return false
            }

        })
        // on below line we are adding on item
        // click listener for our grid view.
        NearbyPlaceGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying
            // a toast message with course name.

            showDialog(position)

        }
    }

    fun showDialog(position:Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog)
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_no) as Button
        yesBtn.setOnClickListener {
            val list=placeAdapter.placeList[position]
            val name=list.name
            val gmmIntentUri = Uri.parse("geo:0,0?q=${name}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
            dialog.dismiss()

        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun filter(text: String?) {

        // creating a new array list to filter our data.
        val filteredlist: ArrayList<NearbyPlaceViewModel> = ArrayList<NearbyPlaceViewModel>()
        for (item in placeList) {
            if (item.name.lowercase(Locale.getDefault())
                    .contains(text!!.lowercase(Locale.getDefault()))) {
                filteredlist.add(item)
            }

        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireContext(),"No data Found", Toast.LENGTH_SHORT).show()
        } else {

            placeAdapter.filterList(filteredlist)

        }
    }

    override fun onBackPressed(): Boolean {
        replaceFragment(MainScreenFrag())
        return true
    }

    public fun replaceFragment(fragment: Fragment) {


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()

    }
}