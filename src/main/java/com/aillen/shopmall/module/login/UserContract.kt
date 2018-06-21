package com.aillen.shopmall.module.login

import com.aillen.shopmall.base.IBasePresenter
import com.aillen.shopmall.base.IBaseView
import com.aillen.shopmall.module.user.User

/**
 * Created by anlonglong on 2018/5/11.
 * Email： 940752944@qq.com
 */
class UserContract {
    interface UserPresenter : IBasePresenter<UserView> {
        fun login(account:String,passWord:String)
        fun register(account:String,passWord:String,gender:String)
    }

    interface UserView:IBaseView {
       fun onLogin(user: User) {}
       fun onRegister(user: User) {}
    }
}