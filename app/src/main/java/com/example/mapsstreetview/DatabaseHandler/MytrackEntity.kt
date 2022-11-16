package com.example.mapsstreetview.DatabaseHandler

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "myTracks")
data class MytrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val duration: String,
    val distance:String,
    val speed:String,
    val date:String,
    val time:String,
    val trackList: List<com.google.android.gms.maps.model.LatLng>
)



