@file:Suppress("UseSwitchCompatOrMaterialCode", "UnusedImport", "UNUSED_VARIABLE", "SetTextI18n",
    "UsePropertyAccessSyntax", "SpellCheckingInspection", "StaticFieldLeak",
    "LiftReturnOrAssignment", "DEPRECATION", "BatteryLife", "ApplySharedPref", "UNUSED_ANONYMOUS_PARAMETER",
    "UNUSED_PARAMETER", "CommitPrefEdits", "MissingInflatedId"
)

package com.jery.tachisy_rpc

import android.app.ActivityManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.jery.tachisy_rpc.utils.Logic


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var chpUsername   : Chip
        lateinit var chpName       : Chip
        lateinit var chpState      : Chip
        lateinit var edtDetails    : EditText
        lateinit var numType       : NumberPicker
        lateinit var numChapter    : NumberPicker
        lateinit var swtSwitch     : Switch
        var arrayOfTypes = arrayOf("Vol", "Ch", "Ep", "")

        lateinit var sharedPreferences : SharedPreferences
        lateinit var prefsEditor: SharedPreferences.Editor
    }

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
        numType = findViewById(R.id.numType)
        numChapter = findViewById(R.id.numChapter)
        swtSwitch = findViewById(R.id.swtRPC)

        // From sharedPrefs, restore the Token, name and then remaining keys
        chpUsername.setText(sharedPreferences.getString("token","Discord Token"))
        chpName.setText(sharedPreferences.getString("keyName",Logic.v_TachiyomiSy))
        chpState.setText(sharedPreferences.getString("keyState", Logic.v_Manga))
        // set the saved chpType from sharedPrefs
        numChapter.value = sharedPreferences.getInt("keyCh", 0)
        // load the correct states
        Logic.restoreFromLastState()
        // load the right chipIcon when restoring lastState
        Logic.restoreCorrectDataOnCreate(this)

        // restore the details entered last if MyService is running
        if (isServiceRunning(MyService::class.java)) {
            chpName.setText(MyService.setName)
            chpState.setText(MyService.setState)
            edtDetails.setText(MyService.setDetails)
            numType.setValue(MyService.setType)
            numChapter.setValue(MyService.setCh)
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
            Logic.restoreFromLastState()
        }

        // inter-switch the name and state chips
        chpState.setOnCloseIconClickListener {
            Logic.stateWasChanged(this)
        }

        // Setup numType's options and restore last state
        numType.minValue = 0
        numType.maxValue = arrayOfTypes.size -1
        numType.wrapSelectorWheel = false
        numType.displayedValues = arrayOfTypes
        numType.setOnValueChangedListener { numType, oldVal, newVal -> var chType = arrayOfTypes[numType.value] }
        numType.value = sharedPreferences.getInt("keyType", 0)

        // Setup numChapter's min and max and restore last state.
        numChapter.minValue = 0
        numChapter.maxValue = 9999
        numChapter.wrapSelectorWheel = false
        numChapter.value = sharedPreferences.getInt("keyCh", 0)

        // Start or Stop RPC and save details to sharedPrefs on click
        swtSwitch.setOnClickListener {
            if ((chpUsername.text == "Discord Token") || (chpUsername.text == "")) {
                Toast.makeText(this, "Enter your discord token first!", Toast.LENGTH_SHORT).show()
                swtSwitch.isChecked = false
            }
            else if (chpUsername.text.matches(Regex("^.+$")))   // need to change this lol
            {
                if (swtSwitch.isChecked())
                    startService(Intent(this, MyService::class.java))
                else
                    stopService(Intent(this, MyService::class.java))
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