@file:Suppress("UseSwitchCompatOrMaterialCode", "UnusedImport", "UNUSED_VARIABLE", "SetTextI18n",
    "UsePropertyAccessSyntax", "SpellCheckingInspection", "StaticFieldLeak",
    "LiftReturnOrAssignment", "DEPRECATION", "BatteryLife"
)

package com.jery.tachisy_rpc

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.chip.Chip


class MainActivity : AppCompatActivity() {
    companion object {
        var token       : String = BuildConfig.dc_token_jery
        var largeImage  : String = "attachments/961577469427736636/971135180322529310/unknown.png"
        var smallImage  : String = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
//        var clipText    : String = "Testing"
//        var clipItem    : ClipData.Item? = null

        lateinit var username   : Chip
        lateinit var name       : Chip
        lateinit var state      : Chip
        lateinit var details    : EditText
        lateinit var switch     : Switch
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.chpUsername)
        name = findViewById(R.id.chpName)
        state = findViewById(R.id.chpState)
        details = findViewById(R.id.edtDetails)
        switch = findViewById(R.id.swtRPC)

//        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//        if (clipboard.hasPrimaryClip())
//            clipItem = clipboard.getPrimaryClip()?.getItemAt(0)!!
//        @Suppress("SENSELESS_COMPARISON")
//        if (clipItem != null) {
//            clipText = clipItem!!.getText().toString()
//            details.setText(clipText)
//        }

        if (isServiceRunning(MyService::class.java)) {
            username.setText(MyService.setUsername)
            name.setText(MyService.setName)
            state.setText(MyService.setState)
            details.setText(MyService.setDetails)
            token = MyService.setToken.toString()
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

        /*name.setOnCloseIconClickListener {
            if (name.getText().toString() == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
                name.text = "𝐀𝐧𝐢𝐦𝐞"
                state.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
                name.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_aniyomi)
                state.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_watching)
                largeImage = "attachments/949382602073210921/1002240570091122798/Aniyomi.png"
                smallImage = "attachments/949382602073210921/1002240620569567404/watching-icon.png"
            } else {       // if (name.getText().toString() == "𝐀𝐧𝐢𝐦𝐞" || "𝙰𝚗𝚒𝚢𝚘𝚖𝚒")
                name.text = "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘"
                state.text = "𝔐𝔞𝔫𝔤𝔞"
                name.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_tachiyomi)
                state.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_reading)
                largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
                smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
        }*/
        name.setOnCloseIconClickListener {
            if (name.getText().toString() == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
                name.text = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
                state.text = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
                name.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_moon_reader)
                state.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_reading_ln)
                largeImage = "attachments/949382602073210921/1031952390636707930/moon-reader-pro.png"
                smallImage = "attachments/949382602073210921/994460304626962484/Reading-Icon.png"
            } else {       // if (name.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥" || "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯")
                name.text = "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘"
                state.text = "𝔐𝔞𝔫𝔤𝔞"
                name.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_tachiyomi)
                state.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_reading)
                largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
                smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
        }

        state.setOnCloseIconClickListener {
            if (state.getText().toString() == "𝔐𝔞𝔫𝔤𝔞")
                state.text = "𝔐𝔞𝔫𝔥𝔴𝔞"
            else if (state.getText().toString() == "𝔐𝔞𝔫𝔥𝔴𝔞")
                state.text = "𝔐𝔞𝔫𝔤𝔞"
            if (state.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") {
                name.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
                state.text = "𝐀𝐧𝐢𝐦𝐞"
                name.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_watching)
                state.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_aniyomi)
            } else if (state.getText().toString() == "𝐀𝐧𝐢𝐦𝐞") {
                name.text = "𝐀𝐧𝐢𝐦𝐞"
                state.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
                name.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_aniyomi)
                state.chipIcon = AppCompatResources.getDrawable(this, R.drawable.ic_watching)
            }
        }

        switch.setOnClickListener {
            if (switch.isChecked())
                startService(Intent(this, MyService::class.java))
            else
                stopService(Intent(this, MyService::class.java))
        }

        findViewById<Chip>(R.id.chpFooter).setOnLongClickListener {
            val packageName = packageName
            val intent = Intent()
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Battery optimization already disabled",
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        }

        findViewById<Button>(R.id.button).setOnClickListener{
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", details.text.toString())
            if (clip != null) {
                @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
                clipboard!!.setPrimaryClip(clip!!)
            }
        }
//        findViewById<Button>(R.id.button2).setOnClickListener{
//            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("label", details.text.toString())
//            if (clip != null) {
//                clipboard!!.setPrimaryClip(clip!!)
//            }
//        }
        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        findViewById<Button>(R.id.button3).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Discord Login")
                .setMessage("Do you really want to login to Discord?")
                .setIcon(R.drawable.ic_discord)
                .setPositiveButton("Yes") { dialog, whichButton ->
                    Toast.makeText(this@MainActivity, "Whoopsies! I havent added the ability to login yet", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null).show()
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