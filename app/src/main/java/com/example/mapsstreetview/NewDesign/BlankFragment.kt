package com.example.mapsstreetview.NewDesign

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.example.mapsstreetview.R
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_blank.*

class BlankFragment : Fragment(),SensorEventListener,IOnBackPressed {
    private var imageView: ImageView? = null
    private var sensorManager: SensorManager? = null
    private var sensorAccelerometer: Sensor? = null
    private var sensorMagneticField: Sensor? = null
    private var floatGravity = FloatArray(3)
    private var floatGeoMagnetic = FloatArray(3)
    private val floatOrientation = FloatArray(3)
    private val floatRotationMatrix = FloatArray(9)
    private var currentDegree = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = requireView().findViewById(R.id.imageViewcompass)
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        sensorAccelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorMagneticField = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sensorEventListenerAccelrometer: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {

                floatGravity = event.values
                SensorManager.getRotationMatrix(
                    floatRotationMatrix,
                    null,
                    floatGravity,
                    floatGeoMagnetic
                )
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation)
                val degree=floatOrientation[0]*180/3.14159.toFloat()
                val ra = RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )

                ra.duration = 210

                // set the animation after the end of the reservation status
                ra.fillAfter = true

                // Start the animation
                imageView?.startAnimation(ra)
                currentDegree = -degree
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        val sensorEventListenerMagneticField: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                floatGeoMagnetic = event.values
                SensorManager.getRotationMatrix(
                    floatRotationMatrix,
                    null,
                    floatGravity,
                    floatGeoMagnetic
                )
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation)
                val degree=floatOrientation[0]*180/3.14159.toFloat()
                val ra = RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )

                ra.duration = 210

                // set the animation after the end of the reservation status
                ra.fillAfter = true

                // Start the animation
                imageView?.startAnimation(ra)
                currentDegree = -degree
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager!!.registerListener(
            sensorEventListenerAccelrometer,
            sensorAccelerometer,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        sensorManager!!.registerListener(
            sensorEventListenerMagneticField,
            sensorMagneticField,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        button.setOnClickListener {
            ResetButton(it)
        }
    }

    fun ResetButton(view: View?) {
        imageView?.rotation = 360f
    }

    override fun onSensorChanged(event: SensorEvent?) {

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onBackPressed(): Boolean {
        replaceFragment(MainScreenFrag())
        return false
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}