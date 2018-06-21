package com.aillen.shopmall.extensions

import android.content.Context
import android.os.Build
import android.support.annotation.DrawableRes
import android.text.Html
import android.view.View
import android.widget.TextView


/**
 * Created by anlonglong on 2018/5/22.
 * Email： 940752944@qq.com
 */
val View.ctx:Context
get() = this.context
fun TextView.setTopDrawable(@DrawableRes topDrawableRes: Int) =
    this.setCompoundDrawables(null,with(resources) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getDrawable(topDrawableRes, null)
        } else {
            getDrawable(topDrawableRes)
        }
    }.also { it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight) },null,null)

fun TextView.setHtml(source:String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(source,Html.FROM_HTML_MODE_LEGACY)
    }else{
        this.text = Html.fromHtml(source)
    }
}
