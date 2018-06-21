package com.aillen.shopmall.module.detail

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.aillen.shopmall.R
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder


/**
 * Created by anlonglong on 2018/5/4.
 * Email： 940752944@qq.com
 */
class DetailAdapter(list: List<String>):BaseQuickAdapter<String,BaseViewHolder>(R.layout.detail_url_iem,list) {

    override fun convert(helper: BaseViewHolder, item: String) {
        Glide.with(helper.itemView.context).load(item).into((helper.itemView.findViewById(R.id.item_image) as ImageView))
    }
    fun addFirstHead(@LayoutRes firstHeadLayoutId:Int){
        val view = View.inflate(mContext, firstHeadLayoutId, null)
        addHeaderView(view)
    }

    fun addSecondfHead(@LayoutRes secondHeadLayoutId:Int){
        val view = View.inflate(mContext, secondHeadLayoutId, null)
        addHeaderView(view,0)
    }

    fun addthirdFirstHead(@LayoutRes thirdHeadLayoutId:Int){
        val view = View.inflate(mContext, thirdHeadLayoutId, null)
        addHeaderView(view,1)
    }

    fun addFouthFirstHead(@LayoutRes fourthHeadLayoutId:Int){
        val view = View.inflate(mContext, fourthHeadLayoutId, null)
        addHeaderView(view,2)
    }

}