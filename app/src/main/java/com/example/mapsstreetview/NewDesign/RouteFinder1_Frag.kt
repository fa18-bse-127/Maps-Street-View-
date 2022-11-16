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
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mapsstreetview.DatabaseHandler.RecordEntity
import com.example.mapsstreetview.DatabaseHandler.RoomAppDb
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_route_finder1_.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class RouteFinder1_Frag : Fragment(), OnMapReadyCallback,IOnBackPressed {

    private lateinit var mMap: GoogleMap
    lateinit var latLng: Array<String>
    lateinit var sourceLatitude:String
    lateinit var sourceLongitude:String
    lateinit var destinationLatitude:String
    lateinit var destinationLongitude:String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private val permissionCode = 101
    lateinit var database: RoomAppDb
    var value=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_route_finder1_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Route Finder"
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()

        converButton_Route1.setOnClickListener {

            when(value){
                1->{ mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    value=2}
                2->{ mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    value=3}
                3->{ mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    value=1}
            }
        }

        zoomIn_routeFinder.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoomOut_routeFinder.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }


        startPointTxt.setOnClickListener {
            val intent = Intent(requireContext(), RoutFinder2::class.java)
            startActivityForResult(intent, 1)
        }

        endPointTxt.setOnClickListener {
            val intent= Intent(requireContext(), RoutFinder2::class.java)
            startActivityForResult(intent,2)
        }

        findRoutBtn.setOnClickListener {

            showDialog()




        }
        locationImg.setOnClickListener {
            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude()
                    )
                ) // Sets the center of the map to location user
                .zoom(17f) // Sets the zoom
                .bearing(90f) // Sets the orientation of the camera to east
                .tilt(40f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            mMap.setOnCameraIdleListener {
                val center = mMap.cameraPosition.target
//            val latLng="${center.latitude},"+"${center.longitude}"
                val address = getStringAddress(center.latitude, center.longitude)
                startPointTxt.text = address

            }
        }

        micImg.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )


            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text")

            try {
                startActivityForResult(intent, 3)
            } catch (e: Exception) {

                Toast
                    .makeText(
                        requireContext(), " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }

        saveBtn_routfinder.setOnClickListener {
            if (startPointTxt.text.isNotEmpty() && endPointTxt.text.isNotEmpty()) {
                database = RoomAppDb.getAppDatabase(requireContext())!!


                val Time: Date = Calendar.getInstance().getTime()
                val sourceAddress = startPointTxt.text.toString()
                val destinationAddress = endPointTxt.text.toString()

//            val  record= (SavedRouteModel(Time.toString(),sourceAddress,destinationAddress)) as ArrayList<SavedRouteModel>
                GlobalScope.launch {
                    database.recordDao()?.insertRecord(
                        RecordEntity(
                            0,
                            Time.toString(),
                            sourceAddress,
                            destinationAddress
                        )
                    )
                }

                replaceFragment(SavedRoute_Frag())
            }else
                Toast.makeText(requireContext(), "Please Enter Source and Destination Address ", Toast.LENGTH_SHORT).show()
        }

    }

    fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog)
        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_no) as Button
        yesBtn.setOnClickListener {
            if (startPointTxt.text.isNotEmpty() && endPointTxt.text.isNotEmpty()){
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=${startPointTxt.text.toString()}&daddr=${endPointTxt.text.toString()}")
                )
                startActivity(intent)
                dialog.dismiss()

            }else{
                try {
                    if (sourceLatitude != null && sourceLongitude != null && destinationLatitude != null && destinationLongitude != null) {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=${sourceLatitude},${sourceLongitude}&daddr=${destinationLatitude},${destinationLongitude}")
                        )
                        startActivity(intent)
                    }
                }catch (e:Exception){
                    Toast.makeText(requireContext(), "Please Enter Source and Destination Address", Toast.LENGTH_SHORT).show()}}

        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun getStringAddress(latitude: Double, longitude: Double): String {
        val address:String?
        val city:String?
        val geocoder: Geocoder?
        val addresses:List<Address>?

        try{

            geocoder= Geocoder(requireContext(), Locale.getDefault())

            Log.d("thisss", "addresses  "+"  latitude  "+latitude+"  longitude  "+ longitude)

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

                val supportMapFragment = (childFragmentManager.findFragmentById(R.id.mapRoutFinderOne) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("thissss", "${requestCode}: ")
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data?.getStringExtra("address")
                startPointTxt.text=address
                latLng= data?.getStringExtra("latlng")?.split(",")?.toTypedArray()!!
                sourceLatitude=latLng[0]
                sourceLongitude=latLng[1]


            }
        }
        if (requestCode==2) {
            if (resultCode == Activity.RESULT_OK) {
                val address = data?.getStringExtra("address")
                latLng= data?.getStringExtra("latlng")?.split(",")?.toTypedArray()!!
                endPointTxt.text=address
                destinationLatitude=latLng[0]
                destinationLongitude=latLng[1]

            }
        }
        if (requestCode == 3) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {

                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                endPointTxt.text=res[0]
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
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
        googleMap.isMyLocationEnabled = true

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