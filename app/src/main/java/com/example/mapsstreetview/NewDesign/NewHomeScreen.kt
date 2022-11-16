package com.example.mapsstreetview.NewDesign

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.mapsstreetview.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_new_home_screen.*


class NewHomeScreen : AppCompatActivity(){
    private lateinit var mMap: GoogleMap
    private val permissionCode = 101

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var actionBarToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_home_screen)


        setSupportActionBar(tbToolbar)



        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()


        val locationTracker=LocationTracker_Frag()
        replaceFragment(MainScreenFrag())

        actionBarToggle =
            ActionBarDrawerToggle(this, drawerLayout,tbToolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarToggle)

        actionBarToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));



        // Display the hamburger icon to launch the drawer


        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()


        val navView = findViewById<NavigationView>(R.id.navView)
//
//        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            Log.d("itemselected", "onCreate: ")
            when (menuItem.itemId) {
                R.id.home_drawer -> {
                    replaceFragment(MainScreenFrag())
                    this.drawerLayout.closeDrawer(GravityCompat.START)

                }
                R.id.premiumFeature_drawer -> {

                }
                R.id.share_drawer -> {

                }
                R.id.rating_drawer -> {

                }
                R.id.privacyPolicy_drawer -> {

                }
            }
            true
        }


    }






    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)

        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it

            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Not permitted", Toast.LENGTH_SHORT).show()

                    showDialog2()
                } else {

                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val frag : Fragment =  supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

        if (frag is MainScreenFrag){

        }else {
            menuInflater.inflate(R.menu.home_icon, menu);

            val homeButton = menu!!.findItem(R.id.HomeIcon)

            homeButton.setOnMenuItemClickListener {
                replaceFragment(MainScreenFrag())
                return@setOnMenuItemClickListener false

            }
        }
        return super.onCreateOptionsMenu(menu)

    }


    @SuppressLint("RestrictedApi")
    public fun replaceFragment(fragment: Fragment) {
        val act: Activity? = fragment.activity
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

//            fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("itemselected", "onOptionsItemSelected: ")
        if (actionBarToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        }else {

            val fragment =
                this.supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {


            }


            val frag: Fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

            if (frag is MainScreenFrag) {
                showDialog()
            }

        }




    }

    fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.exit_dialog)
        val yesBtn = dialog.findViewById<TextView>(R.id.btn_yes_exit)
        val noBtn = dialog.findViewById<TextView>(R.id.btn_no_exit)
        yesBtn.setOnClickListener {
            super.onBackPressed()

        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    fun showDialog2() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.permission_dialog)
        val yesBtn = dialog.findViewById<TextView>(R.id.btn_yes_exit)
        val noBtn = dialog.findViewById<TextView>(R.id.btn_no_exit)
        yesBtn.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
           finish()

        }
        noBtn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        dialog.show()

    }

}



