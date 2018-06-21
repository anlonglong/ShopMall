package com.aillen.shopmall.module.collection

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.store.Store
import com.aillen.shopmall.module.store.StoreActivity
import com.aillen.shopmall.utils.CommonUtils
import com.aillen.shopmall.utils.println
import kotlinx.android.synthetic.main.activity_collection.*

class CollectionActivity : BaseActivity() {

    companion object {

            fun start(ctx: Context) {
                val intent = Intent(ctx, CollectionActivity::class.java)
                ctx.startActivity(intent)

        }
    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
    }

    override fun initView() {
        val add = add(1, 2) { a, b -> a + b }
        println(add)
        collection_list.layoutManager = LinearLayoutManager(this)
        CommonUtils.showProgress(this,true,progress)
    }

    override fun initData() {
        val query = BmobQuery<Store>()
        query.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        query.setLimit(80)
        query.addWhereContainedIn("objectId", UserManager.collectionId)
        query.findObjects(object : FindListener<Store>(){
            override fun done(list: MutableList<Store>?, e: BmobException?) {
                CommonUtils.showProgress(this@CollectionActivity,false,progress)
                when(e){
                    null ->{
                        collection_empty_hint.visibility =if (list!!.isEmpty())View.VISIBLE else View.GONE
                        val collectionAdapter = CollectionAdapter(list)
                        collection_list.adapter = collectionAdapter
                        collectionAdapter.setOnItemClickListener { adapter, view, position ->
                            StoreActivity.start(this@CollectionActivity,list[position].objectId)
                        }
                    }
                    else ->{ toast(e.message!!)}
                }
            }
        })

    }


    override fun getLayoutId() =R.layout.activity_collection

    fun add(a:Int,b:Int,result:(Int,Int) -> Int):Int = result(a,b)

}
