package com.rakuten.tech.mobile.miniapp.display

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.webkit.WebViewAssetLoader
import com.rakuten.tech.mobile.miniapp.MiniAppInfo
import com.rakuten.tech.mobile.miniapp.MiniAppScheme
import com.rakuten.tech.mobile.miniapp.navigator.MiniAppNavigator
import com.rakuten.tech.mobile.miniapp.js.MiniAppMessageBridge
import com.rakuten.tech.mobile.miniapp.navigator.ExternalResultHandler
import com.rakuten.tech.mobile.miniapp.permission.MiniAppCustomPermissionCache
import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

private const val SUB_DOMAIN_PATH = "miniapp"
private const val MINI_APP_INTERFACE = "MiniAppAndroid"

@SuppressLint("SetJavaScriptEnabled")
internal class MiniAppWebView(
    context: Context,
    val basePath: String,
    val miniAppInfo: MiniAppInfo,
    val miniAppMessageBridge: MiniAppMessageBridge,
    var miniAppNavigator: MiniAppNavigator?,
    val hostAppUserAgentInfo: String,
    val miniAppWebChromeClient: MiniAppWebChromeClient = MiniAppWebChromeClient(context, miniAppInfo.displayName),
    val miniAppCustomPermissionCache: MiniAppCustomPermissionCache
) : WebView(context), WebViewListener {

    constructor(
        context: Context,
        miniAppTitle: String,
        miniAppUrl: String,
        miniAppMessageBridge: MiniAppMessageBridge,
        miniAppNavigator: MiniAppNavigator?,
        hostAppUserAgentInfo: String,
        miniAppWebChromeClient: MiniAppWebChromeClient = MiniAppWebChromeClient(context, miniAppTitle),
        miniAppCustomPermissionCache: MiniAppCustomPermissionCache
    ) : this(
            context,
            "",
            MiniAppInfo.empty(),
            miniAppMessageBridge,
            miniAppNavigator,
            hostAppUserAgentInfo,
            miniAppWebChromeClient,
            miniAppCustomPermissionCache) {

        this.miniAppUrl = miniAppUrl
        this.miniAppTitle = miniAppTitle
        miniAppScheme = MiniAppScheme.schemeWithCustomUrl(miniAppUrl)
        miniAppId = "custom${Random.nextInt(0, Int.MAX_VALUE)}" // some id is needed to handle permissions
        commonInit()
    }

    private var miniAppScheme = MiniAppScheme.schemeWithAppId(miniAppInfo.id)
    private var miniAppId = miniAppInfo.id
    private var miniAppUrl: String? = null
    private var miniAppTitle = miniAppInfo.displayName

    @VisibleForTesting
    internal val externalResultHandler = ExternalResultHandler().apply {
        onResultChanged = { externalUrl ->
            if (externalUrl.startsWith(miniAppScheme.miniAppCustomScheme))
                loadUrl(externalUrl.replace(miniAppScheme.miniAppCustomScheme, miniAppScheme.miniAppCustomDomain))
            else if (externalUrl.startsWith(miniAppScheme.miniAppCustomDomain))
                loadUrl(externalUrl)
        }
    }

    init {
        commonInit()
    }

    private fun commonInit() {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )

        settings.javaScriptEnabled = true
        addJavascriptInterface(miniAppMessageBridge, MINI_APP_INTERFACE)

        miniAppMessageBridge.init(
            activity = context as Activity,
            webViewListener = this,
            customPermissionCache = miniAppCustomPermissionCache,
            miniAppId = miniAppId
        )

        settings.allowUniversalAccessFromFileURLs = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false

        if (hostAppUserAgentInfo.isNotEmpty())
            settings.userAgentString =
                String.format("%s %s", settings.userAgentString, hostAppUserAgentInfo)

        setupMiniAppNavigator()
        if (miniAppId.isNotBlank() || miniAppUrl != null) {
            webViewClient = MiniAppWebViewClient(
                context,
                if (miniAppUrl == null) getWebViewAssetLoader() else null,
                miniAppNavigator!!,
                externalResultHandler,
                miniAppScheme)
            webChromeClient = miniAppWebChromeClient

            loadUrl(getLoadUrl())
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onResume()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        onPause()
        if (!(context as Activity).isDestroyed) {
            miniAppWebChromeClient.onWebViewDetach()
            miniAppMessageBridge.onWebViewDetach()
        }
    }

    fun destroyView() {
        stopLoading()
        webViewClient = null
        destroy()
    }

    @VisibleForTesting
    internal fun setupMiniAppNavigator() {
        if (miniAppNavigator == null) {
            miniAppNavigator = object : MiniAppNavigator {
                override fun openExternalUrl(url: String, externalResultHandler: ExternalResultHandler) {
                    val customTabsIntent = CustomTabsIntent.Builder()
                        .setShowTitle(true)
                        .build()
                    customTabsIntent.launchUrl(context, url.toUri())
                }
            }
        }
    }

    override fun runSuccessCallback(callbackId: String, value: String) {
        post {
            evaluateJavascript(
                "MiniAppBridge.execSuccessCallback(`$callbackId`, `${value.replace("`", "\\`")}`)"
            ) {}
        }
    }

    override fun runErrorCallback(callbackId: String, errorMessage: String) {
        post {
            evaluateJavascript(
                "MiniAppBridge.execErrorCallback(\"$callbackId\", \"$errorMessage\")"
            ) {}
        }
    }

    private fun getWebViewAssetLoader() = WebViewAssetLoader.Builder()
        .setDomain(miniAppScheme.miniAppDomain)
        .addPathHandler(
            "/$SUB_DOMAIN_PATH/", WebViewAssetLoader.InternalStoragePathHandler(
                context,
                File(basePath)
            )
        )
        .addPathHandler(
            "/", WebViewAssetLoader.InternalStoragePathHandler(
                context,
                File(basePath)
            )
        )
        .build()

    @VisibleForTesting
    internal fun getLoadUrl(): String {
        return if (miniAppUrl != null) {
            "$miniAppUrl/index.html"
        } else {
            "${miniAppScheme.miniAppCustomDomain}$SUB_DOMAIN_PATH/index.html"
        }
    }
}

internal interface WebViewListener {
    fun runSuccessCallback(callbackId: String, value: String)
    fun runErrorCallback(callbackId: String, errorMessage: String)
}
