package com.aillen.shopmall.module.login

import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.SaveListener
import com.aillen.shopmall.module.user.User

/**
 * Created by anlonglong on 2018/5/11.
 * Email： 940752944@qq.com
 */
class UserPresenter(override var view: UserContract.UserView?) : UserContract.UserPresenter {

    private var isLoginInProgress = false

    override fun login(account: String, passWord: String) {
        if (isLoginInProgress) {
            com.aillen.shopmall.utils.println("has request login in the progress")
            return
        }
        isLoginInProgress = true
        BmobUser.loginByAccount(account, passWord, object : LogInListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                when (e) {
                    null -> {
                        view!!.onLogin(user!!)
                        isLoginInProgress = false
                    }
                    else -> {
                        view!!.onError(e.errorCode, if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                    }
                }
            }

        })
    }

    override fun register(userName: String, passWord: String, gender: String) {
        val user = User(gender == "男", 0, 1)
        user.username = userName
        user.setPassword(passWord)
        user.signUp(object : SaveListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                when (e) {
                    null -> {
                        view?.onRegister(user!!)
                    }
                    else -> {
                        view?.onError(e.errorCode, if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                    }
                }
            }

        })
    }

}