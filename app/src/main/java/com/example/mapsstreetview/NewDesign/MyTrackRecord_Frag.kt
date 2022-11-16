package com.example.mapsstreetview.NewDesign

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mapsstreetview.DatabaseHandler.MytrackEntity
import com.example.mapsstreetview.DatabaseHandler.RoomAppDb
import com.example.mapsstreetview.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

import kotlinx.android.synthetic.main.fragment_my_track_record_.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyTrackRecord_Frag(recordId: Int) : Fragment(),OnMapReadyCallback,IOnBackPressed{
    private lateinit var mMap: GoogleMap
    lateinit var database: RoomAppDb
    lateinit var track: MytrackEntity
    var value=1
    val recordId=recordId


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_track_record_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="My Track"

        convertButton_mytracksfrag.setOnClickListener {


            when(value){
                1->{ mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    value=2}
                2->{ mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    value=3}
                3->{ mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    value=1}
            }

        }

        zoomIn_mytrackfrag.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoomOut_mytrackfrag.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }


        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment_myTrackRecordFrag) as SupportMapFragment
        mapFragment.getMapAsync(this)

        database= RoomAppDb.getAppDatabase(requireContext())!!

        val id =recordId
        CoroutineScope(Dispatchers.IO).launch {
            track = database.mytrackDao()?.getTrackbyId(id)!!

            CoroutineScope(Dispatchers.Main).launch {
                timeTv_myTrackRecord.text=track.duration
                speedTv_myTrackRecord.text=track.speed
                distanceTv_myTrackRecord.text=track.distance

            }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    track.trackList[0].latitude,
                    track.trackList[0].longitude
                )
            ) // Sets the center of the map to location user
            .zoom(25f) // Sets the zoom
            .build() // Creates a CameraPosition from the builder

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        val latLng = LatLng(track.trackList[0].latitude,  track.trackList[0].longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Start Position")
        googleMap.addMarker(markerOptions)!!

//        val latLng2 = LatLng(track.trackList[track.trackList.size-2].latitude,  track.trackList[track.trackList.size-1].longitude)
//        val markerOptions2 = MarkerOptions().position(latLng2).title("Stop Position")
//        googleMap.addMarker(markerOptions2)!!

        googleMap.isMyLocationEnabled=true
        drawRoute(track.trackList)

    }

    private fun drawRoute(locations: List<LatLng>) {

        val polylineOptions = PolylineOptions()

        mMap.clear()

        val points = polylineOptions.points
        points.addAll(locations)


        mMap.addPolyline(polylineOptions)
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