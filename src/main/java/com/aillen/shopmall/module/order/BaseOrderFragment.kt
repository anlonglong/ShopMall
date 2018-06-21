package com.aillen.shopmall.module.order

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.bmob.v3.exception.BmobException
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseApplication
import com.aillen.shopmall.base.BaseFragment
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.store.Shop
import kotlinx.android.synthetic.main.order_center_fagment.*

/**
 * Created by anlonglong on 2018/5/24.
 * Email： 940752944@qq.com
 */
abstract class BaseOrderFragment : BaseFragment() {
    private var isFirstIn = true

    override fun initView() {
        order_list.layoutManager = LinearLayoutManager(ctx)
    }

    override fun loadData(mRootView: View?) {
        showProgress(true)
        val orderModel = ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.getApplication())).get(OrderModel::class.java)
//        if (!UserManager.isLogin) {
//            showProgress(false)
//            toast("请先登录")
//            return@loadData
//        }
        orderModel.init(UserManager.objId, getState())
        orderModel.observerError().observe(this, Observer<BmobException> { showProgress(false) })
        orderModel.getAllOrders().observe(this, Observer<List<Order>> { orderModel.query(it!!) })
        orderModel.getOrdersShops().observe(this, Observer<List<Shop>> {
            showProgress(false)
            val shopOrder = orderModel.getShopOrders()
            if (!shopOrder.isEmpty()) {
                val shopOrderAdapter = ShopOrderAdapter(shopOrder)
                order_list.adapter = shopOrderAdapter
                isFirstIn = false
                shopOrderAdapter.setOnItemClickListener { _, _, position -> OrderDetailActivity.start(ctx, shopOrder[position]) }
            }else{
                toast("空空如也")
            }
        })

    }


    abstract fun getState(): State


    override fun getLayoutId() = R.layout.order_center_fagment


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            order_list.visibility = if (show) View.GONE else View.VISIBLE
            order_list.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            order_list.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            pb_loading.visibility = if (show) View.VISIBLE else View.GONE
            pb_loading.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            pb_loading.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pb_loading.visibility = if (show) View.VISIBLE else View.GONE
            order_list.visibility = if (show) View.GONE else View.VISIBLE
        }
    }


    override fun onStart() {
        super.onStart()
        if (!isFirstIn) {
            loadData(null)
        }
    }
}