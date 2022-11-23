@file:Suppress("UsePropertyAccessSyntax", "UNUSED_PARAMETER", "SetTextI18n", "StaticFieldLeak", "MemberVisibilityCanBePrivate")

package com.jery.tachisy_rpc.utils

import android.app.Activity
import androidx.appcompat.content.res.AppCompatResources
import com.jery.tachisy_rpc.MainActivity
import com.jery.tachisy_rpc.MyService
import com.jery.tachisy_rpc.R

object Logic {
    // set access vars for the arrays
    val Preset_Names: Array<String> = MainActivity.Main_Context.getResources().getStringArray(R.array.Preset_names)
    val Preset_States: Array<String> = MainActivity.Main_Context.getResources().getStringArray(R.array.Preset_states)
    // setup preset names
    val v_TachiyomiSy = Preset_Names[0]
    val v_LightNovel = Preset_Names[1]
    val v_Mangago = Preset_Names[2]
    val v_Anime = Preset_Names[3]
    val v_Webtoon = Preset_Names[4]
    // setup preset states
    val v_Manga = Preset_States[0]
    val v_Manhwa = Preset_States[1]
    val v_MoonReader = Preset_States[2]
    val v_Aniyomi = Preset_States[3]
    val v_Reading = Preset_States[4]

    private val chpName = MainActivity.chpName
    private val chpState = MainActivity.chpState
    lateinit var largeImage: String
    lateinit var smallImage: String

    /**
     * Set the correct chipIcons when MainActivity's onCreate is triggered
     * @param activity The activity that calls this function
     */
    fun restoreCorrectDataOnCreate(activity: Activity) {
        // load the right chipIcon when restoring lastState
        when {
            chpName.getText().toString() == v_TachiyomiSy -> {
                chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_tachiyomi)
                chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
            }
            chpName.getText().toString() == v_Mangago -> {
                chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_mangago)
                chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
            }
            chpName.getText().toString() == v_LightNovel || chpName.text == v_MoonReader -> {
                chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_moon_reader)
                chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading_ln)
            }
            chpName.getText().toString() == v_Aniyomi || chpName.getText().toString() == v_Anime -> {
                chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_aniyomi)
                chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_watching)
            }
            chpName.getText().toString() == v_Webtoon -> {
                chpName.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_webtoon)
                chpState.chipIcon = AppCompatResources.getDrawable(activity, R.drawable.ic_reading)
            }
        }
    }

    /**
     * save all details to sharedPrefs
     * @param
     */
    fun saveToLastState() {
        // extract chpName, chpState and edtDetails to sharedPrefs
        MainActivity.prefsEditor.putString("keyName", MainActivity.chpName.text.toString()).commit()
        MainActivity.prefsEditor.putString("keyState", MainActivity.chpState.text.toString()).commit()

        when (MainActivity.chpName.text) {
            v_TachiyomiSy -> MainActivity.prefsEditor.putString("keyDetails_tachi", MainActivity.edtDetails.text.toString()).commit()
            v_LightNovel, v_MoonReader -> MainActivity.prefsEditor.putString("keyDetails_ln", MainActivity.edtDetails.text.toString()).commit()
            v_Aniyomi, v_Anime -> MainActivity.prefsEditor.putString("keyDetails_anime", MainActivity.edtDetails.text.toString()).commit()
            v_Mangago -> MainActivity.prefsEditor.putString("keyDetails_mangago", MainActivity.edtDetails.text.toString()).commit()
            v_Webtoon -> MainActivity.prefsEditor.putString("keyDetails_webtoon", MainActivity.edtDetails.text.toString()).commit()
        }
        // save the current type and ch to sharedPrefs
        MainActivity.prefsEditor.putInt("keyType", MainActivity.numType.getValue()).commit()
        MainActivity.prefsEditor.putInt("keyCh", MainActivity.numChapter.getValue()).commit()
    }

    /**
     * Set the largeImage and smallImage urls that are passed are passed on to the rich presence
     * @param activity The activity that calls this function
     */
    fun loadRPCData(activity: MyService) {
        when {
            chpName.getText().toString() == v_TachiyomiSy -> {
                largeImage = "attachments/961577469427736636/971135180322529310/unknown.png"
                smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
            chpName.getText().toString() == v_LightNovel || chpName.text == v_MoonReader -> {
                largeImage = "attachments/949382602073210921/1031952390636707930/moon-reader-pro.png"
                smallImage = "attachments/949382602073210921/994460304626962484/Reading-Icon.png"
            }
            chpName.getText().toString() == v_Aniyomi || chpName.getText().toString() == v_Anime -> {
                largeImage = "attachments/949382602073210921/1002240570091122798/Aniyomi.png"
                smallImage = "attachments/949382602073210921/1002240620569567404/watching-icon.png"
            }
            chpName.getText().toString() == v_Mangago -> {
                largeImage = "attachments/949382602073210921/1034172617311133847/mangago.jpg"
                smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
            chpName.getText().toString() == v_Webtoon -> {
                largeImage = "attachments/1035528103017066618/1035529154600382515/Webtoon.png"
                smallImage = "attachments/949382602073210921/1001372717783711814/reading-icon.png"
            }
        }
    }
}