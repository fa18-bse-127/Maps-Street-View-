package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsstreetview.DatabaseHandler.PrefConfig
import com.example.mapsstreetview.Models.AutoCompleteModel
import com.example.mapsstreetview.NewDesign.NewAdapters.SearchAdapter_LiveSatelliteView
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.fragment_live_satellite_view.*
import kotlinx.coroutines.*
import java.util.*


class LiveSatelliteView : Fragment(), OnMapReadyCallback, IOnBackPressed {
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    var value = 1
    val pref = PrefConfig()
    var location: ArrayList<Address> = ArrayList()
    lateinit var adapter: SearchAdapter_LiveSatelliteView
    var dummmy = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for requireContext() fragment


        return inflater.inflate(R.layout.fragment_live_satellite_view, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "Live Satellite View"
        convertMap.setOnClickListener {

            when (value) {
                1 -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                    value = 2
                }
                2 -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                    value = 3
                }
                3 -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                    value = 1
                }
            }
        }

        zoomIn.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoom_out.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        trafficBtn_LiveSatelliteView.setOnClickListener {
            if (dummmy) {
                mMap.isTrafficEnabled = false
                trafficBtn_LiveSatelliteView.setImageResource(R.drawable.trafficimg)
                dummmy = false
            } else {
                mMap.isTrafficEnabled = true
                dummmy = true
                trafficBtn_LiveSatelliteView.setImageResource(R.drawable.traffic_btn_pressed_state)
            }
        }

        if (addressTxtView1 == null || addressTxtView1.text.isEmpty()) {
            addressTxtView1.text = "Loading...."
        }





        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fetchLocation()
        val dialog = Dialog(requireContext())
        mapStreetViewBtn.setOnClickListener {

            showDialog()


        }



        searchView_LiveSatelliteView.isIconified = false
        searchView_LiveSatelliteView.isIconifiedByDefault = false
        searchView_LiveSatelliteView.isSubmitButtonEnabled = true
        searchView_LiveSatelliteView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                var model: ArrayList<AutoCompleteModel>? = ArrayList<AutoCompleteModel>()
                try {
                    model = getLocationFromAddress(p0)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }

                if (model != null) {
                    recyclerview_liveSatelliteView.layoutManager =
                        LinearLayoutManager(requireContext())
                    adapter = SearchAdapter_LiveSatelliteView(this@LiveSatelliteView, model)
                    recyclerview_liveSatelliteView.adapter = adapter


                } else
                    Toast.makeText(requireContext(), "No place Found", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (p0!!.isEmpty()) {
                    val model1 = ArrayList<AutoCompleteModel>()
                    adapter = SearchAdapter_LiveSatelliteView(this@LiveSatelliteView, model1)
                    recyclerview_liveSatelliteView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                return false
            }

        })

    }

    fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog)
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_no) as Button
        yesBtn.setOnClickListener {
            val latlong = pref.readLatLong2(requireContext()).split(",").toTypedArray()
            val latitude = latlong[0]
            val longitude = latlong[1]

            val gmmIntentUri = Uri.parse("google.streetview:cbll=${latitude},${longitude}")
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

    private fun getLocationFromAddress(p0: String?): ArrayList<AutoCompleteModel>? {

        val coder = Geocoder(requireContext())
        var p1: GeoPoint? = null
        var title: String
        val modelArray: ArrayList<AutoCompleteModel> = ArrayList<AutoCompleteModel>()
        var location: ArrayList<Address> = ArrayList()


        val address: MutableList<Address>? = coder.getFromLocationName(p0, 10000)
        if (address == null) {
            Toast.makeText(requireContext(), "Please Enter Any Address", Toast.LENGTH_SHORT).show()
            return null
        }

        if (address.isNotEmpty()) {
            Log.d("thisss___", address.toString())
            for (i in address) {
                location.add(i)
            }

            modelArray.clear()

            Log.d("thisss___", location[0].getAddressLine(0))
            Log.d("thisss___", location.size.toString())
//            val location: Address = address[0]
            for (i in 0 until location.size) {
                p1 = GeoPoint((location[i].latitude), (location[i].longitude))
                title = location[i].getAddressLine(0)
                modelArray.add(AutoCompleteModel(p1, title))
                Log.d("thisss___", modelArray.toString())
            }
            Log.d("check__", modelArray.toString())
            return modelArray
        } else {
            Log.d("check__", "getLocationFromAddress: else")

            return null
        }

    }


    fun newCameraPosition(latlng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    latlng.latitude,
                    latlng.longitude
                )
            ) // Sets the center of the map to location user
            .zoom(17f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                val latLng = data?.getStringExtra("latlng")?.split(",")?.toTypedArray()!!
                val sourceLatitude = latLng[0]
                val sourceLongitude = latLng[1]
                Log.d(
                    "requireContext()___",
                    "onActivityResult: " + sourceLongitude + sourceLongitude
                )
                val cameraPosition = CameraPosition.Builder()
                    .target(
                        LatLng(
                            sourceLatitude.toDouble(),
                            sourceLongitude.toDouble()
                        )
                    ) // Sets the center of the map to location user
                    .zoom(17f) // Sets the zoom
                    .bearing(90f) // Sets the orientation of the camera to east
                    .tilt(40f) // Sets the tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

    }


    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it

                val supportMapFragment =
                    (childFragmentManager.findFragmentById(R.id.mapFragment_LiveSatelliteView) as
                            SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this)
            }
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    43.092461,
                    -79.047150
                )
            ) // Sets the center of the map to location user
            .zoom(17f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder

        p0.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)




        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        p0.isMyLocationEnabled = true




        p0.setOnCameraIdleListener {
            val center = p0.cameraPosition.target
            val latLng = "${center.latitude}," + "${center.longitude}"
            pref.writeLatLong2(requireContext(), latLng)

             val viewModelJob = Job()
             val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

            uiScope.launch {
                withContext(Dispatchers.IO) {
                    //Do background tasks...
                    val address = getStringAddress(center.latitude, center.longitude)
                    withContext(Dispatchers.Main){
                        //Update UI
                        addressTxtView1.text = address
                    }
                }


            }










        }
    }

    private fun getStringAddress(latitude: Double, longitude: Double): String {
        val address: String?
        val city: String?
        val geocoder: Geocoder?
        val addresses: List<Address>?

        try {

            geocoder = Geocoder(requireContext(), Locale.getDefault())

            Log.d(
                "requireContext()ss",
                "addresses  " + "  latitude  " + latitude + "  longitude  " + longitude
            )

            addresses = geocoder.getFromLocation(latitude, longitude, 1)

            address = addresses.get(0).getAddressLine(0)
            city = addresses.get(0).locality

            return address + " " + city

        } catch (e: Exception) {
        }

        return "No Address Found"

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
            }
        }
    }


    override fun onBackPressed(): Boolean {
        replaceFragment(MainScreenFrag())
        return true
    }

    fun replaceFragment(fragment: Fragment) {


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()

    }


}