package com.example.mapsstreetview.ElevationApi


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL="https://elevation-api.io/"
const val API_KEY="Ed4G6A-Ny9-Dxw6346a5z9134--iQ3"


interface ElevationInterface{
    @GET("api/elevation")
    fun getElevation(@Query("key")apikey:String, @Query("points")points: String) : Call<ElevationsResponse>
//    fun getElevation(@Query("points")latLng:LatLng) : Call<ElevationsResponse>
}

object ElevationService {
    lateinit var elevationInstance:ElevationInterface


    init {
        val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        elevationInstance=retrofit.create(ElevationInterface::class.java)

    }
}

//https://elevation-api.io/api/elevation?points=(39.90974,-106.17188),(62.52417,10.02487)&key=YOUR-API-KEY-HERE
//https://elevation-api.io/api/elevation?points=(24.8607)
//long = 33.50510349463763,73.09664636850357


//https://elevation-api.io/api/elevation?
//https://elevation-api.io/api/elevation?key=Ed4G6A-Ny9-Dxw6346a5z9134--iQ3&(33.50510349463763,73.0966463685035)