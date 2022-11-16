package com.example.mapsstreetview.NewDesign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsstreetview.Adapters.Adapter
import com.example.mapsstreetview.DatabaseHandler.RecordEntity
import com.example.mapsstreetview.DatabaseHandler.RoomAppDb
import com.example.mapsstreetview.Models.SavedRouteModel
import com.example.mapsstreetview.R
import kotlinx.android.synthetic.main.fragment_saved_route_.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SavedRoute_Frag : Fragment(), CoroutineScope by MainScope(),IOnBackPressed {
    lateinit var data:ArrayList<RecordEntity>
    lateinit var database: RoomAppDb


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_route_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Saved Routes"
        database= RoomAppDb.getAppDatabase(requireContext())!!


        CoroutineScope(Dispatchers.IO).launch {
            data=database.recordDao()!!.getRecodes() as ArrayList<RecordEntity>
            CoroutineScope(Dispatchers.Main).launch {


                recyclerview_savedroutesfrag.layoutManager= LinearLayoutManager(requireContext())

                val adapter= Adapter(requireContext(),data)
                recyclerview_savedroutesfrag.adapter=adapter
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