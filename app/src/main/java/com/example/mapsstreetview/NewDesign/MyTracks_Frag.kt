package com.example.mapsstreetview.NewDesign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsstreetview.Adapters.AdapterMyTrack
import com.example.mapsstreetview.DatabaseHandler.MytrackEntity
import com.example.mapsstreetview.DatabaseHandler.RoomAppDb
import com.example.mapsstreetview.R
import kotlinx.android.synthetic.main.fragment_my_tracks_.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyTracks_Frag : Fragment(),IOnBackPressed {
    lateinit var data:List<MytrackEntity>
    lateinit var database: RoomAppDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_tracks_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="My Tracks"

        database=RoomAppDb.getAppDatabase(requireContext())!!

        CoroutineScope(Dispatchers.IO).launch {
            data= database.mytrackDao()!!.getTrack()
            CoroutineScope(Dispatchers.Main).launch {

                recyclerview_mytracksfrag.layoutManager = LinearLayoutManager(requireContext())


                val adapter= AdapterMyTrack(this@MyTracks_Frag,data)
                recyclerview_mytracksfrag.adapter=adapter
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