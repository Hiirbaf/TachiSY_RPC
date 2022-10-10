package com.jery.tachisy_rpc

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.jery.tachisy_rpc.rpc.RPCService


@SuppressLint("UseSwitchCompatOrMaterialCode")
class MyService : Service() {
    companion object {
        // Set up some public variables that will be used between classes and activities
        var setUsername: String? = null
        var setName: String? = null
        var setState: String? = null
        var setDetails: String? = null
        var setToken: String? = null
    // Variables that can be referred to from other activities and classes
        const val ACTION_STOP_SERVICE = "Stop RPC"
        const val ACTION_RESTART_SERVICE = "Restart RPC"
        const val CHANNEL_ID = "Discord RPC"
        const val CHANNEL_NAME = "Discord RPC"
    }

    private var token = MainActivity.token
    private var username = MainActivity.username
    private var name = MainActivity.name
    private var state = MainActivity.state
    private var details = MainActivity.details
    private var switch = MainActivity.switch
    private var largeImage = MainActivity.largeImage
    private var smallImage = MainActivity.smallImage

    private var context: Context? = this
    private var restartService: Boolean? = false
    private val rpc = RPCService(token) //Discord account token

    // When this service is started from another activity
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If the Exit button is pressed in Notification
        if (intent?.action.equals(ACTION_STOP_SERVICE)) stopSelf()
        // If the Restart button is pressed in Notification
        else if (intent?.action.equals(ACTION_RESTART_SERVICE)) {
            restartService = true
            stopSelf()
        }
        // When the service starts (And no button is pressed in notification (obviously))
        else {
            Toast.makeText(this, "playing " + name.text.toString(), Toast.LENGTH_SHORT).show()
            var type = 0
            if (name.text.toString() == "𝐀𝐧𝐢𝐦𝐞") type = 3

            // Create a new channel in notifications
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "Notification displayed when rich presence is active"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

            val stopIntent = Intent(this, MyService::class.java)
            stopIntent.action = ACTION_STOP_SERVICE
            val restartIntent = Intent(this, MyService::class.java)
            restartIntent.action = ACTION_RESTART_SERVICE

            val pIntentStop: PendingIntent = PendingIntent.getService(this,
                0,stopIntent,PendingIntent.FLAG_IMMUTABLE)
            val pIntentRestart: PendingIntent = PendingIntent.getService(this,
                0,restartIntent,PendingIntent.FLAG_IMMUTABLE)

            rpc.setName(name.text.toString())
                .setState(state.text.toString())
                .setDetails(details.text.toString())
                .setLargeImage(largeImage)
                .setSmallImage(smallImage)
                .setType(type)
                .setStartTimestamps(System.currentTimeMillis())
//            .setStopTimestamps(System.currentTimeMillis())
//            .setButton1("Button1", "https://youtu.be/1yVm_M1sKBE")
//            .setButton2("Button2", "https://youtu.be/1yVm_M1sKBE")
                .setStatus("online")
                .build()

            switch.isChecked = true
            setUsername = username.text.toString()
            setName = name.text.toString()
            setState = state.text.toString()
            setDetails = details.text.toString()
            setToken = token

            @Suppress("DEPRECATION")
            startForeground(
                99961,
                Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_rpc_placeholder)
                    .setContentTitle(name.text.toString())
                    .setContentText(details.text.toString())
                    .setSubText(state.text.toString())
                    .setUsesChronometer(true)
                    .addAction(R.drawable.ic_rpc_placeholder, "Exit", pIntentStop)
                    .addAction(R.drawable.ic_rpc_placeholder, "Restart", pIntentRestart)
                    .build()
            )

            return START_STICKY
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        rpc.closeRPC()
        switch.isChecked = false
        if (restartService == true)
            startService(Intent(this, MyService::class.java))
        else
            Toast.makeText(this, "Stopping RPC", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}