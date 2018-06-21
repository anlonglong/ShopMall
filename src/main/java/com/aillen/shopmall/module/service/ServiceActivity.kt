package com.aillen.shopmall.module.service

import android.content.Context
import android.content.Intent
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.collection.CollectionActivity
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : BaseActivity() {

    companion object {
        fun strat(ctx:Context){
            val intent = Intent(ctx, ServiceActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initView() {

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
    }

    override fun getLayoutId() = R.layout.activity_service

}
