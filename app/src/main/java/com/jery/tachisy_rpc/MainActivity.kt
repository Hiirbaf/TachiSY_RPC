@file:Suppress("UseSwitchCompatOrMaterialCode", "UnusedImport", "UNUSED_VARIABLE", "SetTextI18n",
    "UsePropertyAccessSyntax", "SpellCheckingInspection", "StaticFieldLeak",
    "LiftReturnOrAssignment", "DEPRECATION", "BatteryLife", "ApplySharedPref", "UNUSED_ANONYMOUS_PARAMETER",
    "UNUSED_PARAMETER"
)

package com.jery.tachisy_rpc

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.jery.tachisy_rpc.utils.Logic


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var chpUsername   : Chip
        lateinit var chpName       : Chip
        lateinit var chpState      : Chip
        lateinit var edtDetails    : EditText
        lateinit var swtSwitch     : Switch

        lateinit var sharedPreferences : SharedPreferences
        lateinit var prefsEditor: SharedPreferences.Editor
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
    // Everything below this will be done when the app is opened.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("lastState", Context.MODE_PRIVATE)
        prefsEditor = sharedPreferences.edit()

        // assign the screen components to variables that can be accessed from within other classes and methods.
        chpUsername = findViewById(R.id.chpUsername)
        chpName = findViewById(R.id.chpName)
        chpState = findViewById(R.id.chpState)
        edtDetails = findViewById(R.id.edtDetails)
        swtSwitch = findViewById(R.id.swtRPC)

        // From sharedPrefs, restore the Token, name and then remaining keys
        chpUsername.setText(sharedPreferences.getString("token","Discord Token"))
        chpName.setText(sharedPreferences.getString("keyName",Logic.v_TachiyomiSy))
        chpState.setText(sharedPreferences.getString("keyState", Logic.v_Manga))
        restoreFromLastState()
        // load the right chipIcon when restoring lastState
        Logic.restoreCorrectDataOnCreate(this)

        // restore the details entered last if MyService is running
        if (isServiceRunning(MyService::class.java)) {
            chpName.setText(MyService.setName)
            chpState.setText(MyService.setState)
            edtDetails.setText(MyService.setDetails)
            swtSwitch.isChecked = true
        }


        // Get user's discord token
        chpUsername.setOnCloseIconClickListener {
            // Start the LoginDiscord activity and return to MainActivity when it calls finish()
            val intent = Intent(this, LoginDiscord::class.java)
            startActivityForResult(intent, 1)
        }

        // switch between differnet presets
        chpName.setOnCloseIconClickListener {
            Logic.nameWasChanged(this)
            restoreFromLastState()
        }

        // inter-switch the name and state chips
        chpState.setOnCloseIconClickListener {
            Logic.stateWasChanged(this)
        }

        // Start or Stop RPC and save details to sharedPrefs on click
        swtSwitch.setOnClickListener {
            if ((chpUsername.text == "Discord Token") || (chpUsername.text == "")) {
                Toast.makeText(this, "Enter your discord token first!", Toast.LENGTH_SHORT).show()
                swtSwitch.isChecked = false
            }
            else if (chpUsername.text.matches(Regex("^(.+)\\.(.+)\\.(.+)$")))
            {
                if (swtSwitch.isChecked())
                    startService(Intent(this, MyService::class.java))
                else
                    stopService(Intent(this, MyService::class.java))
                saveToLastState()
            } else {
                Toast.makeText(this,"Recheck the entered discord token for typos!" , Toast.LENGTH_SHORT).show()
                swtSwitch.isChecked = false
            }
        }

        // Easter Egg!! - Long click paste btn in Activity Details to copy the details to clipboard
//        findViewById<ImageButton>(R.id.imgPasteDetails).setOnLongClickListener() {
//            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("Activity Details", edtDetails.text.toString())
//            clipboard.setPrimaryClip(clip!!)
//        }
    }

    // Copy text in edtDetails to the clipboard
    fun pasteDetails(view: View?) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipItem = clipboard.primaryClip!!.getItemAt(0)
        edtDetails.setText(clipItem.text!!.toString())
    }

    // Long press footer to disable battery optimization
    fun ignoreBatteryOptimization(view: View?) {
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
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun resetDiscordToken(view: View) {
        Toast.makeText(this, "Whoopsies~! I haven't implemented this feature yet, but you can expect it soon!", Toast.LENGTH_LONG).show()
    }

    // open this app's repo on GitHub in browser
    fun openGithubRepo(view: View?) {
         startActivity( Intent(
             Intent.ACTION_VIEW,
             Uri.parse("https://github.com/jeryjs/TachiSY_RPC")
         ) )
    }

    // restore all details from sharedPrefs
    private fun restoreFromLastState() {
        // set the saved edtDetails from sharedPrefs
        if (chpName.text == Logic.v_TachiyomiSy)
            edtDetails.setText(sharedPreferences.getString("keyDetails_tachi", ""))
        else if (chpName.text == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨")
            edtDetails.setText(sharedPreferences.getString("keyDetails_mangago", ""))
        else if ((chpName.text == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (chpName.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"))
            edtDetails.setText(sharedPreferences.getString("keyDetails_ln", ""))
        else if ((chpName.text == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (chpName.text == "𝐀𝐧𝐢𝐦𝐞"))
            edtDetails.setText(sharedPreferences.getString("keyDetails_anime", ""))
    }

    // save all details to sharedPrefs
    private fun saveToLastState() {
        // extract chpName, chpState and edtDetails to sharedPrefs
        prefsEditor.putString("keyName", chpName.text.toString()).commit()
        prefsEditor.putString("keyState", chpState.text.toString()).commit()

        if (chpName.text == Logic.v_TachiyomiSy)
            prefsEditor.putString("keyDetails_tachi", edtDetails.text.toString()).commit()
        else if (chpName.text == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨")
            prefsEditor.putString("keyDetails_mangago", edtDetails.text.toString()).commit()
        else if ((chpName.text == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (chpName.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"))
            prefsEditor.putString("keyDetails_ln", edtDetails.text.toString()).commit()
        else if ((chpName.text == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (chpName.text == "𝐀𝐧𝐢𝐦𝐞"))
            prefsEditor.putString("keyDetails_anime", edtDetails.text.toString()).commit()
        else if ((chpName.text == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (chpName.text == "𝐀𝐧𝐢𝐦𝐞"))
            prefsEditor.putString("keyDetails_anime", edtDetails.text.toString()).commit()
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