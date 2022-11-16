package com.example.mapsstreetview.NewDesign

import android.os.Bundle
import android.widget.Toast
import com.example.mapsstreetview.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class LiveCams : YouTubeBaseActivity() {

    // Change the AppCompactActivity to YouTubeBaseActivity()

    // Add the api key that you had
    // copied from google API
    // This is a dummy api key
    val api_key =  "AIzaSyAFGULpYnF711dLVMyJT6xJ81MqaMzqVSg"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_cams)





        val url=intent.getStringExtra("url")

        // Get reference to the view of Video player
        val ytPlayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)

        ytPlayer.initialize(api_key, object : YouTubePlayer.OnInitializedListener{
            // Implement two methods by clicking on red error bulb
            // inside onInitializationSuccess method
            // add the video link or the
            // playlist link that you want to play
            // In here we also handle the play and pause functionality
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.loadVideo(url)
                player?.play()
            }

            // Inside onInitializationFailure
            // implement the failure functionality
            // Here we will show toast
            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@LiveCams , "Video player Failed" , Toast.LENGTH_SHORT).show()
            }
        })
    }
}