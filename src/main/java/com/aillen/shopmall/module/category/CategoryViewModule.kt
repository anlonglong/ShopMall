package com.aillen.shopmall.module.category

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.base.BaseApplication
import com.aillen.shopmall.base.BaseViewModle

/**
 * Created by anlonglong on 2018/6/11.
 * Email： 940752944@qq.com
 */
class CategoryViewModule: BaseViewModle(BaseApplication.getApplication()) {

    fun query():LiveData<List<Category>>{
        val cateData = MutableLiveData<List<Category>>()
        val query = BmobQuery<Category>()
        query.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        with(query) {
            setLimit(80)
            order("id")
            findObjects(object : FindListener<Category>(){
            override fun done(list: MutableList<Category>?, e: BmobException?) {
               when(e) {
                   null ->{cateData.value = list}
                   else ->{observerError().value = e}
               }
            }
        })
        }
        return cateData
    }
}