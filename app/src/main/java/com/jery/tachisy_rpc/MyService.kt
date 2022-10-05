package com.jery.tachisy_rpc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.my.kizzyrpc.KizzyRPCservice

var setDetails: String? = null
var setState: String? = null


class MyService : Service() {

    private val rpc = KizzyRPCservice(token) //Discord account token

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        rpc.setName(name.text.toString())
            .setState(state.text.toString())
            .setDetails(details.text.toString())
            .setApplicationId("962990036020756480")
            .setLargeImage("attachments/961577469427736636/971135180322529310/unknown.png")
            .setSmallImage("attachments/949382602073210921/1001372717783711814/reading-icon.")
            .setType(0)
            .setStartTimestamps(System.currentTimeMillis())
//            .setButton1("Button1", "https://youtu.be/1yVm_M1sKBE")
//            .setButton2("Button2", "https://youtu.be/1yVm_M1sKBE")
            .setStatus("online")
            .build()
        notification()
        setDetails = details.text.toString()
        setState = state.text.toString()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        rpc.closeRPC()
    }

    private fun notification() {
        val context: Context = this
        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "Background Service",
                "Background Service",
                NotificationManager.IMPORTANCE_LOW
            )
        )
        val builder = Notification.Builder(context, "Background Service")
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(name.text.toString())
            .setContentText(details.text.toString())
            .setSubText(state.text.toString())
            .setUsesChronometer(true)
        startForeground(99961, builder.build())
    }
}