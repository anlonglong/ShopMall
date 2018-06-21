package com.aillen.shopmall.module.love

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

/**
 * Created by anlonglong on 2018/5/25.
 * Email： 940752944@qq.com
 */

private const val ORDER = "S_OID"
private const val OBJECT_ID = "objectId"

class LoveModel(app:Application):BaseViewModle(app){

    private val loveShopList = MutableLiveData<List<Shop>>()

    fun queryLoveShopList(loveShopId: List<String>){
        val query = BmobQuery<Shop>()
        query.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        query.setLimit(80)
        query.order(ORDER)
        query.addWhereContainedIn(OBJECT_ID,loveShopId)
        query.findObjects(object :FindListener<Shop>(){
            override fun done(list: MutableList<Shop>?, e: BmobException?) {

                when(e) {
                    null ->{loveShopList.value = list}
                    else ->{
                        observerError().value = e
                        Toast.makeText(getApplication() as BaseApplication,if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString(), Toast.LENGTH_SHORT).show()}
                }
            }

        })
    }

    fun getLoveShopList() = loveShopList

}