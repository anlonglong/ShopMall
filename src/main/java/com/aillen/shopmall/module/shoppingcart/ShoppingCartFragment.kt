package com.aillen.shopmall.module.main.shoppingcart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseFragment
import com.aillen.shopmall.extensions.ctx
import com.aillen.shopmall.module.login.LoginEvent
import com.aillen.shopmall.module.login.LoginObervable
import com.aillen.shopmall.module.login.LoginObserver
import com.aillen.shopmall.module.order.OrderModel
import com.aillen.shopmall.module.pay.PayActivity
import com.aillen.shopmall.module.shoppingcart.CartAdapter
import com.aillen.shopmall.module.shoppingcart.CartViewModel
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.utils.CommonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_shopping.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
class ShoppingCartFragment : BaseFragment(),LoginObserver, View.OnClickListener {


    private val shopLists:MutableList<Shop> = mutableListOf()

    /**
     * 回调来以后这里拿不到ShoppingCartFragment中的activity的引用和所用有控件的引用[LoginActivity]dd
     * **/

    override fun update(observable: LoginObervable, args: Any?, isLogin: Boolean) {
        Log.i("Main","observable = [${observable}], args = [${args}], isLogin = [${isLogin}]")
        //loadData(null)
    }


    override fun intListener() {
        tv_pay.setOnClickListener(this)
        tv_delete.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        when(v.id) {
            R.id.tv_delete ->{
                val baseQuickAdapter = cart_list.adapter as BaseQuickAdapter<Shop, BaseViewHolder>
                val data = baseQuickAdapter.data.toMutableList()
                data.removeAll(shopLists)
                viewModel.deleteSelectedShop(shopLists.map { it.S_OID }){
                    toast(it)
                    shopLists.clear()
                    tv_all_money.text = CommonUtils.fillFormatString(ctx,R.string.price,"0.00")
                    baseQuickAdapter.setNewData(data)
                }

            }
            R.id.tv_pay ->{
                if (!shopLists.isEmpty()) {
                    PayActivity.start(v.ctx, OrderModel.ShopOrder(shopLists,null))
                }else{
                    toast("请至少选中一件商品")
                }
            }
        }
    }

    //eventbus可以拿到ShoppingCartFragment中的activity的引用和所用有控件的引用
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun loginEvent(event:LoginEvent){
        println("eventLog = [${event}]")
        loadData(null)
    }


    override fun enableEventBus(): Boolean {
        return true
    }

    private var viewModel:CartViewModel by Delegates.notNull()
    companion object {
        fun getInstance() = ShoppingCartFragment()
    }


    override fun loadData(mRootView: View?) {
        (cart_list.adapter as? BaseQuickAdapter<Shop,BaseViewHolder>)?.replaceData(mutableListOf())
        if (!UserManager.isLogin) {
            CommonUtils.showProgress(ctx,false,pb_progress)
            toast(getString(R.string.hint_not_login))
            /**这里暂时弹一个toast，展示登录对话框的话在登录成功的之后应该刷新数据但是这里没有做回调的处理
             * 结果就是在登录成功后返回来的时候没有去获取数据，导致界面还是空的。
             改进：
             **/
           // CommonUtils.showLoginDialog(this.activity)
            return@loadData
        }
        //viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.getApplication()).create(CartViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        viewModel.errorObserver?.observe(this, Observer {
            CommonUtils.showProgress(ctx,false,pb_progress)
            toast(if (it?.errorCode == 9016) getString(R.string.hint_internet_not_connected) else it?.message!!)
        })
        viewModel.queryShopByListId{
            CommonUtils.showProgress(ctx,false,pb_progress)
            cart_bottom_container.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            if(it.isEmpty()) {
                toast(getString(R.string.hint_cart_empty))
                return@queryShopByListId   /**这里只能返回到lambda处，无法返回到loadData函数，导致的结果就是queryShopByListId后面的语句还是会执行**/
            }
            val cartAdapter = CartAdapter(it)
            cart_list.adapter = cartAdapter
            cartAdapter.setOnCheckChangedListener { shop,_,_, b ->
                run {
                    val oldMoney = tv_all_money.text.split("￥").joinToString(separator = "").trim()
                    println("oldMoney = $oldMoney")
                    when (b) {
                        true -> {
                            shopLists.add(shop)
                            tv_all_money.text = CommonUtils.fillFormatString(ctx,R.string.price,add(oldMoney,shop.price))
                        }
                        false -> {
                            shopLists.remove(shop)
                            if (oldMoney != "0.00")
                            tv_all_money.text = CommonUtils.fillFormatString(ctx,R.string.price,sub(oldMoney,shop.price))
                        }
                    }
                }
            }
    }
        println("return has executor this line is ready to exec")
    }


    fun add(oldMoney:String,newMoney:String): String {
        val b1 = BigDecimal(oldMoney)
        val b2 = BigDecimal(newMoney)
        return b1.add(b2).toPlainString()
    }

    fun sub(oldMoney:String,newMoney:String): String {
        val b1 = BigDecimal(oldMoney)
        val b2 = BigDecimal(newMoney)
        return b1.subtract(b2).toPlainString()
    }


    override fun onResume() {
        super.onResume()
    }


    override fun initView() {
        cart_list.layoutManager =LinearLayoutManager(ctx)
    }

    override fun onDetach() {
        super.onDetach()
        println("onDetach")
    }

    override fun getLayoutId() = R.layout.fragment_shopping
}