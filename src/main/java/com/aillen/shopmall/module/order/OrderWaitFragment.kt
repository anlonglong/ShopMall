package com.aillen.shopmall.module.order

/**
 * Created by anlonglong on 2018/5/24.
 * Email： 940752944@qq.com
 */
class OrderWaitFragment:BaseOrderFragment() {
    override fun getState(): State {
        return State.STATE_WAIT
    }

    companion object {
        fun getInstance() = OrderWaitFragment()
    }
}