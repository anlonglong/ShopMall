package com.aillen.shopmall.module.scan

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.detail.DetailActivity
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_history.*


class ScanRecordActivity : BaseActivity() {

    companion object {
        fun start(ctx:Context){
            val intent = Intent(ctx, ScanRecordActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_history

    override fun initView() {
        history_list.layoutManager = LinearLayoutManager(this)
        CommonUtils.showProgress(this,true,progress)
    }

    override fun initData() {
        val query = BmobQuery<Shop>()
        query.cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY
        query.setLimit(80)
        query.order("S_OID")
        query.addWhereContainedIn("objectId",UserManager.scanRecordIds)
        query.findObjects(object: FindListener<Shop>(){
            override fun done(list: MutableList<Shop>?, e: BmobException?) {
                CommonUtils.showProgress(this@ScanRecordActivity,false,progress)
                when(e) {
                    null ->{
                        history_empty_hint.visibility =if (list!!.isEmpty()) View.VISIBLE else View.GONE
                        val scanRecordAdapter = ScanRecordAdapter(list!!)
                        history_list.adapter = scanRecordAdapter
                        scanRecordAdapter.setOnItemClickListener{adapter, view, position ->
                            DetailActivity.start(this@ScanRecordActivity,list[position])
                        }
                    }
                    else ->{toast(e.message!!)}
                }
            }

        })
    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
    }

}
