package org.robotics.blinkworld.Activity.NoPremissionActivity

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.robotics.blinkworld.Activity.MainActivity
import org.robotics.blinkworld.R

class DisabledGPSActivity : AppCompatActivity() {
    var gpsStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disabled_gpsactivity)
        val mainHandler = Handler(Looper.getMainLooper())
        var i = 0



            mainHandler.post(object : Runnable {
                override fun run() {
                    if (i == 0) {
                        locationEnabled()
                        if (gpsStatus) {
                            i = 1
                            finish()

                        }
                    }

                    mainHandler.postDelayed(this, 1000)
                }
            })
        }



    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}