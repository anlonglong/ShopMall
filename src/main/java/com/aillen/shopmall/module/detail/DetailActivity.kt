package com.aillen.shopmall.module.detail

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.extensions.setTopDrawable
import com.aillen.shopmall.module.detail.bottomdialog.BottomDialog
import com.aillen.shopmall.module.detail.head.FirstHead
import com.aillen.shopmall.module.detail.head.SecondHead
import com.aillen.shopmall.module.detail.head.ThirdHead
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.module.store.Store
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.properties.Delegates

class DetailActivity : BaseActivity(), DetailContract.DetailView, View.OnClickListener {
    override fun addUserCartSuccess() {
        toast("加入购物车成功")
    }


    override fun addUserLoveSuccess() {
        toast("添加成功")
//        val topDrawable = resources.getDrawable(R.drawable.ic_favorite_border_black_24dp_on)
//        topDrawable.setBounds(0,0,topDrawable.intrinsicWidth,topDrawable.intrinsicWidth)
//        love.setCompoundDrawables(null,topDrawable,null,null)
        //      love.setTopDrawable(R.drawable.ic_favorite_border_black_24dp_on)
        setTopDrawable(true)
    }


    private var mAdapter: DetailAdapter by Delegates.notNull()
    private val mHeadController by lazy { HeadController(mAdapter) }
    private val mPresenter: DetailPresenter by lazy { DetailPresenter(this) }
    private var mShop by Delegates.notNull<Shop>()

//在recycle中添加三个头，这个三个头分别在不同的头类中处理（这样做的目的是，各自维护各自分数据，不用混杂在一起，让类的结构看起来清晰一些。）

     internal companion object {
        private const val SHOP = "shop"

         fun start(ctx: Context, shop: Shop) {

            val intent = Intent(ctx, DetailActivity::class.java)
            intent.putExtra(SHOP, shop)
            ctx.startActivity(intent)
            if (ctx is DetailActivity) {
                ctx.finish()
            }
        }
    }


    override fun initListener() {
        love.setOnClickListener(this)
        cart.setOnClickListener(this)
        add_to_cart.setOnClickListener(this)
        buy_now.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.love -> {
                mPresenter.addUserLove(mShop.objectId)
            }
            R.id.cart -> {
               BottomDialog.show(this)
                //DetailActivity.start(this,mShop)
            }
            R.id.add_to_cart -> {
                mPresenter.addUserCart(mShop.objectId)
            }
            R.id.buy_now -> {
               ConfirmOrderActivity.start(this,mShop, UserManager.isLogin)
            }
        }
    }

    override fun initView() {
        // CommonUtils.resetStatusBarBackground(this,android.R.color.transparent)
        mAdapter = DetailAdapter(arrayListOf())
        val firstContentView = View.inflate(this, R.layout.first_head_layout, null)
        mHeadController.addHead(FirstHead(firstContentView))
        mShop = getIntentExtra().getSerializableExtra(SHOP) as Shop
        mHeadController.setDate(mShop)
        mPresenter.queryComment(mShop.objectId)
        detail_recycle_view.layoutManager = LinearLayoutManager(this)
        detail_recycle_view.adapter = mAdapter
        setTopDrawable(UserManager.loveId.contains(mShop.objectId))

    }

    private fun setTopDrawable(isContainer: Boolean) {
        love.setTopDrawable(if (isContainer) R.drawable.ic_favorite_border_black_24dp_on else R.drawable.ic_favorite_border_black_24dp_off)
    }

    override fun getLayoutId(): Int {
        //CommonUtils.hideStateBar(this)
        return@getLayoutId R.layout.activity_detail
    }

    override fun onComment(comment: List<Comment>?) {
        val secondContentView = View.inflate(this, R.layout.second_head_layout, null)
        mHeadController.addHead(SecondHead(secondContentView))
        mHeadController.setDate(comment)
        mPresenter.queryStore(mShop.S_OID)
    }

    override fun onError(code: Int, msg: String) {
        toast(msg)
    }

    override fun onSuccess() {

    }



    override fun onStore(stores: List<Store>?) {
        val store = stores?.let { it[0] }
        val thirdContentView = View.inflate(this, R.layout.third_head_layout, null)
        mHeadController.addHead(ThirdHead(thirdContentView))
        mHeadController.setDate(WrappStore(store, mShop.S_OID))
        mAdapter.addData(mShop.detail_urls)
    }

    class WrappStore(val store: Store?, val osid: String)

}
