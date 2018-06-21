package com.aillen.shopmall.module.order

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout.MODE_FIXED
import android.support.v4.app.FragmentStatePagerAdapter
import android.widget.Toast
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.user.UserManager
import kotlinx.android.synthetic.main.activity_order.*


class OrderActivity : BaseActivity(){

    companion object {
        fun start(ctx: Context,needLogin: Boolean) {
            if (needLogin && UserManager.isLogin) {
                val intent = Intent(ctx, OrderActivity::class.java)
                ctx.startActivity(intent)
            }else{
                Toast.makeText(ctx,"(づ￣3￣)づ╭❤～请先登录",Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun initView() {
        val titleList = arrayOf("全部", "待付款", "待发货", "待收货", "待评价")
        order_tap.tabMode = MODE_FIXED
        val arrayListOf = arrayListOf(OrderAllFragment.getInstance(),
                OrderPayFragment.getInstance(),
                OrderSendFragment.getInstance(),
                OrderGetFragment.getInstance(),
                OrderWaitFragment.getInstance())
        main_viewpager.offscreenPageLimit = arrayListOf.size
        main_viewpager.adapter = object :FragmentStatePagerAdapter(supportFragmentManager){
            override fun getItem(position: Int) = arrayListOf[position]

            override fun getCount() = arrayListOf.size

            override fun getPageTitle(position: Int): CharSequence {

                return titleList[position]
            }

        }
        order_tap.setupWithViewPager(main_viewpager)

    }

    override fun getLayoutId() = R.layout.activity_order


}
