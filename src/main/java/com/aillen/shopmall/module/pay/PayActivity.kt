package com.aillen.shopmall.module.pay

import android.content.Context
import android.content.Intent
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.login.LoginObervable
import com.aillen.shopmall.module.login.LoginObserver
import com.aillen.shopmall.module.order.Order
import com.aillen.shopmall.module.order.OrderModel
import com.aillen.shopmall.module.order.State
import kotlinx.android.synthetic.main.activity_pay.*
import java.math.BigDecimal

class PayActivity : BaseActivity(), View.OnClickListener{

    private val shopOrder:OrderModel.ShopOrder by lazy { getIntentExtra().getSerializableExtra(SHOP_ORDER) as OrderModel.ShopOrder }

    companion object {
        private const val SHOP_ORDER = "ShopOrder"
        fun start(ctx: Context, shopOrder: OrderModel.ShopOrder) {
            val intent = Intent(ctx, PayActivity::class.java)
            intent.putExtra(SHOP_ORDER,shopOrder)
            ctx.startActivity(intent)
        }
    }

    override fun initView() {
        var money = 0.0
        fun calc(postage: String, price: String):Double {
            val v1 = BigDecimal(postage)
            val v2 = BigDecimal(price)
            return v1.add(v2).toDouble()
        }
        shopOrder.shopList.forEach { money+=calc(it.postage,it.price) }
        order_money.setRightString("${money}元")
    }

    override fun initListener() {
        pay_weixin.setOnClickListener(this)
        pay_zhifubao.setOnClickListener(this)
        pay_QQ.setOnClickListener(this)
        iv_back.setOnClickListener{finish()}
    }

    override fun onClick(v: View) {
    val payWay =  when(v.id){
         R.id.pay_weixin ->{ PayWay.WEI_XIN.type }
         R.id.pay_zhifubao ->{ PayWay.ZHI_FU_BAO.type }
         R.id.pay_QQ ->{ PayWay.QQ.type }
         else -> "unknown type"
     }
        payOrder(payWay,shopOrder.order)
    }

    private fun payOrder(payWay: String, order: Order?) {
        order?.state = State.STATE_SEND.getState()
        order?.pay_way = payWay
        val copy = order?.copy()
        copy?.objectId =  order?.objectId
        copy?.update(object : UpdateListener(){
            override fun done(e: BmobException?) {
                when(e){
                    null ->{
                        toast("更新成功")
                        finishDelay(300)
                    }
                    else ->{toast(e.message!!)}
                }
            }
        })
    }


    override fun getLayoutId() = R.layout.activity_pay

    enum class PayWay(val type: String) {
        WEI_XIN("微信"){
            override fun getPayType() = type
        },
        ZHI_FU_BAO("支付宝") {
            override fun getPayType() = type
        },
        QQ("QQ") {
            override fun getPayType() = type
        };
        abstract fun getPayType():String
    }
}
