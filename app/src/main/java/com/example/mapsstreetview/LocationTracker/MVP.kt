package com.example.mapsstreetview.LocationTracker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.example.mapsstreetview.R
import com.google.android.gms.maps.model.LatLng

class MapPresenter(private val activity: AppCompatActivity) {

    val ui = MutableLiveData(Ui.EMPTY)

    private val locationProvider = LocationProvider(activity)

    private val stepCounter = StepCounter(activity)

  //  private val permissionsManager = PermissionManager(activity, locationProvider, stepCounter)

    @SuppressLint("StringFormatInvalid")
    fun onViewCreated() {

        locationProvider.liveLocations.observe(activity) { locations ->
            val current = ui.value
            ui.value = current?.copy(userPath = locations)
        }

        locationProvider.liveLocation.observe(activity) { currentLocation ->
            val current = ui.value
            ui.value = current?.copy(currentLocation = currentLocation)
        }

        locationProvider.liveDistance.observe(activity) { distance ->
            val current = ui.value
            val formattedDistance = activity.getString(R.string.distance_value, (distance*.001).toFloat())
            ui.value = current?.copy(formattedDistance = formattedDistance)
//            ui.value=current?.copy(formattedPace = pace)
        }

//        locationProvider.liveDistance.observe(activity) { distance ->
//            val current = ui.value
//            var time=1
//            val pace=activity.getString(R.string.average_pace_label,((distance/time)).toFloat())
//                ui.value = current?.copy(formattedPace = pace)
//        }
    }

    fun onMapLoaded() {
//        permissionsManager.requestUserLocation()
    }

    fun startTracking() {
//        permissionsManager.requestActivityRecognition()
        locationProvider.trackUser()

        val currentUi = ui.value
        ui.value = currentUi?.copy(
            formattedPace = Ui.EMPTY.formattedPace,
            formattedDistance = Ui.EMPTY.formattedDistance
        )
    }

    fun stopTracking() {
        locationProvider.stopTracking()
        stepCounter.unloadStepCounter()
    }
}

data class Ui(
    val formattedPace: String,
    val formattedDistance: String,
    val currentLocation: LatLng?,
    val userPath: List<LatLng>
) {

    companion object {

        val EMPTY = Ui(
            formattedPace = "",
            formattedDistance = "",
            currentLocation = null,
            userPath = emptyList()
        )
    }
}