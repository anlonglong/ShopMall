package com.aillen.shopmall.module.order

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.base.BaseApplication
import com.aillen.shopmall.base.BaseViewModle
import com.aillen.shopmall.module.store.Shop
import java.io.Serializable

/**
 * Created by anlonglong on 2018/5/18.
 * Email： 940752944@qq.com
 */
class OrderModel(val app:Application) : BaseViewModle(app) {


     var data: MutableLiveData<List<Order>> = MutableLiveData()
     var orderShop: MutableLiveData<List<Shop>> = MutableLiveData()

    fun init(uiod: String?,sate:State) {
        val query = BmobQuery<Order>()
        query.cachePolicy =BmobQuery.CachePolicy.NETWORK_ONLY
        query.setLimit(80)
        query.order("-createdAt")
        query.addWhereEqualTo("U_OID", uiod)
        if (sate != State.STATE_ALL) {
            query.addWhereEqualTo("state", sate.getState())
        }
        query.findObjects(object : FindListener<Order>(){
            override fun done(list: List<Order>?, e: BmobException?) {
                println("list = [$list], e = [$e]")
                when(e) {
                    null ->{
                        data.postValue(list)}
                    else ->{
                        observerError().value = e
                        Toast.makeText(getApplication() as BaseApplication,if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString(),Toast.LENGTH_SHORT).show()}
                }
            }
        })
    }

    fun query(it: List<Order>) {
        val list = mutableListOf<String>()
        it.forEach{ list.addAll(it.S_OID) }
        val query = BmobQuery<Shop>()
        query.cachePolicy =BmobQuery.CachePolicy.NETWORK_ONLY
        query.setLimit(80)
        query.order("-S_OID")
        query.addWhereContainedIn("objectId", list)
        query.findObjects(object : FindListener<Shop>(){
            override fun done(list: MutableList<Shop>?, e: BmobException?) {
                println("list = [$list], e = [$e]")
                when(e) {
                    null ->{orderShop.postValue(list)}
                    else ->{
                        observerError().value = e
                        Toast.makeText(getApplication() as BaseApplication,if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString(),Toast.LENGTH_SHORT).show()}
                }
            }
        })
    }



    fun getAllOrders():LiveData<List<Order>> = data

    fun getOrdersShops():LiveData<List<Shop>> = orderShop

    /**
     * 一年前写的代码和一年后写的代码，还是熟悉的配方，只是不同的味道。真是佩服自己的赖，
     */
    fun getShopOrders():List<ShopOrder> {
        val (orderList, shopList) = Pair(data.value!!, orderShop.value!!)
        val shopOrderList = mutableListOf<ShopOrder>()
        orderList.forEach {
            o -> shopOrderList.add(ShopOrder( shopList.filter
            {s -> o.S_OID.contains(s.objectId) }
            .toList(),o)) }
        return shopOrderList
    }

    infix fun <T> T.into(other: Collection<T>): Boolean = other.contains(this)

    data class ShopOrder(val shopList: List<Shop>, val order: Order?):Serializable
}