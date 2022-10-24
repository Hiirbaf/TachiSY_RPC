@file:Suppress("UseSwitchCompatOrMaterialCode", "UnusedImport", "UNUSED_VARIABLE", "SetTextI18n",
    "UsePropertyAccessSyntax", "SpellCheckingInspection", "StaticFieldLeak",
    "LiftReturnOrAssignment", "DEPRECATION", "BatteryLife", "ApplySharedPref", "UNUSED_ANONYMOUS_PARAMETER"
)

package com.jery.tachisy_rpc

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.jery.tachisy_rpc.utils.Logic


class MainActivity : AppCompatActivity() {
    companion object {
        var token       : String = BuildConfig.dc_token_jery

        lateinit var username   : Chip
        lateinit var name       : Chip
        lateinit var state      : Chip
        lateinit var details    : EditText
        lateinit var switch     : Switch
    }

    override fun onCreate(savedInstanceState: Bundle?) {
    // Everything below this will be done when the app is opened.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // assign the screen components to variables that can be accessed from within other classes and methods.
        username = findViewById(R.id.chpUsername)
        name = findViewById(R.id.chpName)
        state = findViewById(R.id.chpState)
        details = findViewById(R.id.edtDetails)
        switch = findViewById(R.id.swtRPC)

        // From sharedPrefs, restore the name first and then the remaining keys
        name.setText(getSharedPreferences("lastState", Context.MODE_PRIVATE).getString("keyName","𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘"))
        state.setText(getSharedPreferences("lastState", Context.MODE_PRIVATE).getString("keyState", "𝔐𝔞𝔫𝔤𝔞"))
        restoreFromLastState()
        // load the right chipIcon when restoring lastState
        Logic.restoreCorrectDataOnCreate(this)

        // restore the details entered last if MyService is running
        if (isServiceRunning(MyService::class.java)) {
            username.setText(MyService.setUsername)
            name.setText(MyService.setName)
            state.setText(MyService.setState)
            details.setText(MyService.setDetails)
            token = MyService.setToken.toString()
            switch.isChecked = true
        }


        // toggle between discord accounts
        username.setOnCloseIconClickListener {
            if (username.getText().toString() == "<--Harry#4627-->") {
                username.text = "jery_js#4490"
                token = BuildConfig.dc_token_jery
            } else if (username.getText().toString() == "jery_js#4490") {
                username.text = "<--Harry#4627-->"
                token = BuildConfig.dc_token_harry
            }
        }
        // switch between differnet presets
        name.setOnCloseIconClickListener {
            Logic.nameWasChanged(this)
            restoreFromLastState()
        }

        // inter-switch the name and state chips
        state.setOnCloseIconClickListener {
            Logic.stateWasChanged(this)
        }

        // Start or Stop RPC and save details to sharedPrefs on click
        switch.setOnClickListener {
            if (switch.isChecked())
                startService(Intent(this, MyService::class.java))
            else
                stopService(Intent(this, MyService::class.java))

            saveToLastState()
        }

        // Long press footer to disable battery optimization
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

        // Copy text in edtDetails to the clipboard
        findViewById<Button>(R.id.button).setOnClickListener{
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", details.text.toString())
            clipboard.setPrimaryClip(clip!!)
        }
        // Get text in clipboard and show in edtDetails
        findViewById<Button>(R.id.button2).setOnClickListener{
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipItem = clipboard.primaryClip!!.getItemAt(0)
            Toast.makeText(this, clipItem.text!!, Toast.LENGTH_SHORT).show()
            details.setText(clipItem.text!!)
            restoreFromLastState()
        }
        // Show a dummy method to demonstrate discord login
        findViewById<Button>(R.id.button3).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Discord Login")
                .setMessage("Do you really want to login to Discord?")
                .setIcon(R.drawable.ic_discord)
                .setPositiveButton("Yes") { dialog, whichButton -> Toast.makeText(this@MainActivity, "Whoopsies! I havent added the ability to login yet", Toast.LENGTH_LONG).show() }
                .setNegativeButton("No", null).show()
        }
    }

    // restore all details from sharedPrefs
    private fun restoreFromLastState() {
        val sharedPreferences : SharedPreferences = getSharedPreferences("lastState", Context.MODE_PRIVATE)
        // set the saved edtDetails from sharedPrefs
        if (name.text == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘")
            details.setText(sharedPreferences.getString("keyDetails_tachi", ""))
        else if (name.text == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨")
            details.setText(sharedPreferences.getString("keyDetails_mangago", ""))
        else if ((name.text == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (name.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"))
            details.setText(sharedPreferences.getString("keyDetails_ln", ""))
        else if ((name.text == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (name.text == "𝐀𝐧𝐢𝐦𝐞"))
            details.setText(sharedPreferences.getString("keyDetails_anime", ""))
    }

    // save all details to sharedPrefs
    private fun saveToLastState() {
        val sharedPreferences : SharedPreferences = getSharedPreferences("lastState", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        // extract chpName, chpState and edtDetails to sharedPrefs
        prefsEditor.putString("keyName", name.text.toString())
        prefsEditor.putString("keyState", state.text.toString())

        if (name.text == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘")
            prefsEditor.putString("keyDetails_tachi", details.text.toString()).commit()
        else if (name.text == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨")
            prefsEditor.putString("keyDetails_mangago", details.text.toString()).commit()
        else if ((name.text == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (name.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"))
            prefsEditor.putString("keyDetails_ln", details.text.toString()).commit()
        else if ((name.text == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (name.text == "𝐀𝐧𝐢𝐦𝐞"))
            prefsEditor.putString("keyDetails_anime", details.text.toString()).commit()
    }

    // check whether a given service is running or not and return it as a boolean value.
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className)
                return true
        }
        return false
    }
}