package com.aillen.shopmall.module.splash

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.text.Html
import android.util.Log
import android.view.ViewGroup
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.login.LoginObervable
import com.aillen.shopmall.module.login.LoginObserver
import com.aillen.shopmall.module.main.category.CategoryFragment
import com.aillen.shopmall.module.main.disvocery.DiscoveryFragment
import com.aillen.shopmall.module.main.home.HomeFragment
import com.aillen.shopmall.module.main.mine.MineFragment
import com.aillen.shopmall.module.main.shoppingcart.ShoppingCartFragment
import kotterknife.bindView
import java.util.*

/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
class MainActivity : BaseActivity() {

    private val navigation: BottomNavigationView by bindView(R.id.navigation)
    private val viewPager: ViewPager by bindView(R.id.view_pager)


    companion object {
        fun start(activity: BaseActivity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun initView() {

    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity","onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity","onDestroy")
    }

    val bottomLists = arrayListOf(
            HomeFragment.getInstance(),
            CategoryFragment.getInstance(),
            DiscoveryFragment.getInstance(),
            ShoppingCartFragment.getInstance(),
            MineFragment.getInstance())
    override fun initData() {
        viewPager.offscreenPageLimit =  bottomLists.size
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int) = bottomLists[position]

            override fun getCount() = bottomLists.size

        }



        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                navigation.selectedItemId = when (position) {
                    0 -> R.id.navigation_home
                    1 -> R.id.navigation_category
                    2 -> R.id.navigation_discovery
                    3 -> R.id.navigation_shop
                    4 -> R.id.navigation_mine
                    else -> -1
                }
            }

        })

    }

    override fun initListener() {
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.navigation_home -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.navigation_category -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.navigation_discovery -> {
                    //(bottomLists[2] as DiscoveryFragment).refresh()
                    viewPager.currentItem = 2
                    true
                }
                R.id.navigation_shop -> {
                    //bottomLists[3].resetFlagState()
                    viewPager.currentItem = 3
                    true
                }
                R.id.navigation_mine -> {
                    viewPager.currentItem = 4
                    true
                }
                else -> false
            }
        }

    }

}