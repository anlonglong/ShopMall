package com.aillen.shopmall.module.main.disvocery

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseFragment
import com.aillen.shopmall.module.disvocery.DiscAdapter
import com.aillen.shopmall.module.disvocery.DiscContract
import com.aillen.shopmall.module.disvocery.DiscPresenter
import com.aillen.shopmall.module.store.StoreActivity
import kotlinx.android.synthetic.main.fragment_discovery.*


/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
class DiscoveryFragment : BaseFragment(),DiscContract.DiscView {


    private val mPresenter: DiscPresenter by lazy { DiscPresenter(this) }
    private var mCurrentPage = 0
    private val mDiscList = mutableListOf<Find>()
    private val discAdapter = DiscAdapter(mDiscList)



    companion object {
        fun getInstance() = DiscoveryFragment()
    }

    override fun loadData(mRootView: View?) {
       discAdapter.setOnItemClickListener { _, view, position -> StoreActivity.start(view.context,mDiscList[position].S_OID) }
        srl_disc_layout.autoRefresh(200)
        val layoutManager = LinearLayoutManager(ctx)
        disc_recycle_view.layoutManager = layoutManager
        disc_recycle_view.adapter = discAdapter
        srl_disc_layout.setOnRefreshListener {
            run {
                mCurrentPage = 0
                mPresenter.query(mCurrentPage)
            }
        }
        //可以在刷新/加载更多开始的时候用handler发送一个延时消息，在规定的时间内没有收到数据，就结束刷新/加载更多，以防止界面一直处在刷新或者加载更多的界面
        srl_disc_layout.setOnLoadmoreListener{
            run {
                mPresenter.query(mCurrentPage)
                mCurrentPage++
            }

        }

    }


    fun refresh(){
        srl_disc_layout.autoRefresh()
    }

    override fun getLayoutId() = R.layout.fragment_discovery

    override fun onDiscList(list: List<Find>?) {
        //init: (index: Int) -> T
        println(if(mDiscList.containsAll(MutableList(list!!.size,{ list[it]})))" 包含数据" else "不包含数据")
        if (!mDiscList.containsAll(MutableList(list.size,{ list[it]}))) {
            try {
                when {
                    !list.isEmpty() -> {
                        when {
                            srl_disc_layout.isRefreshing  ->{
                                srl_disc_layout.finishRefresh(true)
                                mDiscList.addAll(0, list)
                             }
                            srl_disc_layout.isEnableLoadmore ->{
                                srl_disc_layout.finishLoadmore(true)
                                mDiscList.addAll(mDiscList.size, list)
                             }
                            }
                        discAdapter.addData(mDiscList)
                    }
                           else ->{
                                when{ list.isEmpty() -> {
                                resetSrlState()
                                toast("没有更多的数据了")
                        }
                        }
                    }
                }
//            //if-else的写法
//            if ((list?.isEmpty()) == true) {
//                if (srl_disc_layout.isRefreshing) {
//                    srl_disc_layout.finishRefresh(true)
//                    mDiscList.addAll(0, list!!)
//                } else if (srl_disc_layout.isEnableLoadmore) {
//                    srl_disc_layout.finishLoadmore(true)
//                    mDiscList.addAll(mDiscList.size, list!!)
//                }
//                discAdapter.addData(mDiscList)
//            }else{
//                if (list?.isEmpty() == true) {
//                    toast("没有更多的数据了")
//                }
//            }
            }catch (e:Exception){
                toast("返回来的数据可能为null=={${e.message}}")
            }
        } else {
            resetSrlState()
            toast("暂时没有更新的数据了(；′⌒`)")
        }

    }

    private fun resetSrlState() {
        when {
            srl_disc_layout.isRefreshing -> {
                srl_disc_layout.finishRefresh(true)
            }
            srl_disc_layout.isEnableLoadmore -> {
                srl_disc_layout.finishLoadmore(true)
            }
        }
    }

    override fun onError(code: Int, msg: String) {
        if (srl_disc_layout.isRefreshing) {
            srl_disc_layout.finishRefresh(false)
        }else if (srl_disc_layout.isEnableLoadmore) {
            srl_disc_layout.finishLoadmore(false)
        }
    }

    override fun onSuccess() {
        //影藏loading
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

}