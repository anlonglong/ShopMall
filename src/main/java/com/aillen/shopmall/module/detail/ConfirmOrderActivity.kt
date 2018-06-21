package com.aillen.shopmall.module.detail

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.order.Order
import com.aillen.shopmall.module.order.OrderModel
import com.aillen.shopmall.module.order.ShopOrderItemAdapter
import com.aillen.shopmall.module.order.State
import com.aillen.shopmall.module.pay.PayActivity
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.store.Store
import com.aillen.shopmall.module.store.StoreContract
import com.aillen.shopmall.module.store.StorePresenter
import com.aillen.shopmall.repository.DAOManager
import com.aillen.shopmall.utils.CommonUtils
import com.aillen.shopmall.utils.println
import kotlinx.android.synthetic.main.activity_confirm_order.*
import java.math.BigDecimal

class ConfirmOrderActivity : BaseActivity(), StoreContract.View {
    override fun onStore(mutableList: List<Store>?) {

    }

    override fun onShop(list: List<Shop>?) {
    }

    override fun onUser() {
    }

    override fun onError(code: Int, msg: String) {
    }

    override fun onSuccess() {

    }

    private val mShop: Shop by lazy { getIntentExtra().getSerializableExtra(ConfirmOrderActivity.SHOP) as Shop }


    companion object {
        private val SHOP: String = "shop"
        fun start(ctx: Context, shop: Shop, needLogin: Boolean) {
            if (needLogin) {
                val intent = Intent(ctx, ConfirmOrderActivity::class.java)
                intent.putExtra(SHOP, shop)
                ctx.startActivity(intent)
            } else {
                CommonUtils.showLoginDialog(ctx)
            }
        }
    }

    override fun initView() {
        order_item.layoutManager = LinearLayoutManager(this)

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
        buy_now.setOnClickListener {
            val shops = arrayListOf<Shop>()

            shops.add(mShop)
            val order = Order(
                    UserManager.objId!!,
                    arrayListOf(),
                    State.STATE_PAY.getState(),
                    "",
                    "",
                    user_info.leftString,
                    location.leftString,
                    user_info.centerString,
                    CommonUtils.getOrderNumber(),
                    "闪电快递",
                    "节假日",
                    "微信",
                    "个人",
                    "电子发票",
                    "明细",
                     mShop.postage,
                    calc(mShop.postage,mShop.price).toString())
            order.objectId = UserManager.objId
            PayActivity.start(this, OrderModel.ShopOrder(shops, order))
            finish()
        }
    }

    override fun initData() {
        setAddressView()
        order_item.adapter = ShopOrderItemAdapter(arrayListOf<Shop>() + mShop)
        val storePresenter = StorePresenter(this)
        storePresenter.storeQueryByListId(arrayListOf<String>() + mShop.S_OID)
        postage_way.setRightTopString("雅妮快递")
        postage_date.setRightTopString("工作日、双休日与假日均可送货")
        bill_info.setRightString("个人-电子发票-明细")
        sum_money.setRightString(mShop.price)
        postage_money.setRightString(mShop.postage)
        real_money.text = CommonUtils.formatString(this, R.string.real_money, calc(mShop.postage, mShop.price))
    }

    fun calc(postage: String, money: String): Double {
        val v1 = BigDecimal(postage)
        val v2 = BigDecimal(money)
        return v1.add(v2).toDouble()
    }

    private fun setAddressView() {
        val addressDao = DAOManager.get(this).addressDao()
        for (address in addressDao.query(UserManager.userName)) {
            if (address.isDefault!!) {
                println(address)
                user_info.setLeftString(address.realName)
                user_info.setCenterString(address.phone)
                location.setLeftString(address.area + address.street + address.address)
                return@setAddressView
            }
        }

    }

    override fun getLayoutId() = R.layout.activity_confirm_order

}
