package com.aillen.shopmall.module

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.category.RightFragment
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : BaseActivity() {

    private val url by lazy { getIntentExtra().getStringExtra(URL) }

    companion object {
        private const val URL= "url"
        fun start(ctx:Context,url:String){
            val intent = Intent(ctx, WebActivity::class.java)
            intent.putExtra(URL,url)
            ctx.startActivity(intent)
        }
    }

    override fun initData() {
       webView.loadUrl(url)
        //webView.loadUrl("https://www.baidu.com/")
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroy() {
        webView.clearHistory()
        webView.destroy()
        (webView.parent as ViewGroup).removeView(webView)
        super.onDestroy()
    }

    override fun initView() {
        webView.loadUrl("https://www.baidu.com/")
         webView.settings?.run {
             setAppCacheEnabled(true)
             cacheMode = WebSettings.LOAD_DEFAULT
             javaScriptEnabled = true
             javaScriptCanOpenWindowsAutomatically = true
             allowFileAccess = true
             domStorageEnabled = true
             databaseEnabled = true
             setSupportZoom(true)
             builtInZoomControls = true
             displayZoomControls = false
             defaultTextEncodingName = "UTF-8"
             setGeolocationEnabled(true)
         }


        webView.run {
            webViewClient = MyWebClient()
            webChromeClient = MyChromeClient()
        }
    }

     class  MyWebClient : WebViewClient(){

         override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
             println("onPageStarted")
         }

         override fun onPageFinished(view: WebView, url: String) {
             println("onPageFinished")
         }

         override fun onReceivedError(view: WebView, request: WebResourceRequest?, error: WebResourceError?) {
             println("onReceivedError")
         }

         // 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
         @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
         override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
             view.loadUrl(request.url.toString())
             return true
         }
    }

    inner class MyChromeClient: WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            println("view = [$view], newProgress = [$newProgress]")
            if (newProgress < 100) {
            web_progress.progress = newProgress
        }else{
            web_progress.visibility = View.GONE
            }
        }

        override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
            super.onReceivedIcon(view, icon)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun getLayoutId() = R.layout.activity_web

}
