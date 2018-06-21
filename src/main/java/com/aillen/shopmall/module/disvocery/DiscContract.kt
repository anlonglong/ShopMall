package com.aillen.shopmall.module.disvocery

import com.aillen.shopmall.base.IBasePresenter
import com.aillen.shopmall.base.IBaseView
import com.aillen.shopmall.module.main.disvocery.Find

/**
 * Created by anlonglong on 2018/4/28.
 * Email： 940752944@qq.com
 */
interface DiscContract {
    interface DiscPresenter : IBasePresenter<DiscView> {
        fun query(currentPage: Int)
    }

    interface DiscView : IBaseView {
        fun onDiscList(list: List<Find>?)
    }
}