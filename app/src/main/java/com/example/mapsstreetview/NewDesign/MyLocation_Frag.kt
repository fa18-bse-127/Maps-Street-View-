package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.fragment_my_location.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MyLocation_Frag: Fragment(),IOnBackPressed {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private val permissionCode = 101

    lateinit var progressDialog:ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="My Location"

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()
        placeName_myLocation.text="Loading..."
        address_myLocation.text="Loading..."
        latitude_myLocation.setText("Loading...")
        longitude_myLocation.setText("Loading")



        GlobalScope.launch {
            Thread.sleep(1000)




        }

        mapBtn_myLocation.setOnClickListener {
            showDialog()

        }

        copyBtn_Mylocation.setOnClickListener {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(android.R.attr.label.toString(), placeName_myLocation.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show()
        }

        shareBtn_myLocation.setOnClickListener {
            val uri = "http://maps.google.com/maps?geo:${currentLocation?.latitude},${currentLocation?.longitude}"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
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
            val gmmIntentUri = Uri.parse("geo:${currentLocation.latitude},${currentLocation.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(requireActivity().packageManager)?.let {
                startActivity(mapIntent)
            }

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

                val address =
                    getStringAddress(currentLocation.latitude, currentLocation.longitude)

//                    val addressNameArray=address
            try {
                val addressNameArray=address.split(",")
                val addressName=addressNameArray[1]
                placeName_myLocation.text=addressName
                address_myLocation.text=address
                latitude_myLocation.setText(currentLocation.latitude.toString())
                longitude_myLocation.setText(currentLocation.longitude.toString())

            }catch (e:Exception){

            }


            }
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