package com.pascal.ecommercecompose.ui.screen.cart.component

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CartPayment(
    snapUrl: String,
    onFinish: () -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val url = request?.url.toString()
                        Log.d("Payment", "Loaded URL: $url")

                        if (url.contains("https://simulator.sandbox.midtrans.com")) {
                            Toast.makeText(context, "Pembayaran Anda Sudah Selesai 🎉", Toast.LENGTH_LONG).show()
                            onFinish()
                            return true
                        }
                        return false
                    }
                }
                loadUrl(snapUrl)
            }
        },
        update = { it.loadUrl(snapUrl) }
    )
}