package com.aillen.shopmall.module.detail

import com.aillen.shopmall.module.detail.head.IHead
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/5/4.
 * Email： 940752944@qq.com
 */
class HeadController(adapter: DetailAdapter) {

    private var mAdapter by Delegates.notNull<DetailAdapter>()
    private var iHead by Delegates.notNull<IHead>()

    init {
        mAdapter = adapter
    }

    fun addHead(head: IHead) {
        iHead = head
        iHead.setAdapter(mAdapter)
    }

    fun <T> setDate(data:T) {
        iHead.initData(data)
        iHead.initListener()
    }


}