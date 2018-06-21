package com.aillen.shopmall.module.login


/**
 * Created by anlonglong on 2018/6/18.
 * Email： 940752944@qq.com
 */
interface LoginObserver{

    fun update(observable: LoginObervable, args:Any?,isLogin:Boolean)
}