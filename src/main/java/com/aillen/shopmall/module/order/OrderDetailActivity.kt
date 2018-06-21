package com.aillen.shopmall.module.order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.content_order_detail.*
import java.math.BigDecimal

class OrderDetailActivity : BaseActivity(), View.OnClickListener {

    private val shopOrder: OrderModel.ShopOrder by lazy { getIntentExtra().getSerializableExtra(SHOP_ORDER) as OrderModel.ShopOrder }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    override fun initData() {


        val order = shopOrder.order!!
        CommonUtils.disPlayImageWithGlide(this, shopOrder.shopList[0].show_urls[0], iv_shop_pic)
        order_id.setLeftTopString(CommonUtils.formatString(this,R.string.order_no,order.order_number))
        user_info.setLeftString(order.realname)
        user_info.setCenterString(order.phone)
        location.setLeftString(order.address)
        pay_way.setRightTopString(order.pay_way)
        express_info.setRightTopString(order.express_type)

        tv_express_date.text = CommonUtils.formatString(this, R.string.pots_date, order.express_date)
        good_sum_money.setRightTopString(CommonUtils.fillFormatString(this,R.string.price,order.sum_money))
        yun_fei.setRightTopString(CommonUtils.fillFormatString(this,R.string.price,order.postage))
        bill_type.text = CommonUtils.formatString(this,R.string.bill_tile,order.bill_title)
        bill_msg.text = CommonUtils.formatString(this,R.string.bill_tile,order.bill_message)
        fun calc(postage: String, price: String): Double {
            val v1 = BigDecimal(postage)
            val v2 = BigDecimal(price)
            return v1.add(v2).toDouble()
        }
        total_money.setRightTopString(CommonUtils.formatString(this,R.string.real_money,calc(order.postage, order.sum_money)))

        order_time.setRightTopString(CommonUtils.formatString(this,R.string.order_time,order.createdAt))
        when (order.state) {
            State.STATE_SEND.getState() -> {
                order_id.setRightTopString("待发货")
            }
            State.STATE_COMPELTE.getState() -> {
                order_id.setRightTopString("已完成")
            }
            State.STATE_WAIT.getState() -> {
                order_id.setRightTopString("待评价")
            }
            State.STATE_PAY.getState() -> {
                order_id.setRightTopString("待付款")
            }
            State.STATE_GET.getState() -> {
                order_id.setRightTopString("待收货")
            }
        }
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = ShopOrderItemAdapter(shopOrder.shopList)

    }

    override fun initView() {

        toolbar_layout.title = "订单中心"
        toolbar_layout.setExpandedTitleColor(Color.parseColor("#ff5e00"))
        toolbar_layout.setCollapsedTitleTextColor(resources.getColor(android.R.color.black))
        setSupportActionBar(toolbar)
    }

    override fun initListener() {
        iv_back.setOnClickListener(this)
    }

    override fun getLayoutId() = R.layout.activity_order_detail

    companion object {
        private const val SHOP_ORDER = "ShopOrder"
        fun start(ctx: Context, shopOrder: OrderModel.ShopOrder) {
            val intent = Intent(ctx, OrderDetailActivity::class.java)
            intent.putExtra(SHOP_ORDER, shopOrder)
            ctx.startActivity(intent)
        }
    }

}
