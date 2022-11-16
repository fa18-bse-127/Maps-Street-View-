package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_compass_.*
import java.text.SimpleDateFormat
import java.util.*


class Compass_Frag : Fragment(), OnMapReadyCallback, SensorEventListener,IOnBackPressed {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    var value=1

    private var image: ImageView? = null

    // record the compass picture angle turned
    private var currentDegree = 0f

    // device sensor manager
    private var mSensorManager: SensorManager? = null
    var tvHeading: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compass_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Compass"
        converMap_Compass.setOnClickListener {

            when(value){
                1->{ mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    value=2}
                2->{ mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    value=3}
                3->{ mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    value=1}
            }
        }

        zoomIn_compass.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoomOut_compass.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        //
        image = requireView().findViewById<View>(R.id.imageViewCompass) as ImageView
        addressTv_Compass.setText("Loading...")

        // TextView that will tell the user what degree is he heading
        tvHeading = requireView().findViewById<View>(R.id.tvHeading) as TextView

        // initialize your android device sensor capabilities
        mSensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()

        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val dateFormatGmt = SimpleDateFormat("HH:mm:ss")
        dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT")
        val gmtTime=(dateFormatGmt.format(Date()).toString() + "")

        localTime_compass.text="Local "+currentTime
       gmtTime_compass.text="GMT "+gmtTime
        date_compass.text=currentDate
    }

    private fun getStringAddress(latitude: Double, longitude: Double): String {
        val address:String?
        val city:String?
        val geocoder: Geocoder?
        val addresses:List<Address>?

        try{

            geocoder= Geocoder(requireContext(), Locale.getDefault())



            addresses=geocoder.getFromLocation(latitude,longitude,1)

            address=addresses.get(0).getAddressLine(0)
            city=addresses.get(0).locality

            return address+" "+city

        }catch(e: Exception){
        }

        return "No Address Found"

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

                val supportMapFragment = (childFragmentManager.findFragmentById(R.id.map_Compass) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    currentLocation.latitude,
                    currentLocation.longitude
                )
            ) // Sets the center of the map to location user
            .zoom(17f) // Sets the zoom
            .build() // Creates a CameraPosition from the builder

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        googleMap.isMyLocationEnabled=true

        googleMap.setOnCameraIdleListener {
            val center=googleMap.cameraPosition.target
            val address=getStringAddress(center.latitude,center.longitude)
            try {
                addressTv_Compass.text = address
                addressTv_Compass.ellipsize = TextUtils.TruncateAt.MARQUEE;
                addressTv_Compass.isSingleLine = true;
                addressTv_Compass.isSelected = true;
                lat2_compass.setText("${center.longitude} N")
                lng2_compass.setText("${center.longitude} E")

            }catch (e:Exception){}


        }
    }

    override fun onResume() {
        super.onResume()

        // for the system's orientation sensor registered listeners
        mSensorManager!!.registerListener(
            this, mSensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    override fun onPause() {
        super.onPause()

        // to stop the listener and save battery
        mSensorManager!!.unregisterListener(this)
    }


    override fun onSensorChanged(p0: SensorEvent?) {
        // get the angle around the z-axis rotated
        val degree = Math.round(p0!!.values[0]).toFloat()
        if (degree>=0 && degree<=15) {
            tvHeading!!.text = degree.toString()+"`N"
            degree_compass.text = degree.toString()+"`N"
        }else if (degree>=15 && degree<=75) {
            tvHeading!!.text = degree.toString()+"`NE"
            degree_compass.text = degree.toString()+"`NE"
        }else if (degree>=75 && degree<=105) {
            tvHeading!!.text = degree.toString()+"`E"
            degree_compass.text = degree.toString()+"`E"
        }else if (degree>=105 && degree<=165) {
            tvHeading!!.text = degree.toString()+"`SE"
            degree_compass.text = degree.toString()+"`SE"
        }else if (degree>=165 && degree<=195) {
            tvHeading!!.text = degree.toString()+"`S"
            degree_compass.text = degree.toString()+"`S"
        }else if (degree>=195 && degree<=255) {
            tvHeading!!.text = degree.toString()+"`SW"
            degree_compass.text = degree.toString()+"`SW"
        }else if (degree>=255 && degree<=285) {
            tvHeading!!.text = degree.toString()+"`W"
            degree_compass.text = degree.toString()+"`W"
        }else if (degree>=285 && degree<=344) {
            tvHeading!!.text = degree.toString()+"`NW"
            degree_compass.text = degree.toString()+"`NW"
        }
        // create a rotation animation (reverse turn degree degrees)
        val ra = RotateAnimation(
            currentDegree,
            -degree,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        updateCamera(degree)

        // how long the animation will take place
        ra.duration = 210

        // set the animation after the end of the reservation status
        ra.fillAfter = true

        // Start the animation
        image?.startAnimation(ra)
        currentDegree = -degree


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
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

    fun updateCamera(bearing: Float) {
        try {
            val currentPlace = CameraPosition.Builder()
                .target(LatLng(currentLocation.latitude, currentLocation.longitude))
                .bearing(bearing).tilt(0f).zoom(18f).build()
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace))
        }catch (e : Exception){
            Log.d("thisss", e.toString())
        }

    }


}