package org.robotics.blinkworld.location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.robotics.blinkworld.R
import org.robotics.blinkworld.Utils.*
import org.robotics.blinkworld.location.DefaultLocationClient
import org.robotics.blinkworld.models.User
import java.text.SimpleDateFormat
import java.util.*

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val firebaseLocationReference = database.child(NODE_USERS).child(currentUid()!!)
    private lateinit var mUser: User

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location ...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        locationClient.getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val latitude = location.latitude
                val longitude = location.longitude



                // Напиши сюда свою логику того, что делать с полученной локацией
                Log.d("TAG_MAP", "receiving location: $location")
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

                firebaseLocationReference.child(CHILD_LATITUDE).setValue(latitude)
                firebaseLocationReference.child(CHILD_LONGITUDE).setValue(longitude)
                val currentDate = sdf.format(Date()).toString()
                database.child(NODE_USERS).child(currentUid()!!)
                    .addListenerForSingleValueEvent(Utils.ValueEventListenerAdapter {
                        mUser = it.getValue(User::class.java)!!
                        if((latitude.toString()).take(6) != (mUser.latitude.toString()).take(6) && (longitude.toString()).take(6) != (mUser.longitude.toString()).take(6)){
                            database.child(NODE_USERS).child(currentUid()!!).child("Time").setValue(currentDate)
                        }

                            //onMapReady()



                    })

            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}