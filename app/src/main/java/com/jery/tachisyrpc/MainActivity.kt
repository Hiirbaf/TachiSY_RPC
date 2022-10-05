package com.jery.tachisyrpc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.chip.Chip
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import android.os.PowerManager
import android.app.NotificationManager
import android.app.ActivityManager
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Switch

@SuppressLint("StaticFieldLeak, UseSwitchCompatOrMaterialCode, BatteryLife")
class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        Token = findViewById<View>(R.id.chipToken) as Chip

        //        Token.setTooltipText("<--Harry-->#4627");
        Name = findViewById<View>(R.id.edittextName) as EditText
        State = findViewById<View>(R.id.edittextState) as EditText
        Details = findViewById<View>(R.id.edittextDetails) as EditText // Reference Text : Akatsuki no Yona
        Switch = findViewById<View>(R.id.switchRPC) as Switch
        Footer = findViewById<View>(R.id.chipFooter) as Chip
        Token!!.setOnClickListener { v: View? ->
            if (Token!!.text == "OTYxNTc2MjY1MzMxMTM0NDg2.GOGIQ_.yuC6PUXa89ICuBNZEdrutD6-zaWYlfI-qMmWNQ") {
                Token!!.text =
                    "OTQ1MTk2MDc5ODQ1MDgxMTIw.GYyHzW.PPUz-WxacFdGNHtIjLqjj3hk7Nqai_sCLAH3Qo" //jery99961
                Token!!.tooltipText = "jery_js#4490"
            } else {
                Token!!.text =
                    "OTYxNTc2MjY1MzMxMTM0NDg2.GOGIQ_.yuC6PUXa89ICuBNZEdrutD6-zaWYlfI-qMmWNQ" //jery2005may
                Token!!.tooltipText = "<--Harry-->#4627"
            }
        }
        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard!!.hasPrimaryClip())
            clipItem = clipboard!!.primaryClip!!.getItemAt(0)
        if (clipItem != null) {
            clipText = clipItem!!.text.toString()
            Details!!.setText(clipText)
        }

//        Switch.setChecked(isServiceRunning(MyService.class));
        if (isServiceRunning(MyService::class.java)) {
            Details!!.setText(MyService.setDetails)
            Switch!!.isChecked = true
        }
        Switch!!.setOnClickListener { v: View? ->
            if (Switch!!.isChecked) {
                Toast.makeText(this@MainActivity, Details!!.text.toString(), Toast.LENGTH_SHORT)
                    .show()
                startService(Intent(this@MainActivity, MyService::class.java))
            } else {
                Toast.makeText(this@MainActivity, "Turning off RPC", Toast.LENGTH_SHORT).show()
                stopService(Intent(this@MainActivity, MyService::class.java))
            }
        }
        Footer!!.setOnLongClickListener { v: View? ->
            val intent = Intent()
            val packageName = packageName
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Battery optimization already disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
    }

    val isNotificationPresent: Boolean
        get() {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationArray = notificationManager.activeNotifications
            return notificationArray != null && notificationArray.size > 0
        }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        @JvmField
        var Token: Chip? = null
        var Footer: Chip? = null
        @JvmField
        var Name: EditText? = null
        @JvmField
        var State: EditText? = null
        @JvmField
        var Details: EditText? = null
        var Switch: Switch? = null
        var activity: Activity? = null
        var clipboard: ClipboardManager? = null
        var clipItem: ClipData.Item? = null
        var clipText = "Testing"
    }
}