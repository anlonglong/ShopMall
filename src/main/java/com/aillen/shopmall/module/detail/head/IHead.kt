package com.aillen.shopmall.module.detail.head

import com.aillen.shopmall.module.detail.DetailAdapter

/**
 * Created by anlonglong on 2018/5/4.
 * Email： 940752944@qq.com
 */
interface IHead {
    fun setAdapter(adapter: DetailAdapter)
    fun addHead()
    fun <T> initData(data: T)
    fun initListener()
}