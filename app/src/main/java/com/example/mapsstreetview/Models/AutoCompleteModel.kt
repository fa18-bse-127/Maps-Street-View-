package com.example.mapsstreetview.Models

import com.google.firebase.firestore.GeoPoint

data class AutoCompleteModel(val latlng:GeoPoint, val address:String)