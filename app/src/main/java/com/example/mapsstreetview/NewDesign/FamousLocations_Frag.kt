package com.example.mapsstreetview.NewDesign

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsstreetview.Adapters.FamousPlacesAdapter
import com.example.mapsstreetview.Models.ItemsViewModel
import com.example.mapsstreetview.R
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList


class FamousLocations_Frag : Fragment(), IOnBackPressed {
    lateinit var adapter: FamousPlacesAdapter
    lateinit var data:ArrayList<ItemsViewModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_famous_locations_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title="Famous Locations"


        val recyclerview = requireView().findViewById<RecyclerView>(R.id.recyclerview_famousLocation_Fragment)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel
        data = ArrayList<ItemsViewModel>()

        data= (data+ ItemsViewModel("Taj Mahal, India",
            R.drawable.taj_mahal__india, LatLng(27.1751,78.0421)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Al Hamra, Spain",
            R.drawable.al_hamra__spain, LatLng(37.1760783,-3.58810134)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Aqsa Mosque,Jerusalem,Palestine",
            R.drawable.aqsa_mosque__jerusalem__palestine, LatLng(31.7760692,35.2357802)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Atomium, Brussels Belgium",
            R.drawable.atomium__brussels_belgium, LatLng(50.8948787,4.3415547)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Belvedere palaces Vienna, Austria",
            R.drawable.belvedere_places_vienna__austria, LatLng(48.1914751,16.3809322)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Berliner Dom, Germany",
            R.drawable.berliner_dom__germany, LatLng(52.5190608,13.401078)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Besakih Temple, Indonesia",
            R.drawable.besakih_temple_indonesia, LatLng(-8.3712733,115.453096)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Big Ben, London",
            R.drawable.big_ben_london, LatLng(51.5007292,-0.1246254)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Blue Domed Church, Greece",
            R.drawable.blue_domed_church__greece, LatLng(36.461245,25.375832)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Brandenburg Gate Berlin, Germany",
            R.drawable.brandenburg_gate_berlin__germany, LatLng(52.5162746,13.3777041)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Brihadeesware Temple, India",
            R.drawable.brihadeesware_temple__india, LatLng(10.78297275,79.1318679)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Bryce Canyon National Park",
            R.drawable.bryce_canyon_national_park, LatLng(37.59519158,-112.17111003)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Budda At Kamakura Japan",
            R.drawable.budda_at_kamakura_japan, LatLng(35.3166995,139.5361536)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Burj Khalifa Dubai",
            R.drawable.burj_khalifa_dubai, LatLng(25.197197,55.2743764)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Capital Hill Washington D.C., US",
            R.drawable.capitol_hill_washington_d_c___us__png, LatLng(38.8899389,-77.0090505)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Ch Saint Basil Russia",
            R.drawable.ch_saint_basil_russia, LatLng(55.7525229,37.6230868)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Chichen Itza Mexico",
            R.drawable.chichen_itza_maxico, LatLng(20.6842849,-88.5677826)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Christ The Reedmer",
            R.drawable.christ_the_reedmer, LatLng(-22.951916,-43.2104872)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("CN Tower Toronto, Canada",
            R.drawable.cn_tower_toronto__canada, LatLng(43.6425662,-79.3870568)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Colosseum Rome Italy",
            R.drawable.colosseum_rome_italy, LatLng(41.89016529,12.49217371)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Derawar Fort, Pakistan",
            R.drawable.derawar_fort__pakistan, LatLng(28.7680083,71.3343583)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Duomo Di Milano",
            R.drawable.duomo_di_milano, LatLng(45.4640976,9.1919265)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Eiffel Tower Paris",
            R.drawable.eiffel_tower_paris, LatLng(48.8583701,2.2944813)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Ferry Building, San Freacisco",
            R.drawable.ferry_building__san_freacisco, LatLng(37.7954425,-122.3936136)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Fort Bourtange, Netherlands",
            R.drawable.fort_bourtange__netherlands, LatLng(53.0067437,7.1920358)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Golden Temple, India",
            R.drawable.golden_temple__india, LatLng(31.6206738,74.8757482)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Grand Canyon National Park Arizona",
            R.drawable.grand_canyon_national_park_arizona, LatLng(36.0617218,-112.10765563)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Grand Palace Bangkok",
            R.drawable.grand_palace_bangkok, LatLng(13.7498558,100.4915765)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Great Pyramid Giza",
            R.drawable.great_pyramid_giza, LatLng(29.9792345,31.1342019)
        )) as ArrayList<ItemsViewModel>
        data= (data+ ItemsViewModel("Great Wall Of China",
            R.drawable.great_wall_of_china, LatLng(40.4319077,116.5703749)
        )) as ArrayList<ItemsViewModel>




        adapter = FamousPlacesAdapter(requireContext(),data)

        recyclerview.adapter = adapter
        setHasOptionsMenu(true);




    }



    private fun filter(p0: String?) {

        val filteredlist: java.util.ArrayList<ItemsViewModel> =
            java.util.ArrayList<ItemsViewModel>()
        for (item in data) {
            if (item.name.lowercase(Locale.getDefault())
                    .contains(p0!!.lowercase(Locale.getDefault()))) {
                filteredlist.add(item)
            }

        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireContext(),"No data Found", Toast.LENGTH_SHORT).show()
        } else {

            adapter.filterList(filteredlist)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate( R.menu.search_item, menu);
        val menuItem=menu.findItem(R.id.searchBar_famousPlaces)
        val searchBar=menuItem.actionView as SearchView

        searchBar.isIconified=false

        searchBar.isIconifiedByDefault=false
        searchBar.queryHint="Search places"
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filter(p0)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onBackPressed(): Boolean {
        replaceFragment(MainScreenFrag())
        return true
    }

     fun replaceFragment(fragment: Fragment) {


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()

    }


}