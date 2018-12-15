package app.woovictory.boostcamp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import app.woovictory.boostcamp.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        var setting : WebSettings = movieWeb.settings
        setting.setAppCacheEnabled(false)
        setting.javaScriptEnabled = true

        movieWeb.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                movieWeb.loadUrl(url)
                return true
            }
        }

        movieWeb.webChromeClient = object : WebChromeClient(){

        }
        var url : String = getIntent().getStringExtra("link")
        Log.v("752 TAG",url.toString())
        movieWeb.loadUrl(url)
    }
}
