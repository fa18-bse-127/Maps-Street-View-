//package com.example.mapsstreetview.ElevationApi
//
//import android.util.Log
//import com.example.mapsstreetview.ElevationApi.getElevationData.ElevationService.elevationInstanceData
//import com.google.android.gms.maps.model.LatLng
//import retrofit2.Call
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//object getElevationData {
//fun getElevationList(latLng: LatLng){
//    val elevationResponse=   elevationInstanceData.getElevationData(latLng)
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    const val BASE_URL="https://elevation-api.io/"
//   lateinit var latLng:LatLng
//    //const val API_KEY=""
////    fun getElevation(@Query("points")LatLng:LatLng) : Call<ElevationsResponse>
//    interface ElevationInterfaceData{
//        @GET("api/elevation?key=Ed4G6A-Ny9-Dxw6346a5z9134--iQ3&points=")
//        fun getElevationData(@Query("")latLng:): Call<ElevationsResponse>
//    }
//
//    object ElevationService {
//        lateinit var elevationInstanceData:ElevationInterfaceData
//
//
//        init {
//            val retrofit= Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            elevationInstanceData=retrofit.create(ElevationInterfaceData::class.java)
//            Log.d("thissss", retrofit.toString())
//
//        }
//    }
//}
//
//
//
//
//
//
