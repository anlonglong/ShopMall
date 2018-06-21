package com.aillen.shopmall.module.category

import android.arch.lifecycle.Observer
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseFragment
import com.aillen.shopmall.module.WebActivity
import com.aillen.shopmall.utils.CommonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.category_list_item.view.*
import kotlinx.android.synthetic.main.right_fragment.*

/**
 * Created by anlonglong on 2018/6/10.
 * Email： 940752944@qq.com
 */
class RightFragment:BaseFragment() {



    override fun initView() {
        CommonUtils.showProgress(ctx,true,progress)
        right_list.layoutManager = GridLayoutManager(ctx,3)
    }

    override fun loadData(mRootView: View?) {
        BridgeViewModule.data.observe(this, Observer<Pair<String,Int>> {
            val p = it!!
            val categoryViewModule = CategoryViewModule()
            categoryViewModule.query().observe(this, Observer {
                val mutableList = it!!.filter { it.sort == p.second }
                CommonUtils.showProgress(ctx,false,progress)
                val adapter: BaseQuickAdapter<Category, BaseViewHolder> = object : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.category_list_item, mutableList) {
                    override fun convert(holder: BaseViewHolder, item: Category) {
                        CommonUtils.disPlayImageWithGlide(ctx,item.img_url,holder.itemView.iv_src)
                        holder.itemView.tv_title.text = item.name
                    }
                }
                right_list.adapter = adapter
                adapter.setOnItemClickListener { _, view, position -> WebActivity.start(view.context,mutableList[position].go_url) }
            })
            categoryViewModule.observerError().observe(this, Observer {
                CommonUtils.showProgress(ctx,false,progress)
                if (it!!.errorCode == 9016) {
                    toast("无网络连接，请检查您的手机网络")
                }else{
                    toast(it.message!!)
                }
            })

        })
    }

    override fun getLayoutId() = R.layout.right_fragment
}