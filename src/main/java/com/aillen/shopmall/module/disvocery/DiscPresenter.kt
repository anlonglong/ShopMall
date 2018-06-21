package com.aillen.shopmall.module.disvocery

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.module.main.disvocery.Find

/**
 * Created by anlonglong on 2018/4/28.
 * Email： 940752944@qq.com
 */
class DiscPresenter(override var view: DiscContract.DiscView?) : DiscContract.DiscPresenter {

    init {
        println("创建DiscPresenter")
    }
    override fun query(currentPage: Int) {
        val query: BmobQuery<Find> = BmobQuery()
        query.cachePolicy = mPolicy
        query.order("-id")
        query.setLimit(mPageCount)
        query.setSkip(currentPage*mPageCount)
        query.findObjects(object : FindListener<Find>() {
            override fun done(list: List<Find>?, e: BmobException?) {
                if (null != e) {
                    view?.onError(e.errorCode,if (e.errorCode == 9016) "无网络连接，请检查您的手机网络" else e.toString())
                }else{
                    view?.onDiscList(list)
                }
            }
        })
    }

}