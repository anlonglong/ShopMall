package com.aillen.shopmall.module.order

/**
 * Created by anlonglong on 2018/5/17.
 * Email： 940752944@qq.com
 */
class OrderAllFragment : BaseOrderFragment() {
    override fun getState() = State.STATE_ALL

    companion object {
        fun getInstance() = OrderAllFragment()
    }
}