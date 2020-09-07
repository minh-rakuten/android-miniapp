package com.rakuten.tech.mobile.testapp.ui.display

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.rakuten.tech.mobile.miniapp.navigator.MiniAppExternalUrlLoader
import com.rakuten.tech.mobile.testapp.ui.base.BaseActivity
import com.rakuten.tech.mobile.testapp.ui.component.SampleExternalWebView
import com.rakuten.tech.mobile.testapp.ui.component.SampleWebViewClient

class WebViewActivity: BaseActivity() {
    private lateinit var sampleExternalWebView: SampleExternalWebView

    companion object {
        val loadUrlTag = "load_url_tag"
        val miniAppIdTag = "miniapp_id_tag"

        fun startForResult(activity: Activity, url: String, appId: String, externalWebViewReqCode: Int) {
            val intent = Intent(activity, WebViewActivity::class.java).apply {
                putExtra(loadUrlTag, url)
                putExtra(miniAppIdTag, appId)
            }
            activity.startActivityForResult(intent, externalWebViewReqCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackIcon()
        title = "External WebView Sample"

        val miniAppExternalUrlLoader = MiniAppExternalUrlLoader(
            intent.getStringExtra(miniAppIdTag) ?: "", this)
        val webViewClient = SampleWebViewClient(miniAppExternalUrlLoader)

        sampleExternalWebView = SampleExternalWebView(this, intent.getStringExtra(loadUrlTag) ?: "", webViewClient)
        setContentView(sampleExternalWebView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (sampleExternalWebView.canGoBack())
            sampleExternalWebView.goBack()
        else
            super.onBackPressed()
    }
}
