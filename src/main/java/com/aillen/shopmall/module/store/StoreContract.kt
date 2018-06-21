package com.aillen.shopmall.module.store

import com.aillen.shopmall.base.IBasePresenter
import com.aillen.shopmall.base.IBaseView

/**
 * Created by anlonglong on 2018/5/2.
 * Email： 940752944@qq.com
 */
interface StoreContract {
    interface Presenter<V : View>:IBasePresenter<V> {
        fun storeQuery(soid:String)
        fun shopQuery(soid:String)
        fun userQuery(soid:String)
    }

    interface View:IBaseView {
        fun onStore(mutableList : List<Store>?)
        fun onShop(list: List<Shop>?)
        fun onUser()
    }
}