package com.aillen.shopmall.module.user

import cn.bmob.v3.BmobUser
import com.aillen.shopmall.base.BaseApplication
import com.aillen.shopmall.repository.DelegateExt

/**
 * Created by anlonglong on 2018/5/10.
 * Email： 940752944@qq.com
 */

private const val LOGIN = "login"

object UserManager {

    var isLogin: Boolean by DelegateExt.preference(BaseApplication.getApplication(), LOGIN, false)

    /**
    没登录的时候返回null
     */
    fun getCurrentUser(): User? = BmobUser.getCurrentUser(User::class.java)

    val objId: String?
        get() {
            return getCurrentUser()?.objectId
        }

    val sessionToken:String?
    get() {
        return if (isLogin) getCurrentUser()!!.sessionToken else null
    }

    val userName: String
        get() = getCurrentUser()?.username!!

    val loveId: MutableList<String>
        get() {
            return if (isLogin) {
                getCurrentUser()!!.love_oid.toMutableList()
            } else {
                arrayListOf()
            }
        }

    val collectionId:List<String>
    get() = getCurrentUser()?.collection_oid?: arrayListOf()

    val cartOid:List<String>
    get() = getCurrentUser()?.cart_oid?: arrayListOf()

    val scanRecordIds:List<String>
        get() = getCurrentUser()?.scan_record_oid?: arrayListOf()

}