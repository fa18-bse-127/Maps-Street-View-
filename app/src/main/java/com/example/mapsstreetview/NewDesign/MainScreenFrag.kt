package com.example.mapsstreetview.NewDesign

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mapsstreetview.levelMeterPackage.LevelMeter_Frag
import com.example.mapsstreetview.R

import kotlinx.android.synthetic.main.fragment_main_screen.*

class MainScreenFrag : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Maps Street View"



        liveSatelliteViewContainer_Home.setOnClickListener {
            if (isLocationEnabled(requireContext())) {
                replaceFragment(LiveSatelliteView())
            }
        }
        NearbyPlaces_homeScreen.setOnClickListener {
            replaceFragment(NearbyPlaces_Frag())
        }

        distanceFinder_home.setOnClickListener {
            if (isLocationEnabled(requireContext()))
            replaceFragment(DistanceFinder())
        }
        locationTracker_home.setOnClickListener {
            if (isLocationEnabled(requireContext()))
            replaceFragment(LocationTracker_Frag())
        }
        FamousPlaces_homeFrag.setOnClickListener {
            replaceFragment(FamousLocations_Frag())
        }
        SevenWonders_homeFrag.setOnClickListener {
            replaceFragment(SevenWondersFrag())
        }
        Compass_homeFrag.setOnClickListener {
            if (isLocationEnabled(requireContext()))
            replaceFragment(Compass_Frag())
        }
        SavedRoutes_homeFrag.setOnClickListener {
            replaceFragment(SavedRoute_Frag())
        }
        myTracks_homeFrag.setOnClickListener {
            replaceFragment(MyTracks_Frag())
        }
        levelMeter_homeFrag.setOnClickListener {
            replaceFragment(LevelMeter_Frag())
        }
        RouteFinder_homeFrag.setOnClickListener {
            if (isLocationEnabled(requireContext()))
            replaceFragment(RouteFinder1_Frag())
        }
        liveEarthCam_homeFrag.setOnClickListener {
            replaceFragment(LiveCams_Frag())
        }
        LiveTraffic_homeFrag.setOnClickListener {
            if (isLocationEnabled(requireContext()))
            replaceFragment(LiveTraffic_Frag())
        }
        myLocation_homeFrag.setOnClickListener {
            if (isLocationEnabled(requireContext()))
            replaceFragment(MyLocation_Frag())
        }
        altitude_homeFrag.setOnClickListener {
            if (isLocationEnabled(requireContext()))
            replaceFragment(AltitudeFinder())
        }


    }
    public fun replaceFragment(fragment: Fragment) {


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()

    }

    fun isLocationEnabled(context: Context):Boolean{
        val lm = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            network_enabled= cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        } catch (ex: Exception) {
        }

        if (!gps_enabled) {
            // notify user
            AlertDialog.Builder(context)
                .setMessage("Location not enabled")
                .setPositiveButton("Settings",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        context.startActivity(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    })
                .setNegativeButton("Cancel", null)
                .show()
            return false
        }else
            return true


    }



}