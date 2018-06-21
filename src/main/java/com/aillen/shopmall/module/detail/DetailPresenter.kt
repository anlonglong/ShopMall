package com.aillen.shopmall.module.detail

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.store.Store

/**
 * Created by anlonglong on 2018/5/7.
 * Email： 940752944@qq.com
 */
private const val CREATE_ID = "createdAt"
private const val S_OID="S_OID"
class DetailPresenter(override var view : DetailContract.DetailView?):DetailContract.DetailPresenter {
    override fun addUserCart(objId: String) {
        if (!UserManager.isLogin){
            view?.onError(0x02,"请登录后再加入我的购物车")
            return
        }

        val cartIds = UserManager.cartOid
        if (cartIds.contains(objId)) {
            view?.onError(0x03,"已经加入购物车列表")
            return
        }
        val user = UserManager.getCurrentUser()?.copy(cart_oid = cartIds + objId)!!
        user.sessionToken = UserManager.sessionToken
        user.objectId = UserManager.objId
        user.update(object :UpdateListener(){
            override fun done(e: BmobException?) {
                if (null != e) {
                    view?.onError(0x42,"加入购物车失败")

                }else{
                    view?.addUserCartSuccess()
                }
            }

        })
    }

    override fun addUserLove(osid: String) {
        if (!UserManager.isLogin){
            view?.onError(0x02,"请登录后再加入我的关注")
            return
        }

        val loveIds = UserManager.loveId
        if (loveIds.contains(osid)) {
            view?.onError(0x03,"已经加入关注列表")
            return
        }
        loveIds.add(osid)
        val user = UserManager.getCurrentUser()?.copy(love_oid = loveIds)
        user?.sessionToken = UserManager.sessionToken
        user!!.objectId = UserManager.objId
        user!!.update(object :UpdateListener(){
            override fun done(e: BmobException?) {
                if (null != e) {
                    view?.onError(0x42,"关注失败:${e.message}")
                }else{
                    view?.addUserLoveSuccess()
                }
            }

        })

        user!!.updateObservable(UserManager.objId)
    }

    override fun queryStore(osid: String) {
        val query = BmobQuery<Store>()
        query.cachePolicy = mPolicy
        query.setLimit(mLimitPage)
        query.addWhereEqualTo("objectId", osid)
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

    override fun queryComment(osid: String) {
        val commenQuery = BmobQuery<Comment>()
        commenQuery.cachePolicy = mPolicy
        commenQuery.setLimit(mLimitPage)
        commenQuery.order(CREATE_ID)
        commenQuery.addWhereEqualTo(S_OID, osid)
        commenQuery.findObjects(object : FindListener<Comment>() {
            override fun done(list: List<Comment>?, e: BmobException?) {
                when (e) {
                    null -> {
                        view?.onComment(list)
                    }
                    else -> {
                        view?.onError(e.errorCode, if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                    }
                }
            }
        })
    }




}