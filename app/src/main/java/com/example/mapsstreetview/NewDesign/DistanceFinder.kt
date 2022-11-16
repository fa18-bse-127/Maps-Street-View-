package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsstreetview.Adapters.AdapterAutoComplete
import com.example.mapsstreetview.Models.AutoCompleteModel
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.fragment_altitude_finder.*
import kotlinx.android.synthetic.main.fragment_distance_finder.*
import kotlinx.android.synthetic.main.fragment_live_satellite_view.*
import kotlinx.android.synthetic.main.fragment_live_traffic_.*


class DistanceFinder : Fragment(), OnMapReadyCallback,IOnBackPressed {
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var adapter: AdapterAutoComplete
    lateinit var center: LatLng
    private val permissionCode = 101
    var value=1
    var value2=1
    lateinit var location: Location
    lateinit   var latlng1: LatLng
    lateinit var latlng2:LatLng
    var dummmy=false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distance_finder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Distace Finder"

        mapChangebtn_DistanceFinder.setOnClickListener {

            when(value){
                1->{ mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    value=2}
                2->{ mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    value=3}
                3->{ mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    value=1}
            }
        }
        zoomIn_distanceFinder.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())

        }

        zoomOut_distanceFinder.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        trafficBtn_DistanceFinder.setImageResource(R.drawable.trafficimg)
        trafficBtn_DistanceFinder.setOnClickListener  {
            if (dummmy) {
                mMap.isTrafficEnabled = false
                trafficBtn_DistanceFinder.setImageResource(R.drawable.trafficimg)
                dummmy=false
            }else{
                mMap.isTrafficEnabled=true
                trafficBtn_DistanceFinder.setImageResource(R.drawable.traffic_btn_pressed_state)
                dummmy=true

            }
        }

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()

        deleteBtn_distanceFinder.setOnClickListener {
            DistanceTv_distanceFinder.text="0.0"
            value2=1
            mMap.clear()

        }


        searchView_DistanceFinder.isIconified=false
        searchView_DistanceFinder.isIconifiedByDefault=false
        searchView_DistanceFinder.isSubmitButtonEnabled=true
        searchView_DistanceFinder.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                var model:ArrayList<AutoCompleteModel>?= ArrayList<AutoCompleteModel>()
                try {
                    model= getLocationFromAddress(p0)
                }catch (e:Exception){
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }

                if (model!=null) {
                    recyclerview_distanceFinder.layoutManager =
                        LinearLayoutManager(requireContext())
                    adapter = AdapterAutoComplete(this@DistanceFinder, model)
                    recyclerview_distanceFinder.adapter = adapter



                }else
                    Toast.makeText(requireContext(), "No place Found", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (p0!!.isEmpty()){
                    val model1= ArrayList<AutoCompleteModel>()
                    adapter=AdapterAutoComplete(this@DistanceFinder,model1)
                    recyclerview_distanceFinder.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    public fun newCameraPosition(latlng:LatLng){
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

                val supportMapFragment = (childFragmentManager.findFragmentById(R.id.mapFragment_distanceFinder) as
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

        p0.setOnMapClickListener(OnMapClickListener { point ->

            if (value2==1){
                latlng1=point
                p0.addMarker(MarkerOptions().position(point).title("Source"))
                value2+=1
            }else if (value2==2){
                latlng2=point
                p0.addMarker(MarkerOptions().position(point).title("Destination"))
                value2+=1
            }else
                Toast.makeText(requireContext(), "No more than 2 points allowed", Toast.LENGTH_SHORT).show()


            if (value2>=3) {
                val distance = distanceBetween(latlng1!!, latlng2!!)
                DistanceTv_distanceFinder.setText(distance.toString())
                drawRoute(listOf(latlng1,latlng2))
            }


        })

        trafficBtn_DistanceFinder.setOnClickListener {
            if (dummmy) {
                mMap.isTrafficEnabled = false
                dummmy=false
            }else{
                mMap.isTrafficEnabled=true
                dummmy=true
            }
        }




    }

    private fun distanceBetween(latLng1: LatLng, latLng2: LatLng): Float {
        val loc1 = Location(LocationManager.GPS_PROVIDER)
        val loc2 = Location(LocationManager.GPS_PROVIDER)
        loc1.latitude = latLng1.latitude
        loc1.longitude = latLng1.longitude
        loc2.latitude = latLng2.latitude
        loc2.longitude = latLng2.longitude
        return loc1.distanceTo(loc2)/1000
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions()


        val points = polylineOptions.points
        points.addAll(locations)


        mMap.addPolyline(polylineOptions)
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