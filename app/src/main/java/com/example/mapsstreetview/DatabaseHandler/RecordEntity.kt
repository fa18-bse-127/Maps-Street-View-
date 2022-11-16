package com.example.mapsstreetview.DatabaseHandler

import androidx.room.*

@Entity(tableName = "savedRouteRecords")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
     val dateTime:String,
     val src:String,
    val des:String



)

