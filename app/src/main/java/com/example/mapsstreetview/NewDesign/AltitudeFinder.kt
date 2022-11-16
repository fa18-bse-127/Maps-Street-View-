package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsstreetview.ElevationApi.API_KEY
import com.example.mapsstreetview.ElevationApi.ElevationService
import com.example.mapsstreetview.ElevationApi.ElevationsResponse
import com.example.mapsstreetview.Models.AutoCompleteModel
import com.example.mapsstreetview.NewDesign.NewAdapters.SearchAdapter_Altitude
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

import kotlinx.android.synthetic.main.fragment_altitude_finder.*
import kotlinx.android.synthetic.main.fragment_live_satellite_view.*
import kotlinx.android.synthetic.main.fragment_live_traffic_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AltitudeFinder : Fragment(), OnMapReadyCallback,IOnBackPressed {
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var center: LatLng
    private val permissionCode = 101
    lateinit var adapter: SearchAdapter_Altitude
    var value=1
    lateinit var location: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_altitude_finder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Altitude"

        AltitudeTv.setText("Loading....")
        meterLabel.visibility=View.INVISIBLE
        mapChangebtn_Altitude.setOnClickListener {

            when(value){
                1->{ mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    value=2}
                2->{ mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    value=3}
                3->{ mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    value=1}
            }
        }

        zoomIn_Altitude.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoomOut_Altitude.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
        var dummmy = false
        trafficBtn_Altitude.setOnClickListener {

            if (dummmy) {
                mMap.isTrafficEnabled = false
                trafficBtn_Altitude.setImageResource(R.drawable.trafficimg)
                dummmy=false
            }else{
                mMap.isTrafficEnabled=true
                trafficBtn_Altitude.setImageResource(R.drawable.traffic_btn_pressed_state)
                dummmy=true
            }
        }

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()

        searchView_Altitude.isIconified=false
        //searchView_LiveSatelliteView.isIconifiedByDefault=false
        searchView_Altitude.isSubmitButtonEnabled=true
        searchView_Altitude.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                var model:ArrayList<AutoCompleteModel>?= ArrayList<AutoCompleteModel>()
                try {
                    model= getLocationFromAddress(p0)
                }catch (e:Exception){
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }

                if (model!=null) {
                    recyclerview_altitude.layoutManager =
                        LinearLayoutManager(requireContext())
                    adapter = SearchAdapter_Altitude(this@AltitudeFinder, model)
                    recyclerview_altitude.adapter = adapter



                }else
                    Toast.makeText(requireContext(), "No place Found", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (p0!!.isEmpty()){
                    val model1= ArrayList<AutoCompleteModel>()
                    adapter= SearchAdapter_Altitude(this@AltitudeFinder,model1)
                    recyclerview_altitude.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    private fun getLocationFromAddress(p0: String?): ArrayList<AutoCompleteModel>? {

        val coder = Geocoder(requireContext())
        var p1: GeoPoint? =null
        var title:String
        val modelArray:ArrayList<AutoCompleteModel> = ArrayList<AutoCompleteModel>()
        var location:ArrayList<Address> = ArrayList()


        val address: MutableList<Address>? = coder.getFromLocationName(p0, 10000)
        if (address == null) {
            Toast.makeText(requireContext(), "Please Enter Any Address", Toast.LENGTH_SHORT).show()
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
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it

                val supportMapFragment = (childFragmentManager.findFragmentById(R.id.mapFragment_Altitude) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

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

        p0.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        val latLng = LatLng(currentLocation.latitude,  currentLocation.longitude)

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
        p0.isMyLocationEnabled=true

        p0.setOnCameraIdleListener {
            center=p0.cameraPosition.target
            val idlelatLng="${center.latitude},"+"${center.longitude}"
            val address=getStringAddress(center.latitude,center.longitude)

         getAltitude(center)




        }
    }

    private fun getAltitude(latlng: LatLng) {

        Log.d("this_", latlng.toString())
        val array=latlng.toString().split(":")
        val latlong=array[1].trim()
        val elevationResponse = ElevationService.elevationInstance.getElevation(API_KEY, latlong)
        var altitude:Float?=5f
        elevationResponse.enqueue(object : Callback<ElevationsResponse>{
            override fun onResponse(
                call: Call<ElevationsResponse>,
                response: Response<ElevationsResponse>
            ) {

                try {
                    val elevation=response.body()
                    val list= elevation?.elevations


                    altitude= list?.get(list.size-1)?.elevation

                    AltitudeTv.text = altitude.toString()
                    meterLabel.visibility=View.VISIBLE
                }catch (e : Exception){
                    Log.d("thisss", "onResponse: $e")
                }






            }

            override fun onFailure(call: Call<ElevationsResponse>, t: Throwable) {
                if (elevationResponse!=null){

                }
            }

        })

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