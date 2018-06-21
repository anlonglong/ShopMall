package com.aillen.shopmall.module.user

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.repository.Address
import com.aillen.shopmall.repository.DAOManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlin.properties.Delegates

class AddressListActivity : BaseActivity(), View.OnClickListener {

    var position: Int by Delegates.notNull()

    companion object {
        fun start(ctx: Context) = ctx.startActivity(Intent(ctx, AddressListActivity::class.java))
    }

    override fun initData() {
        val query = DAOManager.get(this).addressDao().query(UserManager.userName)
        val adapter = object : BaseQuickAdapter<Address, BaseViewHolder>(R.layout.address_list_item, query) {

            override fun convert(helper: BaseViewHolder, item: Address) {
                helper.getView<TextView>(R.id.tv_realname).text = item.realName
                helper.getView<TextView>(R.id.tv_address).text = item.address
                helper.getView<TextView>(R.id.tv_phone).text = item.phone
                helper.getView<CheckBox>(R.id.cb_isdefault).isChecked = item.isDefault!!
                helper.getView<TextView>(R.id.tv_isdefault).text = if (item.isDefault!!) "默认地址" else "设为默认"
                helper.getView<TextView>(R.id.tv_isdefault).setTextColor(if (item.isDefault!!) Color.parseColor("#FF6622") else Color.parseColor("#333333"))
                helper.addOnClickListener(R.id.ly_edit)
                helper.addOnClickListener(R.id.ly_delete)
                helper.setOnCheckedChangeListener(R.id.cb_isdefault, { _, _ -> run {
                    if((address_list.scrollState == RecyclerView.SCROLL_STATE_IDLE
                                    && !address_list.isComputingLayout)) {
                        helper.getView<TextView>(R.id.tv_isdefault).text = "默认地址"
                        selectedDefault(this, helper.adapterPosition)
                    }
                } })
            }
        }
        address_list.adapter = adapter
        adapter.setOnItemChildClickListener { adapter, view, position ->
            run {
                when (view.id) {
                    R.id.ly_edit -> {
                        AddAddressActivity.start(this, adapter.getItem(position) as Address)
                    }

                    R.id.ly_delete -> {
                        DAOManager.get(this).addressDao().delete(adapter.getItem(position) as Address)
                        adapter.remove(position)
                    }
                }
            }
        }
    }

    private fun selectedDefault(baseQuickAdapter: BaseQuickAdapter<Address, BaseViewHolder>, adapterPosition: Int) {
        for (i in baseQuickAdapter.data.indices) {
            baseQuickAdapter.data[i].isDefault = (i == adapterPosition)
            DAOManager.get(this).addressDao().update(baseQuickAdapter.data[i])
        }
        baseQuickAdapter.notifyDataSetChanged()
    }


    override fun initView() {
        address_list.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    override fun onRestart() {
        super.onRestart()
        initData()
    }


    override fun initListener() {
        iv_back.setOnClickListener(this)
        add_location.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.add_location -> {
                AddAddressActivity.start(this, null)
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_address_list


}
