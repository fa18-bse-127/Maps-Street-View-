package com.example.mapsstreetview.levelMeterPackage

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.mapsstreetview.levelMeterPackage.DeviceRotationManager.OnRotationChangedListener
import com.example.mapsstreetview.NewDesign.IOnBackPressed
import com.example.mapsstreetview.NewDesign.MainScreenFrag
import com.example.mapsstreetview.NewDesign.NewHomeScreen
import com.example.mapsstreetview.R
import javax.annotation.MatchesPattern

class LevelMeter_Frag : Fragment(), SensorEventListener, IOnBackPressed {

    lateinit var levelView: LevelViewMeter
    private var deviceRotationManager: DeviceRotationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         levelView = LevelViewMeter(
             requireContext()
         )
        levelView.setBackgroundColor(R.color.black)
        levelView.layoutParams=  ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)


        return levelView



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val decorView: View = requireActivity().getWindow().getDecorView()
        decorView.systemUiVisibility = (decorView.systemUiVisibility
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        deviceRotationManager = DeviceRotationManager(requireContext(),
            OnRotationChangedListener { x: Float, y: Float ->
                levelView.setData(
                    x,
                    y
                )
            })







    }

    override fun onStart() {
        super.onStart()
        deviceRotationManager!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        deviceRotationManager!!.onStop()
    }

    override fun onSensorChanged(event: SensorEvent?) {
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onBackPressed(): Boolean {
        startActivity(Intent(requireContext(),NewHomeScreen::class.java))
        requireActivity().finish()
        return true
    }

    public fun replaceFragment(fragment: Fragment) {


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()

    }

}