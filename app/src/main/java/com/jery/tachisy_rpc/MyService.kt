package com.jery.tachisy_rpc

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.jery.tachisy_rpc.rpc.RPCService
import com.jery.tachisy_rpc.utils.Logic


@SuppressLint("UseSwitchCompatOrMaterialCode")
class MyService : Service() {
    companion object {
        // Set up some public variables that will be used between classes and activities
        var setName: String? = null
        var setState: String? = null
        var setDetails: String? = null
        // Variables that can be referred to from other activities and classes
        const val ACTION_STOP_SERVICE = "Stop RPC"
        const val ACTION_RESTART_SERVICE = "Restart RPC"
        const val ACTION_OPEN_APP = "Open App"
        const val ACTION_SUB_ONE = "Subtract One"
        const val ACTION_ADD_ONE = "Add One"
        const val CHANNEL_ID = "Discord RPC"
        const val CHANNEL_NAME = "Discord RPC"
    }

    private var token = MainActivity.chpUsername.text.toString()

    private var context: Context? = this
    private var restartService: Boolean? = false
    private val rpc = RPCService(token) //Discord account token

    private lateinit var notiBtnOneIntent: Intent
    private lateinit var notiBtnTwoIntent: Intent
    private lateinit var notiBtnOneText: String
    private lateinit var notiBtnTwoText: String

    // When this service is started from another activity
    @SuppressLint("SetTextI18n")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If the Exit button is pressed in Notification
        if (intent?.action.equals(ACTION_STOP_SERVICE)) stopSelf()
        // If the Open button is pressed in Notification
        if (intent?.action.equals(ACTION_OPEN_APP)) {
            val launchIntent = Intent(this, MainActivity::class.java)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
        }
        // If the Restart button is pressed in Notification
        else if (intent?.action.equals(ACTION_RESTART_SERVICE)) {
            restartService = true
            stopSelf()
        }
        else if (intent?.action.equals(ACTION_SUB_ONE)) {
            MainActivity.edtDetails.setText("Subtracted One")
            restartService = true
            stopSelf()
        }
        else if (intent?.action.equals(ACTION_ADD_ONE)) {
            MainActivity.edtDetails.setText("Added One")
            restartService = true
            stopSelf()
        }
        // When the service starts (And no button is pressed in notification (obviously))
        else {
            // Create a new channel in notifications
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            channel.description = "Notification displayed when rich presence is active"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

            val stopIntent = Intent(this, MyService::class.java)
            stopIntent.action = ACTION_STOP_SERVICE
            val openIntent = Intent(this, MyService::class.java)
            openIntent.action = ACTION_OPEN_APP
            val restartIntent = Intent(this, MyService::class.java)
            restartIntent.action = ACTION_RESTART_SERVICE
            val subIntent = Intent(this, MyService::class.java)
            subIntent.action = ACTION_SUB_ONE
            val addIntent = Intent(this, MyService::class.java)
            addIntent.action = ACTION_ADD_ONE

            if (MainActivity.edtDetails.text.matches(Regex("^.+(Vol)|(Ch)|(Ep)\\.?\\s?\\d{1,4}\\s*$"))) {
                notiBtnOneIntent = subIntent
                notiBtnTwoIntent = addIntent
                notiBtnOneText = "-1"
                notiBtnTwoText = "+1"
            } else {
                notiBtnOneIntent = openIntent
                notiBtnTwoIntent = restartIntent
                notiBtnOneText = "Open"
                notiBtnTwoText = "Restart"
            }

            Logic.loadRPCData(this)

            rpc.setName(MainActivity.chpName.text.toString())
                .setState(MainActivity.chpState.text.toString())
                .setDetails("「" + MainActivity.edtDetails.text.toString() + "」")
                .setLargeImage(Logic.largeImage)
                .setSmallImage(Logic.smallImage)
                .setType(Logic.type)
                .setStartTimestamps(System.currentTimeMillis())
//            .setStopTimestamps(System.currentTimeMillis())
//            .setButton1("Button1", "https://youtu.be/1yVm_M1sKBE")
//            .setButton2("Button2", "https://youtu.be/1yVm_M1sKBE")
                .setStatus("online")
                .build()

            MainActivity.swtSwitch.isChecked = true
            setName = MainActivity.chpName.text.toString()
            setState = MainActivity.chpState.text.toString()
            setDetails = MainActivity.edtDetails.text.toString()

            @Suppress("DEPRECATION")
            startForeground(
                99961,
                Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_rpc_placeholder)
                    .setContentTitle(MainActivity.chpName.text.toString())
                    .setContentText(MainActivity.edtDetails.text.toString())
                    .setSubText(MainActivity.chpState.text.toString())
                    .setUsesChronometer(true)
                    .addAction(R.drawable.ic_rpc_placeholder, "Exit", PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE))
                    .addAction(R.drawable.ic_rpc_placeholder, notiBtnOneText, PendingIntent.getService(this, 0, notiBtnOneIntent,PendingIntent.FLAG_IMMUTABLE))
                    .addAction(R.drawable.ic_rpc_placeholder, notiBtnTwoText, PendingIntent.getService(this, 0, notiBtnTwoIntent,PendingIntent.FLAG_IMMUTABLE))
                    .build()
            )

            return START_STICKY
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        rpc.closeRPC()
        MainActivity.swtSwitch.isChecked = false
        if (restartService == true)
            startService(Intent(this, MyService::class.java))
        else
            Toast.makeText(this, "Stopping RPC", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}