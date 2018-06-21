package com.aillen.shopmall.module.home

import android.view.View
import android.widget.ImageView
import com.aillen.shopmall.R
import com.aillen.shopmall.module.detail.DetailAdapter
import com.aillen.shopmall.module.detail.head.IHead
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.first_head_layout.view.*
import kotlinx.android.synthetic.main.home_first_head_layout.view.*
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/6/14.
 * Email： 940752944@qq.com
 */
class HomeFirstHead(contentView: View):IHead{

    private var view by Delegates.notNull<View>()
    private var mAdapter by Delegates.notNull<DetailAdapter>()
    private var bannList:List<String> by Delegates.notNull()
    init {
        view = contentView
    }
    override fun setAdapter(adapter: DetailAdapter) {
        mAdapter = adapter
        addHead()
    }

    override fun addHead() {
        mAdapter.addHeaderView(view)
    }

    override fun <T> initData(data: T) {
        bannList = data as List<String>
        view.home_first_banner.setAdapter { _, view, imageUrl, _ ->
            Glide.with(view.context).load(imageUrl).into(view as ImageView)
        }
        view.home_first_banner.setData(R.layout.banner_item, bannList, null)
    }

    override fun initListener() {

    }

}