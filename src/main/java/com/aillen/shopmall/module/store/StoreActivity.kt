package com.aillen.shopmall.module.store

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.detail.DetailActivity
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_store.*
import kotlin.properties.Delegates

/**
 * 1.你想要的是什么
 * 2.你的目的是什么
 */

class StoreActivity : BaseActivity(), StoreContract.View {
    private val mOsid: String by lazy { getIntentExtra().getStringExtra(OSID) }

    private val mPresenter = StorePresenter(this)

    private var mAdapter:ShopAdapter by Delegates.notNull()

    override fun onShop(list: List<Shop>?) {
        mAdapter = ShopAdapter(list)
        store_list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position -> DetailActivity.start(this,adapter.data[position] as Shop) }
    }

    override fun onStore(mutableList: List<Store>?) {
        val store = mutableList?.let { it[0] }
        displayImage(store!!.img_url, iv_img)
        store_name.text = store!!.name
        CommonUtils.addRateImage(store.rate,rate_container)
        love_num.text = store!!.love_num.toString()

    }


    override fun onUser() {

    }

    override fun onError(code: Int, msg: String) {
        toast(msg)
    }

    override fun onSuccess() {
            toast("收藏成功")
    }

    companion object {
        private const val OSID: String = "osid"

        fun start(ctx: Context, osid: String?) {
            val intent = Intent(ctx, StoreActivity::class.java)
            intent.putExtra(OSID, osid)
            ctx.startActivity(intent)
        }
    }

    override fun initView() {
        val layoutManager = GridLayoutManager(this,2)
        store_list.layoutManager = layoutManager
        collection.text = if (UserManager.collectionId.contains(mOsid)) "已收藏" else "未收藏"
    }

    override fun initData() {
        mPresenter.storeQuery(mOsid)
        mPresenter.shopQuery(mOsid)
    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
        collection.setOnClickListener{mPresenter.addUserCollection(mOsid)}
    }

    override fun getLayoutId() = R.layout.activity_store

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

}
