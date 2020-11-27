package com.rakuten.tech.mobile.miniapp.display

import android.content.Context
import com.rakuten.tech.mobile.miniapp.MiniAppInfo
import com.rakuten.tech.mobile.miniapp.MiniAppScheme
import com.rakuten.tech.mobile.miniapp.js.MiniAppMessageBridge
import com.rakuten.tech.mobile.miniapp.navigator.MiniAppNavigator
import com.rakuten.tech.mobile.miniapp.permission.MiniAppCustomPermissionCache
import kotlin.random.Random

internal class MiniAppHttpWebView(
    context: Context,
    miniAppTitle: String,
    val miniAppUrl: String,
    miniAppMessageBridge: MiniAppMessageBridge,
    miniAppNavigator: MiniAppNavigator?,
    hostAppUserAgentInfo: String,
    miniAppWebChromeClient: MiniAppWebChromeClient = MiniAppWebChromeClient(context, miniAppTitle),
    miniAppCustomPermissionCache: MiniAppCustomPermissionCache
): MiniAppWebView(
    context,
    "",
    MiniAppInfo.empty(),
    miniAppMessageBridge,
    miniAppNavigator,
    hostAppUserAgentInfo,
    miniAppWebChromeClient,
    miniAppCustomPermissionCache
) {
    init {
        miniAppScheme = MiniAppScheme.schemeWithCustomUrl(miniAppUrl)
        miniAppId = "custom${Random.nextInt(0, Int.MAX_VALUE)}" // some id is needed to handle permissions
        commonInit()
    }

    override fun getMiniAppWebViewClient(): MiniAppWebViewClient = MiniAppWebViewClient(
        context,
        null,
        miniAppNavigator!!,
        externalResultHandler,
        miniAppScheme
    )

    override fun getLoadUrl(): String = miniAppUrl
}
