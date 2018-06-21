package com.aillen.shopmall.utils

import com.aillen.shopmall.BuildConfig

/**
 * Created by anlonglong on 2018/6/6.
 * Email： 940752944@qq.com
 */
fun println(message:Any){
    if (BuildConfig.DEBUG) {
        kotlin.io.println(message)
    }
}