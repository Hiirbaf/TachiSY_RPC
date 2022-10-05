@file:Suppress("UseSwitchCompatOrMaterialCode", "UnusedImport", "UNUSED_VARIABLE", "SetTextI18n",
    "UsePropertyAccessSyntax", "SpellCheckingInspection", "StaticFieldLeak",
    "LiftReturnOrAssignment", "DEPRECATION", "BatteryLife"
)

package com.jery.tachisy_rpc

import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip

var token       : String = BuildConfig.dc_token_harry
var clipText    : String = "Testing"
var clipItem    : ClipData.Item? = null
lateinit var username   : Chip
lateinit var name       : Chip
lateinit var state      : Chip
lateinit var details    : EditText
lateinit var switch     : Switch
lateinit var footer     : Chip

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.chpUsername)
        name     = findViewById(R.id.chpName)
        state    = findViewById(R.id.chpState)
        details  = findViewById(R.id.edtDetails)
        switch   = findViewById(R.id.swtRPC)
        footer   = findViewById(R.id.chipFooter)

        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip())
            clipItem = clipboard.getPrimaryClip()?.getItemAt(0)!!
        @Suppress("SENSELESS_COMPARISON")
        if (clipItem != null) {
            clipText = clipItem!!.getText().toString()
            details.setText(clipText)
        }

        if (isServiceRunning(MyService::class.java)) {
            details.setText(setDetails)
            if(setState == "𝔐𝔞𝔫𝔤𝔞")  state.text="𝔐𝔞𝔫𝔤𝔞" else state.text="𝔐𝔞𝔫𝔥𝔴𝔞"
            switch.isChecked = true
        }

        username.setOnCloseIconClickListener {
            if (username.getText().toString() == "<--Harry#4627-->") {
                username.text = "jery_js#4490"
                token = BuildConfig.dc_token_jery
            } else if (username.getText().toString() == "jery_js#4490") {
                username.text = "<--Harry#4627-->"
                token = BuildConfig.dc_token_harry
            }
        }

        name.setOnCloseIconClickListener {
            if (name.getText().toString() == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
                name.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
                state.text = "𝐀𝐧𝐢𝐦𝐞"
            }
            else if (name.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") {
                name.text = "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘"
                state.text = "𝔐𝔞𝔫𝔤𝔞"
            }
        }

        state.setOnCloseIconClickListener {
            if (state.getText().toString() == "𝔐𝔞𝔫𝔤𝔞")
                state.text = "𝔐𝔞𝔫𝔥𝔴𝔞"
            else if (state.getText().toString() == "𝔐𝔞𝔫𝔥𝔴𝔞")
                state.text = "𝔐𝔞𝔫𝔤𝔞"
        }

        switch.setOnClickListener {
            if (switch.isChecked())
                startService(Intent(this, MyService::class.java))
            else
                stopService(Intent(this, MyService::class.java))
        }

        footer.setOnLongClickListener {
            val packageName = packageName
            val intent = Intent()
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Battery optimization already disabled", Toast.LENGTH_LONG).show()
            }
            true
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className)
                return true
        }
        return false
    }
}
