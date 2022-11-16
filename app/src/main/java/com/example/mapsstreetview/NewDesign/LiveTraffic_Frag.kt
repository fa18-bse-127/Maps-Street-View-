package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_live_traffic_.*


class LiveTraffic_Frag : Fragment(),OnMapReadyCallback,IOnBackPressed {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private val permissionCode = 101
    var value=1
    var dummmy=true
    var dummy1=true
    lateinit var latlng1: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_traffic_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Live Traffic"


        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()
        converButton_LiveTraffic.setOnClickListener {
            when(value){
                1->{ mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    value=2}
                2->{ mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    value=3}
                3->{ mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    value=1}
            }
        }

        zoomIn_liveTraffic.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }
        zoomOut_liveTraffic.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        trafficBtn_liveTraffic.setImageResource(R.drawable.traffic_btn_pressed_state)
        trafficBtn_liveTraffic.setOnClickListener {
            if (dummmy) {
                mMap.isTrafficEnabled = false
                dummmy=false
                trafficBtn_liveTraffic.setImageResource(R.drawable.trafficimg)
            }else{
                mMap.isTrafficEnabled=true
                dummmy=true
                trafficBtn_liveTraffic.setImageResource(R.drawable.traffic_btn_pressed_state)
            }
        }

        dimensionBtn_liveTraffic.setOnClickListener {
            if (dummy1) {
                dimensionBtn_liveTraffic.setImageResource(R.drawable._d)
                val cameraPosition = CameraPosition.Builder()
                    .target(
                        LatLng(
                            latlng1.latitude,
                            latlng1.longitude
                        )
                    ) // Sets the center of the map to location user
                    .zoom(17f) // Sets the zoom
                    .tilt(90f)
                    .bearing(17f)
                    .build() // Creates a CameraPosition from the builder

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                dummy1=false
            }else{
                val cameraPosition = CameraPosition.Builder()
                    .target(
                        LatLng(
                            latlng1.latitude,
                            latlng1.longitude
                        )
                    ) // Sets the center of the map to location user
                    .zoom(17f) // Sets the zoom
                    .tilt(0f)
                    .bearing(0f)
                    .build() // Creates a CameraPosition from the builder

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                dimensionBtn_liveTraffic.setImageResource(R.drawable._d_btn)
                dummy1=true
            }
        }

        streetViewBtn_liveTraffic.setOnClickListener {

            val gmmIntentUri = Uri.parse("google.streetview:cbll=${latlng1.latitude},${latlng1.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun fetchLocation()  {
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
                latlng1= LatLng(it.latitude,it.longitude)

                val supportMapFragment = (childFragmentManager.findFragmentById(R.id.liveTrafficMap) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        p0.isTrafficEnabled=true
        p0.isMyLocationEnabled=true
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
                )
            ) // Sets the center of the map to location user
            .zoom(17f) // Sets the zoom
            .build() // Creates a CameraPosition from the builder

        p0.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        p0.setOnCameraIdleListener {
            val center=p0.cameraPosition.target
            latlng1=LatLng(center.latitude,center.longitude)

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