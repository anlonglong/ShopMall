package com.aillen.shopmall.module.order

/**
 * Created by anlonglong on 2018/5/24.
 * Email： 940752944@qq.com
 */
class OrderPayFragment:BaseOrderFragment(){
    override fun getState() = State.STATE_PAY
    companion object {
        fun getInstance() = OrderPayFragment()
    }
}