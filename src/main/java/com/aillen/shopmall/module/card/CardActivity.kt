package com.aillen.shopmall.module.card

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_card.*

class CardActivity : BaseActivity() {

    companion object {
        fun start(ctx: Context, needLogin: Boolean) {
            if (needLogin && UserManager.isLogin) {
                val intent = Intent(ctx, CardActivity::class.java)
                ctx.startActivity(intent)
            }else{
                 Toast.makeText(ctx,"(づ￣3￣)づ╭❤～请先登录",Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun initView() {
        card_list.layoutManager = LinearLayoutManager(this)
    }

    override fun initData() {
        val cardViewModule = ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))[CardViewModule::class.java]
        with(cardViewModule) {
            postDelayedTime({ query() }, 300)
            observerError().observe(this@CardActivity, Observer {
                card_empty_hint.visibility = View.VISIBLE
                CommonUtils.showProgress(this@CardActivity, false, progress, null)
                toast(it?.message!!)
            })
            cardDatas.observe(this@CardActivity, Observer {
                card_empty_hint.visibility = View.GONE
                CommonUtils.showProgress(this@CardActivity, false, progress, null)
                card_list.adapter = CardAdapter(it)
            })
        }

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
    }

    override fun getLayoutId() = R.layout.activity_card

}
