package com.jery.tachisy_rpc

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.my.kizzyrpc.KizzyRPCservice

var setUsername: String? = null
var setName: String? = null
var setState: String? = null
var setDetails: String? = null
var setToken: String? = null


class MyService : Service() {

    private val rpc = KizzyRPCservice(token) //Discord account token

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "playing "+ name.text.toString(), Toast.LENGTH_SHORT).show()
        var type = 0
        if (name.text.toString() == "𝐀𝐧𝐢𝐦𝐞") type = 3

        rpc.setName(name.text.toString())
            .setState(state.text.toString())
            .setDetails(details.text.toString())
            .setLargeImage(largeImage)
            .setSmallImage(smallImage)
            .setType(type)
            .setApplicationId("962990036020756480")
            .setStartTimestamps(System.currentTimeMillis())
//            .setStopTimestamps(System.currentTimeMillis())
//            .setButton1("Button1", "https://youtu.be/1yVm_M1sKBE")
//            .setButton2("Button2", "https://youtu.be/1yVm_M1sKBE")
            .setStatus("online")
            .build()
        notification()
        setUsername = username.text.toString()
        setName = name.text.toString()
        setState = state.text.toString()
        setDetails = details.text.toString()
        setToken = token
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        rpc.closeRPC()
        Toast.makeText(this, "Stopping RPC", Toast.LENGTH_SHORT).show()
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

        val pendingIntent: PendingIntent = PendingIntent.getService(
            this,
            0,
            (Intent(this, MyService::class.java)),
            PendingIntent.FLAG_IMMUTABLE
        )

        builder.setSmallIcon(R.drawable.ic_rpc_placeholder)
            .setContentTitle(name.text.toString())
            .setContentText(details.text.toString())
            .setSubText(state.text.toString())
            .setUsesChronometer(true)
            .addAction(R.drawable.ic_rpc_placeholder, "Exit", pendingIntent)
        startForeground(99961, builder.build())
    }
}