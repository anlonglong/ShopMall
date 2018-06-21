package com.aillen.shopmall.module.main.home

import android.Manifest
import android.arch.lifecycle.*
import android.os.Build
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ImageView
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseFragment
import com.aillen.shopmall.module.QRcodeActivity
import com.aillen.shopmall.module.SearchActivity
import com.aillen.shopmall.module.WebActivity
import com.aillen.shopmall.module.home.Banner
import com.aillen.shopmall.module.home.HomeAdapter
import com.aillen.shopmall.module.home.HomeViewModel
import com.aillen.shopmall.module.permission.IPermission
import com.aillen.shopmall.module.permission.PersmissionDispatch
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.store.StoreActivity
import com.aillen.shopmall.utils.CommonUtils
import com.aillen.shopmall.utils.PERMISSION_REQUEST_CODE_RECORD_AUDIO
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_category_list_item.view.*
import kotlinx.android.synthetic.main.home_first_head_layout.view.*
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
class HomeFragment : BaseFragment(), IPermission, View.OnClickListener {


    private val persmissionDispatch by lazy { PersmissionDispatch(this) }

    companion object {
        fun getInstance() = HomeFragment()
    }

    private val eror = { msg: String, i: Int -> println("msg = [${msg}], i = [${i}]") }
    private val adapter = HomeAdapter(null)
    private var homeViewModel:HomeViewModel by Delegates.notNull()
    override fun loadData(mRootView: View?) {
        homeViewModel =  ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.errorObserver?.observe(this, Observer {
            CommonUtils.showProgress(ctx,false,home_progress)
            toast(if (it?.errorCode == 9016) getString(R.string.hint_internet_not_connected) else it?.message!!)
        })
        initBannerAndHomeCategory(homeViewModel)
        //initCenterData(homeViewModel)
        adapter.setOnItemClickListener { adapter, view, position ->
            StoreActivity.start(view.context,(adapter.data[position] as Shop).S_OID)
        }
    }

    //填充数据
    private fun initCenterData(homeViewModel: HomeViewModel) {
        homeViewModel.queryCenterData(onSuccess = {
            val centerDataView = View.inflate(this.ctx, R.layout.home_center_data_layout, null)
            adapter.addHeaderView(centerDataView)
        })
    }

    private fun initBannerAndHomeCategory(homeViewModel: HomeViewModel) {
        homeViewModel.requestBanner(onSuccess = {
            val flatMap = it.flatMap { banner -> arrayListOf<String>() + banner.img_url }
            val firstContentView = View.inflate(this.ctx, R.layout.home_first_head_layout, null)
            adapter.addHeaderView(firstContentView)
            fillBannerData(firstContentView, it, flatMap)
            fillHomeCategoryData(firstContentView)
            initShopData(homeViewModel)
        })

    }

    private fun initShopData(homeViewModel: HomeViewModel) {
        homeViewModel.requestShopData(onSuccess = {
            adapter.addData(it)
            CommonUtils.showProgress(ctx, false, home_progress)
            if (srf.isRefreshing) {
                srf.finishRefresh()
                home_recycle_view.smoothScrollToPosition(0)
            }
        })
    }


    private fun fillHomeCategoryData(firstContentView: View) {
        val homecates = arrayListOf<HomeCatreBen>()
        val titles = arrayOf("我的关注", "物流查询", "充值", "电影票", "游戏充值", "领卡券", "领金豆", "更多")
        val icons = arrayOf(R.drawable.home_mid_ic_menu_wdgz, R.drawable.home_mid_ic_menu_wlcx, R.drawable.home_mid_ic_menu_cz,
                R.drawable.home_mid_ic_menu_dyp, R.drawable.home_mid_ic_menu_yxcz, R.drawable.home_mid_ic_menu_lkq,
                R.drawable.home_mid_ic_menu_ljd, R.drawable.home_mid_ic_menu_gd)
        for (i in titles.indices) {
            homecates.add(HomeCatreBen(titles[i], icons[i]))
        }
        firstContentView.home_first_category_list.layoutManager = GridLayoutManager(ctx, 4)
        val adapter: BaseQuickAdapter<HomeCatreBen, BaseViewHolder> = object : BaseQuickAdapter<HomeCatreBen, BaseViewHolder>(R.layout.home_category_list_item, homecates) {
            override fun convert(holder: BaseViewHolder, item: HomeCatreBen) {
                holder.itemView.iv_src.setImageResource(item.iconUrl)
                holder.itemView.tv_title.text = item.title
            }
        }
        firstContentView.home_first_category_list.adapter = adapter
        adapter.setOnItemClickListener {_,_,_ -> toast("此功能未开启") }

    }

    private fun fillBannerData(firstContentView: View, it: List<Banner>, flatMap: List<String>) {
        firstContentView.home_first_banner.setDelegate { _, _, _, position -> WebActivity.start(this.activity, it[position].go_url) }
        firstContentView.home_first_banner.setAdapter { _, view, imageUrl, _ -> Glide.with(view.context).load(imageUrl).into(view as ImageView) }
        firstContentView.home_first_banner.setData(R.layout.banner_item, flatMap, null)
    }

    class HomeCatreBen(val title: String, val iconUrl: Int)

    override fun initView() {
        //test()
        srf.isEnableLoadmore = false
        home_recycle_view.layoutManager = GridLayoutManager(this.ctx, 2)
        home_recycle_view.adapter = adapter
    }


    private fun test() {
        val liveData = MutableLiveData<String>()

        val switchMap = Transformations.switchMap(liveData, {
            getLived(it)//这里只能观察到getLived的LiveData发送的数据
        })

        switchMap.observe(this, Observer {
            toast(it!!)
        })
        liveData.value = "kkkkkkkkkkkkkk"
    }


    private fun getLived(input: String?): LiveData<String> {
        val liveData1 = MutableLiveData<String>()
        liveData1.value = input
        return liveData1
    }

    override fun intListener() {
        iv_talk.setOnClickListener(this)
        tv_qr_scan.setOnClickListener(this)
        tv_search_shop_and_goods.setOnClickListener(this)
        srf.setOnRefreshListener {
            CommonUtils.showProgress(ctx,true,home_progress)
            adapter.removeAllHeaderView()
            adapter.replaceData(mutableListOf())
            loadData(null)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_talk -> {
                hasRecordAutoPermission()
            }
            R.id.tv_qr_scan -> {
                QRcodeActivity.start(v.context, false)
            }
            R.id.tv_search_shop_and_goods -> {
                SearchActivity.start(v.context, "")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE_RECORD_AUDIO -> {
                persmissionDispatch.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun hasRecordAutoPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CommonUtils.showRecognizerDialog(this.activity)
        } else {
            persmissionDispatch.checkPermissionFragment(PERMISSION_REQUEST_CODE_RECORD_AUDIO, this, "去获取麦克风权限", Manifest.permission.RECORD_AUDIO)
        }
    }

    override fun hasPermission(requestCode: Int, vararg permission: String) {
        CommonUtils.showRecognizerDialog(this.activity)
    }

    override fun permissionDenied(requestCode: Int, vararg permission: String) {
        println("requestCode = [$requestCode], permission = [$permission]")
        toast("没有录音权限，某些功能无法正常使用╮(╯_╰)╭")
    }

    override fun onNeverAskAgain(requestCode: Int, vararg permission: String) {
        toast("没有录音权限，某些功能无法正常使用╮(╯_╰)╭")
    }


    override fun getLayoutId() = R.layout.fragment_home
}