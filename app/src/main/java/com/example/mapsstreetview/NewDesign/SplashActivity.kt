package com.example.mapsstreetview.NewDesign

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsstreetview.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()



        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this, NewHomeScreen::class.java))
            finish()
        }, 3000)
    }

    override fun onStop() {
        super.onStop()
    }
}
