package com.rakuten.tech.mobile.testapp.ui.display

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CustomSchemeInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: Uri? = intent?.data

        if (data != null && data.isHierarchical) {
            val msg = data.getQueryParameter("message")
            val itemName = data.getQueryParameter("itemName")
            val itemPrice: String? = data.getQueryParameter("itemPrice")
            val itemDesc: String? = data.getQueryParameter("itemDesc")
            val itemImgUrl: String? = data.getQueryParameter("itemImgUrl")

            var baseText = ""

            if (msg.isNullOrBlank()) {
                baseText = " Hey! I bought $itemName for just JPY$itemPrice. \n $itemDesc \n $itemImgUrl"
                Toast.makeText(
                    this, "Bought $itemName for $itemPrice",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                baseText = msg
                Toast.makeText(
                    this, msg,
                    Toast.LENGTH_SHORT
                ).show()
            }

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, baseText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
}
