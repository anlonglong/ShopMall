package com.aillen.shopmall.module.detail.head

import android.text.Html
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.module.detail.Comment
import com.aillen.shopmall.module.detail.DetailActivity
import com.aillen.shopmall.module.detail.DetailAdapter
import com.aillen.shopmall.module.store.Store
import com.aillen.shopmall.module.store.StoreActivity
import com.aillen.shopmall.utils.CommonUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.third_head_layout.view.*
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/5/8.
 * Email： 940752944@qq.com
 */
class ThirdHead(private var tHeadView: View):IHead {

    private var mAdapter by Delegates.notNull<DetailAdapter>()
    private var mlist:ArrayList<Comment> by Delegates.notNull()
    private var mStore by Delegates.notNull<Store>()
    private var mSoid:String? = null

    override fun setAdapter(adapter: DetailAdapter) {
        this.mAdapter = adapter
        addHead()
    }

    override fun addHead() {
        mAdapter.addHeaderView(tHeadView,2)
    }

    override fun <T> initData(data: T) {
        val wps = data as DetailActivity.WrappStore
        mSoid = wps.osid
        val store = wps.store
        this.mStore = store!!
        tHeadView.run {
            Glide.with(tHeadView.context).load(store.img_url).into(third_store_img)
            third_store_name.text = store.name
            CommonUtils.addRateImage(store.rate,third_rate_container)
            third_all_shop.text = Html.fromHtml(getHtmlString(num = store.all_shop))
            third_love_num.text = Html.fromHtml(getHtmlString(num = store.love_num,str = "关注人数"))
            shop_grade.text = Html.fromHtml(getHtmlString(d = store.shop_grade))
            store_grade.text = Html.fromHtml(getHtmlString(str = "卖家服务",d = store.shop_grade))
            delivery_grade.text = Html.fromHtml(getHtmlString(str = "物流服务",d = store.delivery_grade))
        }

        initListener()

    }

    private fun getHtmlString(stringResID:Int =R.string.third_item, num:Int,str:String = "全部宝贝") = String.format(tHeadView.context.resources.getString(stringResID),num,str)
    private fun getHtmlString(stringResID:Int =R.string.third_service,str:String = "宝贝描述", d:Double) = String.format(tHeadView.context.resources.getString(stringResID),str,d)


    override fun initListener() {
        tHeadView.run {
            third_category.setOnClickListener { StoreActivity.start(tHeadView.context,mSoid) }
            look_look.setOnClickListener { StoreActivity.start(tHeadView.context,mSoid) }
        }

    }
}