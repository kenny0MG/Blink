package org.robotics.blinkworld.Activity

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class App: Application() {
    val locationLiveData = MutableLiveData<Location>()

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Сохраняем локацию даже если оффлайн, чтобы потом выгрузить
        Firebase.database.setPersistenceEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        lateinit var instance: App
    }
}