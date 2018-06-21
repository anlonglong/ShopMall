package com.aillen.shopmall.module.category

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.left_fragment.*
import android.support.v7.widget.DividerItemDecoration



/**
 * Created by anlonglong on 2018/6/10.
 * Email： 940752944@qq.com
 */
class LeftFragment:BaseFragment() {

    override fun initView() {
        left_list.layoutManager = LinearLayoutManager(ctx)
        left_list.addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))
    }

    private var clickPosi=0
    override fun loadData(mRootView: View?) {
        val data = listOfNotNull("潮流男装", "数码家电", "潮流女装", "文娱运动", "潮流鞋品", "儿童用品", "美妆配饰", "母婴频道")
        BridgeViewModule.data.value = Pair(data[0],0)
        val adapter: BaseQuickAdapter<String, BaseViewHolder> = object : BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1, data) {

            override fun convert(holder: BaseViewHolder, item: String) {
                holder.getView<TextView>(android.R.id.text1).text = item
                if (holder.layoutPosition == clickPosi){
                    holder.itemView.setBackgroundColor(Color.WHITE)
                    holder.getView<TextView>(android.R.id.text1).setTextColor(Color.RED)
                }else{
                    holder.itemView.setBackgroundColor(0xe2e1e2)
                    holder.getView<TextView>(android.R.id.text1).setTextColor(Color.BLACK)
                }
            }

        }
        left_list.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            BridgeViewModule.data.value = Pair(data[position],position)
            clickPosi = position
            adapter.notifyDataSetChanged()
        }

    }

    override fun getLayoutId() = R.layout.left_fragment


}