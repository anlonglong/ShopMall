package com.aillen.shopmall.module.store

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.aillen.shopmall.module.user.UserManager

/**
 * Created by anlonglong on 2018/5/2.
 * Email： 940752944@qq.com
 */
class StorePresenter(override var view: StoreContract.View?) : StoreContract.Presenter<StoreContract.View> {

    override fun storeQuery(soid: String) {
        val query = BmobQuery<Store>()
        query.cachePolicy = mPolicy
        query.setLimit(mLimitPage)
        query.addWhereEqualTo("objectId", soid)
        query.findObjects(object : FindListener<Store>() {
            override fun done(mutableList: List<Store>?, e: BmobException?) {
                when (e) {       
                    null -> {
                        view?.onStore(mutableList)
                    }
                    else -> {
                        view?.onError(e.errorCode, if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                    }
                }
            }

        })
    }


    override fun shopQuery(soid: String) {
        val query = BmobQuery<Shop>()
        query.cachePolicy = mPolicy
        query.setLimit(mLimitPage)
        query.order("id")
        query.addWhereEqualTo("S_OID", soid)
        query.findObjects(object : FindListener<Shop>() {
            override fun done(list: List<Shop>?, e: BmobException?) {
                when (e) {
                    null -> {
                        view?.onShop(list)
                    }
                    else -> {
                        view?.onError(e.errorCode, if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                    }
                }
            }
        })
    }

    override fun userQuery(soid: String) {

    }

    fun storeQueryByListId(soids:List<String>){
        val query = BmobQuery<Store>()
        query.cachePolicy = mPolicy
        query.setLimit(mLimitPage)
        query.addWhereEqualTo("objectId", soids)
        query.findObjects(object : FindListener<Store>() {
            override fun done(mutableList: List<Store>?, e: BmobException?) {
                when (e) {
                    null -> {
                        view?.onStore(mutableList)
                    }
                    else -> {
                        view?.onError(e.errorCode, if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                    }
                }
            }

        })
    }


    fun addUserCollection(objectId:String){
        if (!UserManager.isLogin){
            view?.onError(0x00,"请登录后再加入我的收藏")
            return
        }

        val collectionIds = UserManager.collectionId
        if (collectionIds.contains(objectId)) {
           view?.onError(0x02,"已经加入我的收藏列表")
           return
       }
        UserManager.getCurrentUser()
        val user =  UserManager.getCurrentUser()?.copy(collection_oid = collectionIds + objectId)
        user?.update(object : UpdateListener(){
            override fun done(e: BmobException?) {
                when (e) {
                    null -> {
                        view?.onSuccess()
                    }
                    else -> {
                        view?.onError(e.errorCode, if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                    }
                }
            }
        })

    }


}