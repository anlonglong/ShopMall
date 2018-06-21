package com.aillen.shopmall.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import cn.bmob.v3.exception.BmobException

/**
 * Created by anlonglong on 2018/5/25.
 * Email： 940752944@qq.com
 */
open class BaseViewModle(application: Application):AndroidViewModel(application) {
    private val error = MutableLiveData<BmobException>()
    fun observerError() = error
}