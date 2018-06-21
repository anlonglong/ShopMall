package com.aillen.shopmall.module.detail

import com.aillen.shopmall.base.IBasePresenter
import com.aillen.shopmall.base.IBaseView
import com.aillen.shopmall.module.store.Store

/**
 * Created by anlonglong on 2018/5/7.
 * Email： 940752944@qq.com
 */
interface DetailContract {
    interface DetailPresenter:IBasePresenter<DetailView>{
       fun queryComment(osid:String)
       fun queryStore(osid:String)
        fun addUserLove(osid:String)
        fun addUserCart(objId:String)
    }

    interface DetailView:IBaseView {
        fun onComment(comment: List<Comment>?)
        fun onStore(store:List<Store>?)
        fun addUserLoveSuccess()
        fun addUserCartSuccess()
    }
}