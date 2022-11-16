package com.example.mapsstreetview.NewDesign

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsstreetview.Adapters.GridRVAdapter
import com.example.mapsstreetview.Models.LiveCamListModel
import com.example.mapsstreetview.R


class LiveCams_Frag : Fragment() ,IOnBackPressed{
    lateinit var courseGRV: GridView
    lateinit var LivecamList: List<LiveCamListModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_cams_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Live Cams"
        courseGRV = requireView().findViewById(R.id.idGRV)
        LivecamList = ArrayList<LiveCamListModel>()



        LivecamList = LivecamList + LiveCamListModel("California Academy of Science", R.drawable.california_academy_of_science,"sKlz04IFkbM")
        LivecamList = LivecamList + LiveCamListModel("Dok Lemstor", R.drawable.dok_lemstor_toer_lemmer,"kOHUT5FfyZ0")
        LivecamList = LivecamList + LiveCamListModel("Hotel Roanokerail", R.drawable.hotel_roanokerailcam,"cV3kP5FSUDk")
        LivecamList = LivecamList + LiveCamListModel("Jake Johnson Properties", R.drawable.jake_johnson_properties,"qP1y7Tdab7Y")
        LivecamList = LivecamList + LiveCamListModel("Miramar Beach", R.drawable.leeward_key_webcam_miramar_beach__fl,"CVgBM39TOh8")
        LivecamList = LivecamList + LiveCamListModel("Parkville Missouri", R.drawable.parkville__missouri__usa,"7oD2KnSLT44")
        LivecamList = LivecamList + LiveCamListModel("Renesse", R.drawable.renesse__aan_zee,"EH6GNSEvmQE")
        LivecamList = LivecamList + LiveCamListModel("Santa Monica Beach", R.drawable.santa_monica_beach,"vvOjJoSEFM0")
        LivecamList = LivecamList + LiveCamListModel("Watermeterplein", R.drawable.watermeterplein__centrum_renesse,"5xX0Z_-mJHo")
        LivecamList = LivecamList + LiveCamListModel("Assisi", R.drawable.assisi_live_stream,"CkNeltsc5ps")
        LivecamList =
            LivecamList + LiveCamListModel("Brook Falls", R.drawable.img_1, "mphg2feuAPo")
        LivecamList = LivecamList + LiveCamListModel("Volcano", R.drawable.img_2, "NqMRbKmEudk")
        LivecamList = LivecamList + LiveCamListModel("Newyork", R.drawable.img_3, "u8Vvws69Fso")
        LivecamList = LivecamList + LiveCamListModel("Birds", R.drawable.img_4, "N609loYkFJo")

        // on below line we are initializing our course adapter
        // and passing course list and context.
        val courseAdapter = GridRVAdapter(camList = LivecamList, requireContext())

        // on below line we are setting adapter to our grid view.
        courseGRV.adapter = courseAdapter

        // on below line we are adding on item
        // click listener for our grid view.
        courseGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying
            // a toast message with course name.
            if (isLocationOrNetworkEnabled(requireContext())) {
                val intent = Intent(requireContext(), LiveCams::class.java)
                intent.putExtra("url", LivecamList[position].url)
                startActivity(intent)
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
    fun isLocationOrNetworkEnabled(context: Context):Boolean{
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
                .setMessage("Network or Location not enabled")
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