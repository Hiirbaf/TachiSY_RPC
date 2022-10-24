@file:Suppress("UsePropertyAccessSyntax")

package com.jery.tachisy_rpc.utils

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.jery.tachisy_rpc.MainActivity
import com.jery.tachisy_rpc.MyService
import com.jery.tachisy_rpc.R

object Logic {
    //    var token       : String = BuildConfig.dc_token_jery
    var type = 0
    lateinit var largeImage: String
    lateinit var smallImage: String
    private val name = MainActivity.name
    private val state = MainActivity.state

    fun nameWasChanged(activity: Activity) {
        if (name.getText().toString() == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
            name.text = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
            state.text = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
        }
        else if ((name.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (name.getText().toString() == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯")) {
            name.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
            state.text = "𝐀𝐧𝐢𝐦𝐞"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        }
        else if ((name.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (name.getText().toString() == "𝐀𝐧𝐢𝐦𝐞")) {
            name.text = "𝐌𝐚𝐧𝐠𝐚𝐠𝐨"
            state.text = "𝔐𝔞𝔫𝔤𝔞"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if (name.getText().toString() == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨") {
            name.text = "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘"
            state.text = "𝔐𝔞𝔫𝔤𝔞"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
    }

    fun stateWasChanged(activity: Activity) {
        if (state.getText().toString() == "𝔐𝔞𝔫𝔤𝔞") {
            state.text = "𝔐𝔞𝔫𝔥𝔴𝔞"
        } else if (state.getText().toString() == "𝔐𝔞𝔫𝔥𝔴𝔞") {
            state.text = "𝔐𝔞𝔫𝔤𝔞"
        }
        else if (state.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") {
            name.text = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
            state.text = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
        } else if (state.getText().toString() == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯") {
            name.text = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
            state.text = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
        }
        else if (state.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") {
            name.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
            state.text = "𝐀𝐧𝐢𝐦𝐞"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        } else if (state.getText().toString() == "𝐀𝐧𝐢𝐦𝐞") {
            name.text = "𝐀𝐧𝐢𝐦𝐞"
            state.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
        }
    }

    fun loadRPCData(activity: MyService) {
        if (name.text.toString() == "𝐀𝐧𝐢𝐦𝐞") {
            type = 3
            Toast.makeText(activity, "watching " + name.text.toString() + "\n" + state.text + ":「" + MainActivity.details.text + "」", Toast.LENGTH_SHORT).show()
        }else {
            type = 0
            Toast.makeText(activity, "playing " + name.text.toString() + "\n" + state.text + ":「" + MainActivity.details.text + "」", Toast.LENGTH_SHORT).show()
        }


        if (name.getText().toString() == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
            largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
        else if ((name.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (name.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯")) {
            largeImage = "attachments/949382602073210921/1031952390636707930/moon-reader-pro.png"
            smallImage = "attachments/949382602073210921/994460304626962484/Reading-Icon.png"
        }
        else if ((name.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (name.getText().toString() == "𝐀𝐧𝐢𝐦𝐞"))
        {
            largeImage = "attachments/949382602073210921/1002240570091122798/Aniyomi.png"
            smallImage = "attachments/949382602073210921/1002240620569567404/watching-icon.png"
        }
        else if (name.getText().toString() == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨") {
            largeImage = "attachments/949382602073210921/1034172617311133847/mangago.jpg"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
    }

    fun restoreCorrectDataOnCreate(activity: Activity) {
        // load the right chipIcon when restoring lastState
        if (name.text == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if (name.getText().toString() == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨") {
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if ((name.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (name.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯")) {
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
        }
        else if ((name.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (name.getText().toString() == "𝐀𝐧𝐢𝐦𝐞")) {
            name.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            state.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        }
    }
}