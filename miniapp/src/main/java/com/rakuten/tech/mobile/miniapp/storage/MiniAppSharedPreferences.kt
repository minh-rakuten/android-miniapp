package com.rakuten.tech.mobile.miniapp.storage

import android.content.Context
import android.content.SharedPreferences

object MiniAppSharedPreferences {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(
            "com.rakuten.tech.mobile.miniapp.storage", Context.MODE_PRIVATE
        )
    }

    fun setAppExisted(appId: String, versionId: String, value: Boolean) =
        prefs.edit().putBoolean(appId+versionId, value).apply()

    fun isAppExisted(appId: String, versionId: String, default: Boolean = false):Boolean =
        prefs.getBoolean(appId+versionId, default)
}
