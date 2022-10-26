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
    private val chpName = MainActivity.chpName
    private val chpState = MainActivity.chpState

    fun nameWasChanged(activity: Activity) {
        Toast.makeText(activity, "clicked!", Toast.LENGTH_SHORT).show()
        if (chpName.getText().toString() == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
            chpName.text = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
            chpState.text = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
        }
        else if ((chpName.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (chpName.getText().toString() == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯")) {
            chpName.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
            chpState.text = "𝐀𝐧𝐢𝐦𝐞"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        }
        else if ((chpName.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (chpName.getText().toString() == "𝐀𝐧𝐢𝐦𝐞")) {
            chpName.text = "𝐌𝐚𝐧𝐠𝐚𝐠𝐨"
            chpState.text = "𝔐𝔞𝔫𝔤𝔞"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if (chpName.getText().toString() == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨") {
            chpName.text = "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘"
            chpState.text = "𝔐𝔞𝔫𝔤𝔞"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
    }

    fun stateWasChanged(activity: Activity) {
        if (chpState.getText().toString() == "𝔐𝔞𝔫𝔤𝔞") {
            chpState.text = "𝔐𝔞𝔫𝔥𝔴𝔞"
        } else if (chpState.getText().toString() == "𝔐𝔞𝔫𝔥𝔴𝔞") {
            chpState.text = "𝔐𝔞𝔫𝔤𝔞"
        }
        else if (chpState.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") {
            chpName.text = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
            chpState.text = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
        } else if (chpState.getText().toString() == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯") {
            chpName.text = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
            chpState.text = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
        }
        else if (chpState.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") {
            chpName.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
            chpState.text = "𝐀𝐧𝐢𝐦𝐞"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        } else if (chpState.getText().toString() == "𝐀𝐧𝐢𝐦𝐞") {
            chpName.text = "𝐀𝐧𝐢𝐦𝐞"
            chpState.text = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
        }
    }

    fun restoreCorrectDataOnCreate(activity: Activity) {
        // load the right chipIcon when restoring lastState
        if (chpName.text == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if (chpName.getText().toString() == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨") {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if ((chpName.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (chpName.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯")) {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
        }
        else if ((chpName.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (chpName.getText().toString() == "𝐀𝐧𝐢𝐦𝐞")) {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        }
    }

    fun loadRPCData(activity: MyService) {
        if (chpName.text.toString() == "𝐀𝐧𝐢𝐦𝐞") {
            type = 3
            Toast.makeText(activity, "watching " + chpName.text.toString() + "\n" + chpState.text + ":「" + MainActivity.edtDetails.text + "」", Toast.LENGTH_SHORT).show()
        }else {
            type = 0
            Toast.makeText(activity, "playing " + chpName.text.toString() + "\n" + chpState.text + ":「" + MainActivity.edtDetails.text + "」", Toast.LENGTH_SHORT).show()
        }

        if (chpName.getText().toString() == "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘") {
            largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
        else if (chpName.getText().toString() == "𝐌𝐚𝐧𝐠𝐚𝐠𝐨") {
            largeImage = "attachments/949382602073210921/1034172617311133847/mangago.jpg"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
        else if ((chpName.getText().toString() == "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥") || (chpName.text == "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯")) {
            largeImage = "attachments/949382602073210921/1031952390636707930/moon-reader-pro.png"
            smallImage = "attachments/949382602073210921/994460304626962484/Reading-Icon.png"
        }
        else if ((chpName.getText().toString() == "𝙰𝚗𝚒𝚢𝚘𝚖𝚒") || (chpName.getText().toString() == "𝐀𝐧𝐢𝐦𝐞"))
        {
            largeImage = "attachments/949382602073210921/1002240570091122798/Aniyomi.png"
            smallImage = "attachments/949382602073210921/1002240620569567404/watching-icon.png"
        }
    }
}