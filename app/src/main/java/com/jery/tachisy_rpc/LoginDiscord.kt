@file:Suppress("OVERRIDE_DEPRECATION", "KotlinConstantConditions", "StaticFieldLeak", "SetJavaScriptEnabled",
    "UNUSED_ANONYMOUS_PARAMETER", "UNUSED_PARAMETER"
)

package com.jery.tachisy_rpc

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


private var loginToken: String? = null

var webView: WebView? = null

class LoginDiscord : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_discord)
        val webView2 = findViewById<WebView>(R.id.mainWebView)
        webView = webView2
        webView2.settings.javaScriptEnabled = true
//        webView!!.settings.setAppCacheEnabled(true)
        webView!!.settings.databaseEnabled = true
        webView!!.settings.domStorageEnabled = true
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(webView: WebView, str: String): Boolean {
                Log.d("Web", "Attempt to enter $str")
                webView.stopLoading()
                if (!str.endsWith("/app")) {
                    return false
                }
                webView.visibility = View.GONE
                this@LoginDiscord.extractToken()
                this@LoginDiscord.login(webView)
                return false
            }
        }
        this@LoginDiscord.login(webView)
    }

    fun login(view: View?) {
        if (webView!!.visibility == View.VISIBLE) {
            webView!!.stopLoading()
            webView!!.visibility = View.GONE
        }
        else if (loginToken != null) {
            Toast.makeText(this, "Logged in with token:\n$loginToken", Toast.LENGTH_SHORT).show()
            MainActivity.chpUsername.text = loginToken
            MainActivity.prefsEditor.putString("token", loginToken).commit()
            finish()
        }
        else {
            webView!!.visibility = View.VISIBLE
            webView!!.loadUrl("https://discord.com/login")
        }
    }

    private fun extractToken(): Boolean {
        var readLine: String
        return try {
            val listFiles = File(
                filesDir.parentFile,
                "app_webview/Default/Local Storage/leveldb"
            ).listFiles { file, str ->
                str.endsWith(".log")
            }
            if (listFiles!!.isEmpty()) {
                return false
            }
            val bufferedReader = BufferedReader(FileReader(listFiles[0]))
            do {
                readLine = bufferedReader.readLine()
                if (readLine == null) {
                    break
                }
            } while (!readLine.contains("token"))
            val substring = readLine.substring(readLine.indexOf("token") + 5)
            val substring2 = substring.substring(substring.indexOf("\"") + 1)
            loginToken = substring2.substring(0, substring2.indexOf("\""))
            true
        } catch (th: Throwable) {
            false
        }
    }
}