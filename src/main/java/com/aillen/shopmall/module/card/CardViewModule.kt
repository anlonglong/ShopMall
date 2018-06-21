package com.aillen.shopmall.module.card

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.base.BaseViewModle

/**
 * Created by anlonglong on 2018/6/6.
 * Email： 940752944@qq.com
 */
class CardViewModule(application: Application): BaseViewModle(application),CardContract.Presenter {

    val cardDatas = MutableLiveData<MutableList<Card>>()
    private  var mCoid: List<String>? = null
    private  val OBJID = "objectId"
    override fun query() {
        queryCard()
    }




    override fun queryByList(coid: List<String>) {
        this.mCoid = coid
        queryCard()
    }

    private fun queryCard() {
        val query = BmobQuery<Card>()
        query.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        query.setLimit(80)
        if (mCoid!= null) {
            query.addWhereContainedIn(OBJID, mCoid)
        }
        query.findObjects(object : FindListener<Card>() {
            override fun done(list: MutableList<Card>?, e: BmobException?) {
                when (e) {
                    null -> {
                        cardDatas.postValue(list!!)
                    }
                    else -> {
                        observerError().value = e
                    }
                }
            }
        })
    }

}