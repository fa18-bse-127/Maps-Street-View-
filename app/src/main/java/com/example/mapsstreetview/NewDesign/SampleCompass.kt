//package com.example.mapsstreetview.NewDesign
//
//import android.content.Context.SENSOR_SERVICE
//import android.hardware.Sensor
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.core.content.ContextCompat.getSystemService
//import androidx.fragment.app.Fragment
//import com.example.mapsstreetview.R
//
//
//class SampleCompass : Fragment() {
//
//    private var imageView: ImageView? = null
//
//    private var sensorManager: SensorManager? = null
//    private var sensorAccelerometer: Sensor? = null
//    private var sensorMagneticField: Sensor? = null
//
//    private val floatGravity = FloatArray(3)
//    private val floatGeoMagnetic = FloatArray(3)
//
//    private val floatOrientation = FloatArray(3)
//    private val floatRotationMatrix = FloatArray(9)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sample_compass, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        imageView = requireView().findViewById(R.id.imageView);
//
//        sensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
//
//        sensorAccelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorMagneticField = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//
//        val sensorEventListenerAccelrometer =  SensorEventListener() {
//
//            onSensorChanged(SensorEvent event) {
//                floatGravity = event.values;
//
//                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
//                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
//
//                imageView.setRotation((float) (-floatOrientation[0]*180/3.14159));
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//            }
//        };
//
//        SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent event) {
//                floatGeoMagnetic = event.values;
//
//                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
//                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
//
//                imageView.setRotation((float) (-floatOrientation[0]*180/3.14159));
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//            }
//        };
//        sensorManager.registerListener(sensorEventListenerAccelrometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(sensorEventListenerMagneticField, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    public void ResetButton(View view){
//        imageView.setRotation(180);
//    }
//}