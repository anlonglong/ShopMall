package com.aillen.shopmall.module.shoppingcart

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.aillen.shopmall.module.login.LoginObervable
import com.aillen.shopmall.module.login.LoginObserver
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.user.UserManager

/**
 * Created by anlonglong on 2018/6/15.
 * Email： 940752944@qq.com
 */

private const val S_OID = "S_OID"
private const val OBJECTID = "objectId"
class CartViewModel:ViewModel(){

    val login = MutableLiveData<Boolean>()
     val logout = MutableLiveData<Boolean>()

    private val error = MutableLiveData<BmobException>()
    /**
     * 之所以用Transformations.switchMap来创建出错时候的LiveData，是因为如果已经有可活动的观察者的话，就不会再重复的触发UI层的观察者，
     * 这样可以减少在UI层的重复调用
     * 如果直接创建一个错误LiveData观察的话，每次数据出错都会触发UI，增加UI层的压力
     */
    private val _errorObserver = Transformations.switchMap(error,{ getErrorLiveData(it) })!!
    val errorObserver: LiveData<BmobException>?
        get() = _errorObserver

    private fun getErrorLiveData(e: BmobException): LiveData<BmobException> {
        val data = MutableLiveData<BmobException>()
        data.value = e
        return data
    }


    fun queryShopByListId(onSuccess:(List<Shop>) ->Unit){
        val bmobQuery = bmobQuery<Shop>()
        bmobQuery.order(S_OID)
        bmobQuery.addWhereContainedIn(OBJECTID,UserManager.cartOid)
        bmobQuery.findObjects(object : FindListener<Shop>(){
            override fun done(list: List<Shop>?, e: BmobException?)  = when(e){
                null ->{onSuccess(list!!)}
                else ->{error.value = e}
            }
        })
    }

    private  fun <T> bmobQuery(): BmobQuery<T> {
        val bannerQuery = BmobQuery<T>()
        bannerQuery.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        bannerQuery.setLimit(80)
        return bannerQuery
    }

    fun deleteSelectedShop(selectedObjs:List<String>,onsuccess:(String) ->Unit){
        if (!selectedObjs.isEmpty()){
            val cartOid = UserManager.cartOid
            val filter = cartOid.filter { !selectedObjs.contains(it) }
            val user = UserManager.getCurrentUser()!!
            user.copy(cart_oid = filter)
            user.objectId = UserManager.objId
            user.update(object : UpdateListener(){
                override fun done(e: BmobException?) {
                    when(e) {
                        null ->{onsuccess("更新成功")}
                        else ->{error.value = e}
                    }
                }

            })
        }
    }

}