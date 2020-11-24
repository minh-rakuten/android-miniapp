package com.rakuten.tech.mobile.miniapp

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

internal class MiniAppScheme private constructor(miniAppId: String) {

    val miniAppDomain = "mscheme.$miniAppId"
    val miniAppCustomScheme = "$miniAppDomain://"
    val miniAppCustomDomain = "https://$miniAppDomain/"
    var miniAppUrl: String? = null
        private set

    companion object {
        fun schemeWithAppId(miniAppId: String) = MiniAppScheme(miniAppId)

        fun schemeWithCustomUrl(miniAppUrl: String): MiniAppScheme {
            val scheme = MiniAppScheme("")
            scheme.miniAppUrl = miniAppUrl
            return scheme
        }
    }

    fun isMiniAppUrl(url: String): Boolean {
        return if (miniAppUrl?.isNotEmpty() == true) {
            val miniAppUri = miniAppUrl!!.toUri()
            if (miniAppUri.host?.isNotEmpty() == true) {
                miniAppUri.host.equals(url.toUri().host)
            } else {
                false
            }
        } else {
            url.startsWith(miniAppCustomDomain) || url.startsWith(miniAppCustomScheme)
        }
    }

    internal fun openPhoneDialer(context: Context, url: String) = Intent(Intent.ACTION_DIAL).let {
        it.data = url.toUri()
        context.startActivity(it)
    }
}
