@file:Suppress("UsePropertyAccessSyntax", "UNUSED_PARAMETER", "SetTextI18n")

package com.jery.tachisy_rpc.utils

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.content.res.AppCompatResources
import com.jery.tachisy_rpc.MainActivity
import com.jery.tachisy_rpc.MyService
import com.jery.tachisy_rpc.R

@SuppressLint("StaticFieldLeak")
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

    lateinit var largeImage: String
    lateinit var smallImage: String
    private val chpName = MainActivity.chpName
    private val chpState = MainActivity.chpState
    private val chpType = MainActivity.chpType

    fun nameWasChanged(activity: Activity) {
        if (chpName.getText().toString() == v_TachiyomiSy) {
            chpName.text = v_LightNovel
            chpState.text = v_MoonReader
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
            chpType.text = "Vol"
        }
        else if ((chpName.getText().toString() == v_LightNovel) || (chpName.getText().toString() == v_MoonReader)) {
            chpName.text = v_Aniyomi
            chpState.text = v_Anime
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
            chpType.text = "Ep"
        }
        else if ((chpName.getText().toString() == v_Aniyomi) || (chpName.getText().toString() == v_Anime)) {
            chpName.text = v_Mangago
            chpState.text = v_Manga
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
            chpType.text = "Vol"
        }
        else if (chpName.getText().toString() == v_Mangago) {
            chpName.text = v_Webtoon
            chpState.text = v_Reading
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_webtoon)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
            chpType.text = "Ep"
        }
        else if (chpName.getText().toString() == v_Webtoon) {
            chpName.text = v_TachiyomiSy
            chpState.text = v_Manga
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
            chpType.text = "Vol"
        }
        // This is a workaround for this weird bug I can't fix...
        // When close app from recent apps and reopened, chpName can't be changed anymore.
        // I believe it might be cuz the fancy names are not saved in the exact same format in sharedPrefs
        else {
            println("Wasn't able to switch template properly!!\nSwitching back to fallback data.")
            chpName.text = v_TachiyomiSy
            chpState.text = v_Manga
            chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
            chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
            chpType.text = "Vol"
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

    // restore all details from sharedPrefs
    fun restoreFromLastState() {
        // set the saved edtDetails from sharedPrefs
        if (chpName.text == v_TachiyomiSy)
            MainActivity.edtDetails.setText(MainActivity.sharedPreferences.getString("keyDetails_tachi", ""))
        else if ((chpName.text == v_LightNovel) || (chpName.text == v_MoonReader))
            MainActivity.edtDetails.setText(MainActivity.sharedPreferences.getString("keyDetails_ln", ""))
        else if ((chpName.text == v_Aniyomi) || (chpName.text == v_Anime))
            MainActivity.edtDetails.setText(MainActivity.sharedPreferences.getString("keyDetails_anime", ""))
        else if (chpName.text == v_Mangago)
            MainActivity.edtDetails.setText(MainActivity.sharedPreferences.getString("keyDetails_mangago", ""))
        else if (chpName.text == v_Webtoon)
            MainActivity.edtDetails.setText(MainActivity.sharedPreferences.getString("keyDetails_webtoon", ""))
    }

    // save all details to sharedPrefs
    fun saveToLastState() {
        // extract chpName, chpState and edtDetails to sharedPrefs
        MainActivity.prefsEditor.putString("keyName", MainActivity.chpName.text.toString()).commit()
        MainActivity.prefsEditor.putString("keyState", MainActivity.chpState.text.toString()).commit()

        if (MainActivity.chpName.text == v_TachiyomiSy)
            MainActivity.prefsEditor.putString("keyDetails_tachi", MainActivity.edtDetails.text.toString()).commit()
        else if ((MainActivity.chpName.text == v_LightNovel) || (MainActivity.chpName.text == v_MoonReader))
            MainActivity.prefsEditor.putString("keyDetails_ln", MainActivity.edtDetails.text.toString()).commit()
        else if ((MainActivity.chpName.text == v_Aniyomi) || (MainActivity.chpName.text == v_Anime))
            MainActivity.prefsEditor.putString("keyDetails_anime", MainActivity.edtDetails.text.toString()).commit()
        else if (MainActivity.chpName.text == v_Mangago)
            MainActivity.prefsEditor.putString("keyDetails_mangago", MainActivity.edtDetails.text.toString()).commit()
        else if (MainActivity.chpName.text == v_Webtoon)
            MainActivity.prefsEditor.putString("keyDetails_webtoon", MainActivity.edtDetails.text.toString()).commit()
        // save the current type and ch to sharedPrefs
        MainActivity.prefsEditor.putString("keyType", MainActivity.chpType.text.toString()).commit()
        MainActivity.prefsEditor.putInt("keyCh", MainActivity.numChapter.getValue()).commit()
    }

    fun loadRPCData(activity: MyService) {
        if (chpName.getText().toString() == v_TachiyomiSy) {
            largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
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
        else if (chpName.getText().toString() == v_Mangago) {
            largeImage = "attachments/949382602073210921/1034172617311133847/mangago.jpg"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
        else if (chpName.getText().toString() == v_Webtoon)
        {
            largeImage = "attachments/1035528103017066618/1035529154600382515/Webtoon.png"
            smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
        }
    }
}