package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.mapsstreetview.DatabaseHandler.MytrackEntity
import com.example.mapsstreetview.DatabaseHandler.RoomAppDb
import com.example.mapsstreetview.LocationTracker.MapPresenter
import com.example.mapsstreetview.LocationTracker.Ui
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_live_traffic_.*

import kotlinx.android.synthetic.main.fragment_location_tracker_.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LocationTracker_Frag : Fragment(),OnMapReadyCallback,IOnBackPressed {

    private lateinit var map: GoogleMap
    lateinit var database: RoomAppDb
    lateinit var userPath: List<LatLng>
    var value=1
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var presenter:MapPresenter
    private val permissionCode = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter= MapPresenter(requireActivity() as AppCompatActivity)
        return inflater.inflate(R.layout.fragment_location_tracker_, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Location Tracker"

        converButton_LocationTracker.setOnClickListener {


            when(value){
                1->{ map.mapType=GoogleMap.MAP_TYPE_HYBRID
                    value=2}
                2->{ map.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    value=3}
                3->{ map.mapType=GoogleMap.MAP_TYPE_NORMAL
                    value=1}
            }

        }

        zoomIn_locationTracker.setOnClickListener {
            map.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoomOut_locationTracker.setOnClickListener {
            map.animateCamera(CameraUpdateFactory.zoomOut())
        }
        var dummmy = false
        trafficBtn_locationTracker.setOnClickListener {

            if (dummmy) {
                map.isTrafficEnabled = false
                trafficBtn_locationTracker.setImageResource(R.drawable.trafficimg)
                dummmy=false
            }else{
                map.isTrafficEnabled=true
                trafficBtn_locationTracker.setImageResource(R.drawable.traffic_btn_pressed_state)
                dummmy=true
            }
        }


        textView7.isVisible=false
        textView8.isVisible=false

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fetchLocation()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        btnStartStop.setOnClickListener {
            if (btnStartStop.text == getString(R.string.start_label)) {
                startTracking()
                btnStartStop.setText(R.string.stop_label)
                textView7.isVisible=true
                textView8.isVisible=true
            } else {
                stopTracking()
                btnStartStop.setText(R.string.start_label)
                textView7.isVisible=false
                textView8.isVisible=false

                database= RoomAppDb.getAppDatabase(requireContext())!!

                val duration=txtTime.text.toString().replace(":",".")
                val distance=txtDistance.text.toString()
                val speed=txtPace.text.toString()
                val sdf = SimpleDateFormat("dd/M/yyyy | hh:mm:ss")
                val currentDateTime = sdf.format(Date())
                val dateTime=currentDateTime.split("|")
                val date=dateTime[0]
                val time=dateTime[1]
                val options=userPath

                GlobalScope.launch {
                    database.mytrackDao()
                        ?.insertTrack(MytrackEntity(0,date,duration,distance,speed,date,time,options))
                }

            }
        }

        presenter.onViewCreated()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it

                val mapFragment = childFragmentManager
                    .findFragmentById(R.id.mapLocationTracker) as SupportMapFragment
                mapFragment.getMapAsync(this)
                address_LocationTracker.setText(getStringAddress(currentLocation.latitude,currentLocation.longitude))
            }
        }
    }

    private fun getStringAddress(latitude: Double, longitude: Double): String {
        val address:String?
        val city:String?
        val geocoder: Geocoder?
        val addresses:List<Address>?

        try{

            geocoder= Geocoder(requireContext(), Locale.getDefault())

            Log.d("requireContext()ss", "addresses  "+"  latitude  "+latitude+"  longitude  "+ longitude)

            addresses=geocoder.getFromLocation(latitude,longitude,1)

            address=addresses.get(0).getAddressLine(0)
            city=addresses.get(0).locality

            return address+" "+city

        }catch(e: Exception){
        }

        return "No Address Found"

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    currentLocation.latitude,
                    currentLocation.longitude
                )
            ) // Sets the center of the map to location user
            .zoom(17f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap.isMyLocationEnabled=true

        presenter.ui.observe(this) { ui ->
            updateUi(ui)
        }

        presenter.onMapLoaded()


    }

    private fun startTracking() {
        txtPace.text = ""
        txtDistance.text = ""
        txtTime.base = SystemClock.elapsedRealtime()
        txtTime.start()
        map.clear()

        presenter.startTracking()

    }

    private fun stopTracking() {
        presenter.stopTracking()
        txtTime.stop()
    }

    @SuppressLint("MissingPermission")
    private fun updateUi(ui: Ui) {



        if (ui.currentLocation != null && ui.currentLocation != map.cameraPosition.target) {
            map.isMyLocationEnabled = true
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.currentLocation, 14f))
        }
        txtDistance.text = ui.formattedDistance
        if (txtDistance.text.toString().isNotEmpty() && txtTime.text.toString().isNotEmpty()) {
            val distancetxt = (txtDistance.text.toString())
            val distance = distancetxt.toFloat()
            val timetxt = txtTime.text.toString().replace(":",".")
            val time = timetxt.toFloat()
            val pace = (distance / time)*36
            if(pace>1000000000 || pace.isNaN()){
                txtPace.text="0"
            }else {
                txtPace.text = (pace.toString())
                drawRoute(ui.userPath)
            }
        }else {

            drawRoute(ui.userPath)

        }
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions()

        map.clear()

        val points = polylineOptions.points
        points.addAll(locations)
        userPath=points

        map.addPolyline(polylineOptions)
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