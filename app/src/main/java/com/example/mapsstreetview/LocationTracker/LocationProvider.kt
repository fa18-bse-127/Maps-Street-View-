package com.example.mapsstreetview.LocationTracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.roundToInt


class LocationProvider(private val activity: AppCompatActivity) {

    private val client by lazy { LocationServices.getFusedLocationProviderClient(activity) }

    private val locations = mutableListOf<LatLng>()
    private var distance = 0

    val liveLocations = MutableLiveData<List<LatLng>>()
    val liveDistance = MutableLiveData<Int>()
    val liveLocation = MutableLiveData<LatLng>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val currentLocation = result.lastLocation
            val latLng = currentLocation?.let { LatLng(it.latitude, currentLocation.longitude) }

            val lastLocation = locations.lastOrNull()

            if (lastLocation != null) {
                distance += SphericalUtil.computeDistanceBetween(lastLocation, latLng).roundToInt()
                liveDistance.value = distance
            }

            locations.add(latLng!!)
            liveLocations.value = locations
        }
    }

    fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(activity, "Location service is off", Toast.LENGTH_SHORT).show()
            return
        }
        try{
            client.lastLocation.addOnSuccessListener { location ->
                if (location!=null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    locations.add(latLng)
                    liveLocation.value = latLng
                }else
                    Toast.makeText(activity, "please turn on location services", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            Toast.makeText(activity, "Please turn on location services", Toast.LENGTH_SHORT).show()
        }
    }

    fun trackUser() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun stopTracking() {
        client.removeLocationUpdates(locationCallback)
        locations.clear()
        distance = 0
    }
}