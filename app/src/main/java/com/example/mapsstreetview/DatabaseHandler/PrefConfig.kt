package com.example.mapsstreetview.DatabaseHandler

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.mapsstreetview.Models.SavedRouteModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PrefConfig {

    fun writeLatLong(context: Context, Latlong:String){
        val pref= PreferenceManager.getDefaultSharedPreferences(context)
        val editor: SharedPreferences.Editor=pref.edit()
        editor.putString("hi", Latlong)
        editor.apply()

    }

    fun writeLatLong2(context: Context, Latlong:String){
        val pref= PreferenceManager.getDefaultSharedPreferences(context)
        val editor: SharedPreferences.Editor=pref.edit()
        editor.putString("hi2", Latlong)
        editor.apply()

    }

    fun writeAddress(context: Context, Latlong:String){
        val pref= PreferenceManager.getDefaultSharedPreferences(context)
        val editor: SharedPreferences.Editor=pref.edit()
        editor.putString("address", Latlong)
        editor.apply()

    }

    fun writeListInPref(context:Context, storedPackages: java.util.ArrayList<SavedRouteModel>){
        val pref:SharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        val gson= Gson()


        val jsonString=gson.toJson(storedPackages)
        val editor:SharedPreferences.Editor=pref.edit()
        editor.putString("route list",jsonString)
        editor.apply()

    }

    fun readListFromPref(context:Context):ArrayList<SavedRouteModel> {
        val pref:SharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        val jsonString=pref.getString("route list","")
        val gson=Gson()
        val type: Type =object : TypeToken<ArrayList<SavedRouteModel>>(){}.type

        var list=gson.fromJson<ArrayList<SavedRouteModel>>(jsonString,type)
        if (list.isNullOrEmpty()){
            list= ArrayList<SavedRouteModel>()
        }

        return list


    }


    fun readLatLong(context: Context): String {
        val pref= PreferenceManager.getDefaultSharedPreferences(context)
        val phone=pref.getString("hi","")
        return phone!!
    }

    fun readLatLong2(context: Context): String {
        val pref= PreferenceManager.getDefaultSharedPreferences(context)
        val phone=pref.getString("hi2","")
        return phone!!
    }

    fun readAddress(context: Context): String {
        val pref= PreferenceManager.getDefaultSharedPreferences(context)
        val address=pref.getString("address","")
        return address!!
    }


}