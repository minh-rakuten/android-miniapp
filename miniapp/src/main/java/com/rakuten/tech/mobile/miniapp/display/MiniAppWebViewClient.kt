package com.rakuten.tech.mobile.miniapp.display

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.*
import androidx.annotation.VisibleForTesting
import androidx.webkit.WebViewAssetLoader
import java.io.BufferedReader

internal class MiniAppWebViewClient(
    val context: Context,
    @VisibleForTesting internal val loader: WebViewAssetLoader,
    private val customDomain: String,
    private val customScheme: String
) : WebViewClient() {

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    @VisibleForTesting
    internal val bridgeJs = try {
        val inputStream = context.assets.open("bridge.js")
        inputStream.bufferedReader().use(BufferedReader::readText)
    } catch (e: Exception) {
        null
    }

    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? = loader.shouldInterceptRequest(request.url)

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url.toString().contains("oneapp")) {
//            val intent = Intent(Intent.ACTION_VIEW);
//            intent.data = Uri.parse(request?.url.toString());
//            context.startActivity(intent);

            val msg = request?.url?.getQueryParameter("message")
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, msg)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
            return true
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageStarted(webView: WebView, url: String?, favicon: Bitmap?) {
        super.onPageStarted(webView, url, favicon)
        webView.evaluateJavascript(bridgeJs) {}
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        if (request.url != null && request.url.toString().startsWith(customScheme)) {
            view.loadUrl(request.url.toString().replace(customScheme, customDomain))
            return
        }
        super.onReceivedError(view, request, error)
    }
}
