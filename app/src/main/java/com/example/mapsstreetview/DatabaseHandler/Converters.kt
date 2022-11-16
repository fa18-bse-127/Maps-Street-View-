package com.example.mapsstreetview.DatabaseHandler

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.type.LatLng

class Converters {

        @TypeConverter
        fun ToList(value: String): List<com.google.android.gms.maps.model.LatLng> {
                return Gson().fromJson(value, Array<com.google.android.gms.maps.model.LatLng>::class.java).toList()
        }

        @TypeConverter
        fun ToJson(value: List<com.google.android.gms.maps.model.LatLng>?): String? {
                return Gson().toJson(value)
        }



}
