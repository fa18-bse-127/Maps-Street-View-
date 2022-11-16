package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsstreetview.DatabaseHandler.PrefConfig
import com.example.mapsstreetview.Models.AutoCompleteModel
import com.example.mapsstreetview.NewDesign.NewAdapters.SearchAdapter_RouteFinder2
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
import kotlinx.android.synthetic.main.activity_rout_finder2.*
import kotlinx.android.synthetic.main.fragment_live_satellite_view.*
import kotlinx.android.synthetic.main.fragment_live_traffic_.*
import java.util.*


class RoutFinder2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private val permissionCode = 101
    var value=true
    lateinit var adapter:SearchAdapter_RouteFinder2
    var dummy=true
    val pref= PrefConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val resultCode=intent.getIntExtra("ResultCode",1)


        setContentView(R.layout.activity_rout_finder2)

        setSupportActionBar(tbToolbarRouteFinder)
        supportActionBar?.elevation= 0F
        supportActionBar?.title="Route Finder"

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this@RoutFinder2)
        fetchLocation()

        converButton_Route2.setOnClickListener {

            if (value){
                mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                value=false
            }else {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                value = true
            }
        }


        zoomin_routFinder2.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoomOut_routeFinder2.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
        var dummmy = false
        trafficBtn_routeFinder2.setOnClickListener {

            if (dummmy) {
                mMap.isTrafficEnabled = false
                trafficBtn_routeFinder2.setImageResource(R.drawable.trafficimg)
                dummmy=false
            }else{
                mMap.isTrafficEnabled=true
                trafficBtn_routeFinder2.setImageResource(R.drawable.traffic_btn_pressed_state)
                dummmy=true
            }
        }



        pickLocationBtn.setOnClickListener {
            val address=pref.readAddress(this)
            val latlng=pref.readLatLong2(this)
            val returnIntent = Intent()
            returnIntent.putExtra("address", address)
            returnIntent.putExtra("latlng",latlng)
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        searchView_RouteFinder2.isIconified=false
        searchView_RouteFinder2.isIconifiedByDefault=false
        searchView_RouteFinder2.isSubmitButtonEnabled=true
        searchView_RouteFinder2.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                var model:ArrayList<AutoCompleteModel>?= ArrayList<AutoCompleteModel>()
                try {
                    model= getLocationFromAddress(p0)
                }catch (e:Exception){
                    Toast.makeText(this@RoutFinder2, e.toString(), Toast.LENGTH_SHORT).show()
                }

                if (model!=null) {
                    recyclerview_RouteFinder2.layoutManager =
                        LinearLayoutManager(this@RoutFinder2)
                    adapter = SearchAdapter_RouteFinder2(this@RoutFinder2, model)
                    recyclerview_RouteFinder2.adapter = adapter



                }else
                    Toast.makeText(this@RoutFinder2, "No place Found", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (p0!!.isEmpty()){
                    val model1= ArrayList<AutoCompleteModel>()
                    adapter= SearchAdapter_RouteFinder2(this@RoutFinder2,model1)
                    recyclerview_RouteFinder2.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    private fun getLocationFromAddress(p0: String?): ArrayList<AutoCompleteModel>? {

        val coder = Geocoder(this)
        var p1: GeoPoint? =null
        var title:String
        val modelArray:ArrayList<AutoCompleteModel> = ArrayList<AutoCompleteModel>()
        var location:ArrayList<Address> = ArrayList()


        val address: MutableList<Address>? = coder.getFromLocationName(p0, 10000)
        if (address == null) {
            Toast.makeText(this, "Please Enter Any Address", Toast.LENGTH_SHORT).show()
            return null
        }

        if (address.isNotEmpty()) {
            Log.d("thisss___", address.toString())
            for (i in address){
                location.add(i)
            }

            modelArray.clear()

            Log.d("thisss___", location[0].getAddressLine(0))
            Log.d("thisss___", location.size.toString())
//            val location: Address = address[0]
            for (i in 0 until location.size) {
                p1 = GeoPoint((location[i].latitude), (location[i].longitude))
                title= location[i].getAddressLine(0)
                modelArray.add( AutoCompleteModel(p1,title))
                Log.d("thisss___", modelArray.toString())
            }
            Log.d("check__", modelArray.toString())
            return modelArray
        }else{
            Log.d("check__", "getLocationFromAddress: else")

            return null
        }

    }


    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
//                Toast.makeText(this, currentLocation.latitude.toString() + "   " +
//                        currentLocation.longitude, Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.mapRoutFinderTwo) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this@RoutFinder2)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
                )
            )
            .zoom(17f)
            .bearing(90f)
            .tilt(40f)
            .build()

        p0.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        p0.isMyLocationEnabled=true

        p0.setOnCameraIdleListener {
            val center=p0.cameraPosition.target
            val latLng="${center.latitude},"+"${center.longitude}"
            pref.writeLatLong2(this,latLng)
            val address=getStringAddress(center.latitude,center.longitude)
            pref.writeAddress(this,address)
            addressHeadlineTxt.text=address
        }


    }

    private fun getStringAddress(latitude: Double, longitude: Double): String {
        val address:String?
        val city:String?
        val geocoder: Geocoder?
        val addresses:List<Address>?

        try{

            geocoder= Geocoder(this, Locale.getDefault())

            Log.d("thisss", "addresses  "+"  latitude  "+latitude+"  longitude  "+ longitude)

            addresses=geocoder.getFromLocation(latitude,longitude,1)

            address=addresses.get(0).getAddressLine(0)
            city=addresses.get(0).locality

            return address+" "+city

        }catch(e: Exception){
        }

        return "No Address Found"

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
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


}
