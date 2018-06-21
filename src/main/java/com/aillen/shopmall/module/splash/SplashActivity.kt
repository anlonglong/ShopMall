package com.aillen.shopmall.module.splash

import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.store.Shop

class SplashActivity : BaseActivity() {


    override fun getLayoutId() = R.layout.activity_splash

    override fun initData() {

    }

    override fun initView() {
        getHandler()?.postDelayed({MainActivity.start(SplashActivity@this)},1000)
    }

}
