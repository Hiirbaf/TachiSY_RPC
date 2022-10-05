package com.jery.tachisyrpc

import android.app.Notification
import android.content.Intent
import android.os.IBinder
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.Service
import android.content.Context
import com.my.kizzyrpc.KizzyRPCservice
import java.lang.UnsupportedOperationException

class MyService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        Toast.makeText(this, MainActivity.clipText, Toast.LENGTH_SHORT).show();
        rpc.setName(MainActivity.Name!!.text.toString())
            .setState(MainActivity.State!!.text.toString())
            .setDetails(MainActivity.Details!!.text.toString())
            .setApplicationId("962990036020756480")
            .setLargeImage("attachments/961577469427736636/971135180322529310/unknown.png")
            .setSmallImage("attachments/949382602073210921/1001372717783711814/reading-icon.png")
            .setType(0)
            .setStartTimestamps(System.currentTimeMillis())
//            .setButton1("Button1", "https://youtu.be/1yVm_M1sKBE")
//            .setButton2("Button2", "https://youtu.be/1yVm_M1sKBE")
            .setStatus("online")
            .build()
        notification()
        setDetails = MainActivity.Details!!.text.toString()
        return START_STICKY
    }

    override fun onDestroy() {
//        Toast.makeText(this, "Stop RPC", Toast.LENGTH_SHORT).show();
        rpc.closeRPC()
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun notification() {
        val context: Context = this
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "Discord RPC",
                "Discord RPC",
                NotificationManager.IMPORTANCE_MIN
            )
        )
        val builder = Notification.Builder(context, "Discord RPC")
        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(MainActivity.Name!!.text.toString())
            .setContentText(MainActivity.Details!!.text.toString())
            .setSubText(MainActivity.State!!.text.toString())
            .setUsesChronometer(true)
        startForeground(11234, builder.build())
    }

    companion object {
        var rpc = KizzyRPCservice(MainActivity.Token!!.text.toString())
        var setDetails: String? = null
    }
}