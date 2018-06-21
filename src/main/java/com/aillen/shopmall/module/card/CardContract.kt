package com.aillen.shopmall.module.card

/**
 * Created by anlonglong on 2018/6/6.
 * Email： 940752944@qq.com
 */
interface CardContract {
    interface Presenter{
        fun query()
        fun queryByList(coid:List<String>)
    }

}