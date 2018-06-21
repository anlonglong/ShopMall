package com.aillen.shopmall.base

import cn.bmob.v3.BmobQuery

/**
 * Created by anlonglong on 2018/4/27.
 * Email： 940752944@qq.com
 */
interface IBasePresenter<V : IBaseView> {

    val mPolicy: BmobQuery.CachePolicy
        get() = BmobQuery.CachePolicy.NETWORK_ONLY
    val mPageCount: Int
        get() = 12
    val mLimitPage: Int
        get() = 80
    val mConnectTime: Long
        get() = 50000L
    val mIntervalTime: Long
        get() = 50000L
    /*
      具体的presenter会在构造中把值传进来
     */
    var view: V?


    fun detachView() {
        this.view = null
    }

    fun isAttach(): Boolean = this.view != null
}