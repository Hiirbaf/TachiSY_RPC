@file:Suppress("DEPRECATION", "unused")

package com.jery.tachisy_rpc.utils

import android.app.ActivityManager
import android.content.Context
import com.jery.tachisy_rpc.MyService

object AppUtils {

    fun myServiceRunning(context: Context): Boolean {
        for (runningServiceInfo in (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
            Int.MAX_VALUE)) {
            if (MyService::class.java.name == runningServiceInfo.service.className)
                return true
        }
        return false
    }

}