@file:Suppress("SimplifyBooleanWithConstants")

package com.jery.tachisy_rpc

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.blankj.utilcode.util.NotificationUtils
import com.jery.tachisy_rpc.MainActivity.Companion.sharedPreferences
import com.jery.tachisy_rpc.rpc.RPCService
import com.jery.tachisy_rpc.utils.Logic


class MyService : Service() {
    companion object {
        // Set up some public variables that will be used between classes and activities
        var setName: String? = null
        var setState: String? = null
        var setDetails: String? = null
        var setType: Int = 0
        var setCh: Int = 0
        // Variables that can be referred to from other activities and classes
        const val ACTION_STOP_SERVICE = "Stop RPC"
        const val ACTION_RESTART_SERVICE = "Restart RPC"
        const val ACTION_OPEN_APP = "Open App"
        const val ACTION_SUB_ONE = "Subtract One"
        const val ACTION_ADD_ONE = "Add One"
        const val CHANNEL_ID = "Discord RPC"
        const val CHANNEL_NAME = "Discord RPC"
    }

    // set up some variables for the internal operations
    private var token = MainActivity.Token
    private var type = 0
    private var chType = MainActivity.arrayOfTypes[MainActivity.numType.value]
    var startTimestamp: Long = sharedPreferences.getLong("keyStartTimestamp", System.currentTimeMillis())

    private var context: Context? = this
    private var restartService: Boolean? = false
    private val rpc = RPCService(token) //Discord account token

    // Variables for the notification
    private lateinit var notiBtnOneIntent: Intent
    private lateinit var notiBtnTwoIntent: Intent
    private lateinit var notiBtnOneText: String
    private lateinit var notiBtnTwoText: String

    // When this service is started from another activity
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If the Exit button is pressed in Notification
        if (intent?.action.equals(ACTION_STOP_SERVICE)) stopSelf()
        // If the Open button is pressed in Notification
        else if (intent?.action.equals(ACTION_OPEN_APP)) {
            val launchIntent = Intent(this, MainActivity::class.java)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(launchIntent)
        }
        // If the Restart button is pressed in Notification
        else if (intent?.action.equals(ACTION_RESTART_SERVICE)) {
            restartService = true
            stopSelf()
        }
        // If the -1 btn is pressed in the Notification
        else if (intent?.action.equals(ACTION_SUB_ONE)) {
            MainActivity.numChapter.value = MainActivity.numChapter.value - 1
            println("new chapter number = ${MainActivity.numChapter.value}")
            Logic.saveToLastState()
            restartService = true
            stopSelf()
        }
        // If the +1 btn is pressed in the Notification
        else if (intent?.action.equals(ACTION_ADD_ONE)) {
            MainActivity.numChapter.value = MainActivity.numChapter.value + 1
            println("new chapter number = ${MainActivity.numChapter.value}")
            Logic.saveToLastState()
            restartService = true
            stopSelf()
        }
        // When the service starts (And no button is pressed in notification (obviously))
        else {
            // setup vars that will be used by the rich presence service
            loadRPCData(this)
            Logic.saveToLastState()

            var chapterType = " $chType "   // surround chType with spaces
            var chapterNumber = MainActivity.numChapter.value.toString()
            if (chType == "") { chapterType = ""; chapterNumber = "" }  // If chType is blank, then empty the chapterType and chapterNumber

            if (MainActivity.chpName.text == Logic.v_Anime) {
                type = 3
                Toast.makeText(this, "${getString(R.string.watching)} ${MainActivity.chpName.text}\n${MainActivity.chpState.text}:「${MainActivity.edtDetails.text.trim()} $chapterType $chapterNumber」", Toast.LENGTH_SHORT).show()
            }else {
                type = 0
                Toast.makeText(this, "${getString(R.string.playing)} ${MainActivity.chpName.text}\n${MainActivity.chpState.text}:「${MainActivity.edtDetails.text.trim()} $chapterType $chapterNumber」", Toast.LENGTH_SHORT).show()
            }
            val activityDetails = "${MainActivity.edtDetails.text.trim()}$chapterType$chapterNumber"

            // Display Notification
            if (!NotificationUtils.areNotificationsEnabled()) {
                val permission = "android.permission.POST_NOTIFICATIONS"
                val activity = MainActivity.Main_Activity
                // Check whether the permission rationale is required
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    AlertDialog.Builder(activity)
                        .setTitle(getString(R.string.permission_needed))
                        .setMessage(R.string.notification_permission_is_needed)
                        .setPositiveButton("Ok") { _, _ ->
                            ActivityCompat.requestPermissions(activity, arrayOf(permission), 101)
                        }
                        .setNegativeButton("Cancel", null)
                        .create().show()
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(permission), 101)
                }
            } else {
                // Create a new channel in notifications
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
                channel.description = getString(R.string.notification_description)
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

                if (activityDetails.matches(Regex("^.+\\s((Vol)|(Ch)|(Ep))\\s\\d{1,4}\\s*$"))) {
                    notiBtnOneIntent = subIntent
                    notiBtnTwoIntent = addIntent
                    notiBtnOneText = "-1 $chType"
                    notiBtnTwoText = "+1 $chType"
                } else {
                    notiBtnOneIntent = openIntent
                    notiBtnTwoIntent = restartIntent
                    notiBtnOneText = getString(R.string.Open)
                    notiBtnTwoText = getString(R.string.Restart)
                }

                // set startTimestamp based on persistentTimestamp preference
                startTimestamp = if (sharedPreferences.getBoolean("keyPersistentTimestamp", false))
                    sharedPreferences.getLong("keyStartTimestamp", System.currentTimeMillis())
                else
                    System.currentTimeMillis()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    @Suppress("DEPRECATION")
                    startForeground(
                        99961,
                        Notification.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_rpc_placeholder)
                            .setContentTitle(MainActivity.chpName.text)
                            .setContentText("「$activityDetails」")
                            .setSubText(MainActivity.chpState.text)
                            .setUsesChronometer(true)
                            .setWhen(startTimestamp)
                            .addAction(R.drawable.ic_rpc_placeholder, getString(R.string.Exit), PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE))
                            .addAction(R.drawable.ic_rpc_placeholder, notiBtnOneText, PendingIntent.getService(this, 0, notiBtnOneIntent, PendingIntent.FLAG_IMMUTABLE))
                            .addAction(R.drawable.ic_rpc_placeholder, notiBtnTwoText, PendingIntent.getService(this, 0, notiBtnTwoIntent, PendingIntent.FLAG_IMMUTABLE))
                            .setOngoing(true)
                            .setFlag(Notification.FLAG_NO_CLEAR,true)
                            .build()
                    )
                }
            }

            // Setup Rich Presence
            rpc.setName(MainActivity.chpName.text.toString())
                .setState(MainActivity.chpState.text.toString())
                .setDetails("「$activityDetails」")
                .setLargeImage(Logic.largeImage)
                .setSmallImage(Logic.smallImage)
                .setType(type)
                .setStartTimestamps(startTimestamp)
                .setStatus("online")
                .build()

            // Save the variables to active memory so that they can be called by MainActivity's onCreate()
            MainActivity.swtSwitch.isChecked = true
            setName = MainActivity.chpName.text.toString()
            setState = MainActivity.chpState.text.toString()
            setDetails = MainActivity.edtDetails.text.toString().trim()
            setType = MainActivity.numType.value
            setCh = MainActivity.numChapter.value
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
            Toast.makeText(this, getString(R.string.Stopping_RPC), Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    /**
     * Set the largeImage and smallImage urls that are passed are passed on to the rich presence
     * @param activity The activity that calls this function
     */
    fun loadRPCData(activity: MyService) {
        when {
            MainActivity.chpName.getText().toString() == Logic.v_TachiyomiSy -> {
                Logic.largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
                Logic.smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
            MainActivity.chpName.getText().toString() == Logic.v_LightNovel || MainActivity.chpName.getText().toString() == Logic.v_MoonReader -> {
                Logic.largeImage = "attachments/949382602073210921/1031952390636707930/moon-reader-pro.png"
                Logic.smallImage = "attachments/949382602073210921/994460304626962484/Reading-Icon.png"
            }
            MainActivity.chpName.getText().toString() == Logic.v_Aniyomi || MainActivity.chpName.getText().toString() == Logic.v_Anime -> {
                Logic.largeImage = "attachments/949382602073210921/1002240570091122798/Aniyomi.png"
                Logic.smallImage = "attachments/949382602073210921/1002240620569567404/watching-icon.png"
            }
            MainActivity.chpName.getText().toString() == Logic.v_Mangago -> {
                Logic.largeImage = "attachments/949382602073210921/1034172617311133847/mangago.jpg"
                Logic.smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
            MainActivity.chpName.getText().toString() == Logic.v_Webtoon -> {
                Logic.largeImage = "attachments/1035528103017066618/1035529154600382515/Webtoon.png"
                Logic.smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
        }
    }
}