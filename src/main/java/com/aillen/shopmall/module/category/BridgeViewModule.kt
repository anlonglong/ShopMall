package com.aillen.shopmall.module.category

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by anlonglong on 2018/6/11.
 * Email： 940752944@qq.com
 */
object BridgeViewModule : ViewModel() {
    val data = MutableLiveData<Pair<String,Int>>()
}