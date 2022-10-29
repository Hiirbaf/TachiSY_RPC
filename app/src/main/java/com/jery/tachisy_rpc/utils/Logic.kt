@file:Suppress("UsePropertyAccessSyntax")

package com.jery.tachisy_rpc.utils

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.jery.tachisy_rpc.MainActivity
import com.jery.tachisy_rpc.MyService
import com.jery.tachisy_rpc.R

@Suppress("MemberVisibilityCanBePrivate")
object Logic {

    const val v_TachiyomiSy = "𝐓𝐚𝐜𝐡𝐢𝐲𝐨𝐦𝐢𝐒𝐘"
    const val v_Manga = "𝔐𝔞𝔫𝔤𝔞"
    const val v_Manhwa = "𝔐𝔞𝔫𝔥𝔴𝔞"
    const val v_LightNovel = "𝐋𝐢𝐠𝐡𝐭 𝐍𝐨𝐯𝐞𝐥"
    const val v_MoonReader = "𝔐𝔬𝔬𝔫+ ℜ𝔢𝔞𝔡𝔢𝔯"
    const val v_Aniyomi = "𝙰𝚗𝚒𝚢𝚘𝚖𝚒"
    const val v_Anime = "𝐀𝐧𝐢𝐦𝐞"
    const val v_Mangago = "𝐌𝐚𝐧𝐠𝐚𝐠𝐨"
    const val v_Webtoon = "𝐖𝐞𝐛𝐭𝐨𝐨𝐧"
    const val v_Reading = "ℜ𝔢𝔞𝔡𝔦𝔫𝔤"


    //    var token       : String = BuildConfig.dc_token_jery
    var type = 0
    lateinit var largeImage: String
    lateinit var smallImage: String
    private val chpName = MainActivity.chpName
    private val chpState = MainActivity.chpState

    fun nameWasChanged(activity: Activity) {
        if (chpName.getText().toString() == v_TachiyomiSy) {
            chpName.text = v_LightNovel
            chpState.text = v_MoonReader
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
        }
        else if ((chpName.getText().toString() == v_LightNovel) || (chpName.getText().toString() == v_MoonReader)) {
            chpName.text = v_Aniyomi
            chpState.text = v_Anime
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        }
        else if ((chpName.getText().toString() == v_Aniyomi) || (chpName.getText().toString() == v_Anime)) {
            chpName.text = v_Mangago
            chpState.text = v_Manga
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if (chpName.getText().toString() == v_Mangago) {
            chpName.text = v_Webtoon
            chpState.text = v_Reading
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_webtoon)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        // This is a workaround for this weird bug I can't fix...
        // When close app from recent apps and reopened, chpName can't be changed anymore.
        // I believe it might be cuz the fancy names are not saved in the exact same format in sharedPrefs
        else {
            println("Wasn't able to switch template properly!! Switching back to fallback data.")
            chpName.text = v_TachiyomiSy
            chpState.text = v_Manga
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
    }

    fun stateWasChanged(activity: Activity) {
        if (chpState.getText().toString() == v_Manga) {
            chpState.text = v_Manhwa
        } else if (chpState.getText().toString() == v_Manhwa) {
            chpState.text = v_Manga
        }
        else if (chpState.getText().toString() == v_LightNovel) {
            chpName.text = v_LightNovel
            chpState.text = v_MoonReader
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
        } else if (chpState.getText().toString() == v_MoonReader) {
            chpName.text = v_MoonReader
            chpState.text = v_LightNovel
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
        }
        else if (chpState.getText().toString() == v_Aniyomi) {
            chpName.text = v_Aniyomi
            chpState.text = v_Anime
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        } else if (chpState.getText().toString() == v_Anime) {
            chpName.text = v_Anime
            chpState.text = v_Aniyomi
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
        }
    }

    fun restoreCorrectDataOnCreate(activity: Activity) {
        // load the right chipIcon when restoring lastState
        if (chpName.getText().toString() == v_TachiyomiSy) {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if (chpName.getText().toString() == v_Mangago) {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
        else if ((chpName.getText().toString() == v_LightNovel) || (chpName.text == v_MoonReader)) {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
        }
        else if ((chpName.getText().toString() == v_Aniyomi) || (chpName.getText().toString() == v_Anime)) {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
        }
        else if (chpName.getText().toString() == v_Webtoon) {
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_webtoon)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
        }
    }

    fun loadRPCData(activity: MyService) {
        if (chpName.text.toString() == v_Anime) {
            type = 3
            Toast.makeText(activity, "watching " + chpName.text.toString() + "\n" + chpState.text + ":「" + MainActivity.edtDetails.text + "」", Toast.LENGTH_SHORT).show()
        }else {
            type = 0
            Toast.makeText(activity, "playing " + chpName.text.toString() + "\n" + chpState.text + ":「" + MainActivity.edtDetails.text + "」", Toast.LENGTH_SHORT).show()
        }

        if (chpName.getText().toString() == v_TachiyomiSy) {
            largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
        else if (chpName.getText().toString() == v_Mangago) {
            largeImage = "attachments/949382602073210921/1034172617311133847/mangago.jpg"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
        else if ((chpName.getText().toString() == v_LightNovel) || (chpName.text == v_MoonReader)) {
            largeImage = "attachments/949382602073210921/1031952390636707930/moon-reader-pro.png"
            smallImage = "attachments/949382602073210921/994460304626962484/Reading-Icon.png"
        }
        else if ((chpName.getText().toString() == v_Aniyomi) || (chpName.getText().toString() == v_Anime))
        {
            largeImage = "attachments/949382602073210921/1002240570091122798/Aniyomi.png"
            smallImage = "attachments/949382602073210921/1002240620569567404/watching-icon.png"
        }
        else if ((chpName.getText().toString() == v_Aniyomi) || (chpName.getText().toString() == v_Webtoon))
        {
            largeImage = "attachments/1035528103017066618/1035529154600382515/Webtoon.png"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
    }
}