package com.aillen.shopmall.module.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.module.store.Shop

/**
 * Created by anlonglong on 2018/6/14.
 * Email： 940752944@qq.com
 */

private const val ID = "id"
private const val ID_SORT_TYPE = "id,sort_type"
private const val PAGECOUNT = 12
class HomeViewModel:ViewModel() {



    private val error = MutableLiveData<BmobException>()
    /**
     * 之所以用Transformations.switchMap来创建出错时候的LiveData，是因为如果已经有可活动的观察者的话，就不会再重复的触发UI层的观察者，
     * 这样可以减少在UI层的重复调用
     * 如果直接创建一个错误LiveData观察的话，每次数据出错都会触发UI，增加UI层的压力
     */
    private val _errorObserver = Transformations.switchMap(error,{ getErrorLiveData(it) })!!
    val errorObserver: LiveData<BmobException>?
        get() = _errorObserver

    private fun getErrorLiveData(e:BmobException):LiveData<BmobException> {
        val data = MutableLiveData<BmobException>()
        data.value = e
        return data
    }

    fun requestBanner(onSuccess:(bannerList:List<Banner>) ->Unit){
        val bannerQuery = bmobQuery<Banner>()
        bannerQuery.order(ID)
        bannerQuery.findObjects(object : FindListener<Banner>(){
            override fun done(bannerList: List<Banner>?, e: BmobException?) {
                when(e){
                    null ->{onSuccess(bannerList!!)}
                    else ->{ error.value = e }
                }
            }

        })
    }


    fun queryCenterData(onSuccess:(list:List<Sort>) ->Unit){
        val sortQuery = bmobQuery<Sort>()
        sortQuery.order(ID_SORT_TYPE)
        sortQuery.findObjects(object : FindListener<Sort>(){
            override fun done(list: List<Sort>?,e: BmobException?) = when(e) {
                null ->{onSuccess(list!!)}
                else ->{ error.value = e}
            }

        })
    }

    private  fun <T> bmobQuery(): BmobQuery<T> {
        val bannerQuery = BmobQuery<T>()
        bannerQuery.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        bannerQuery.setLimit(80)
        return bannerQuery
    }

    private var lastRequestPage = 0
    fun requestShopData(onSuccess:(shopList:List<Shop>) ->Unit){
        val shopQuery = bmobQuery<Shop>()
        shopQuery.setSkip(lastRequestPage*PAGECOUNT)
        shopQuery.order(ID)
        shopQuery.findObjects(object : FindListener<Shop>(){
            override fun done(shopList: List<Shop>?, e: BmobException?) = when(e){
                null ->{
                    lastRequestPage++
                    onSuccess(shopList!!)
                }
                else ->{ error.value = e }
            }
        })
    }

    /**
     * onSuccess()&onError()可以代理回调接口，
     * 以前我们的写法是定义一个接口，
     * interface ResultListener{
     * onSuccess(bannerList:List<Banner>)
     * onError(msg:String,errorCode:Int)
     * }
     *然后在requestBanner方法上注册进监听，
     * 现在我们直接用高阶函数来代替回调函数
     * **/
    private fun requestBanner(onSuccess:(bannerList:List<Banner>) ->Unit,onError:(String,Int) ->Unit){
        val bannerQuery = bmobQuery<Banner>()
        bannerQuery.order(ID)
        bannerQuery.findObjects(object : FindListener<Banner>(){
            override fun done(bannerList: List<Banner>?, e: BmobException?) {
                when(e){
                    null ->{onSuccess(bannerList!!)}
                    else ->{
                        error.value = e
                        onError(e.message!!,e.errorCode)
                    }
                }
            }

        })
    }

    private fun requestShopData(onSuccess:(shopList:List<Shop>) ->Unit,onError:(String,Int) ->Unit){
        val shopQuery = bmobQuery<Shop>()
        shopQuery.setSkip(lastRequestPage*PAGECOUNT)
        shopQuery.order(ID)
        shopQuery.findObjects(object : FindListener<Shop>(){
            override fun done(shopList: List<Shop>?, e: BmobException?) = when(e){
                null ->{
                    lastRequestPage++
                    onSuccess(shopList!!)
                }
                else ->{
                    error.value = e
                    onError(e.message!!,e.errorCode)
                }
            }
        })
    }




}