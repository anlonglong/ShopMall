package com.aillen.shopmall.module.love

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.bmob.v3.exception.BmobException
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.base.BaseApplication
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.store.Shop
import kotlinx.android.synthetic.main.activity_love.*

class LoveActivity : BaseActivity() {

     companion object {
         fun start(ctx: Context) {
             val intent = Intent(ctx, LoveActivity::class.java)
             ctx.startActivity(intent)
         }
     }

    override fun initView() {
        rv_love_list.layoutManager = LinearLayoutManager(this)

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
    }

    override fun initData() {
        showProgress(true)
        val loveIdList = UserManager.loveId
        val loveModel = ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.getApplication()))[LoveModel::class.java]
        loveModel.queryLoveShopList(loveIdList)
        loveModel.observerError().observe(this, Observer<BmobException> {  showProgress(false) })
        loveModel.getLoveShopList().observe(this, Observer<List<Shop>> {
                 showProgress(false)
                 if (it!!.isEmpty()) {
                     tv_love_empty_hnt.visibility  = View.VISIBLE
                     btn_delete.visibility  = View.VISIBLE
                     return@Observer
                 }else{
                     btn_delete.visibility  = View.GONE
                     tv_love_empty_hnt.visibility  = View.GONE
                     rv_love_list.adapter = LoveShopAdapter(it)
                 }
        })
    }

    override fun getLayoutId() = R.layout.activity_love

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            rv_love_list.visibility = if (show) View.GONE else View.VISIBLE
            rv_love_list.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            rv_love_list.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            tv_love_progress.visibility = if (show) View.VISIBLE else View.GONE
            tv_love_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            tv_love_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            tv_love_progress.visibility = if (show) View.VISIBLE else View.GONE
            rv_love_list.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

}
