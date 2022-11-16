package com.example.mapsstreetview.NewDesign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsstreetview.Adapters.FamousPlacesAdapter
import com.example.mapsstreetview.Models.ItemsViewModel
import com.example.mapsstreetview.R
import com.google.android.gms.maps.model.LatLng


class SevenWondersFrag : Fragment() ,IOnBackPressed{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seven_wonders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Seven Wonders"
        val recyclerview = requireView().findViewById<RecyclerView>(R.id.recyclerview_sevenWonders_Frag)

        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        var data = ArrayList<ItemsViewModel>()

        data= (data+ ItemsViewModel("Taj Mahal, India",R.drawable.taj_mahal__india, LatLng(27.1751,78.0421))) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Wall of China",R.drawable.great_wall_of_china, LatLng(40.4319077,116.5703749))) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Petra, Jordan",R.drawable.petra__jordan, LatLng(30.3285,35.4444))) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Christ the Redeemer",R.drawable.christ_the_reedmer, LatLng(-22.951916,-43.21010727))) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Machu Picchu, Peru",R.drawable.machu_picchu__peru, LatLng(-13.1631412,-72.5449629))) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Colosseum, Italy",R.drawable.colosseum_rome_italy, LatLng(41.89016529,12.49217371))) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Chichen Itza, Mexico",R.drawable.chichen_itza_maxico, LatLng(20.6842849,-88.5677826))) as ArrayList<ItemsViewModel>



        // This will pass the ArrayList to our Adapter
        val adapter = FamousPlacesAdapter(requireContext(),data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

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