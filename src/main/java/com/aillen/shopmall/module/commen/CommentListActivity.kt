package com.aillen.shopmall.module.commen

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.detail.Comment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_comment_list.*
import org.greenrobot.eventbus.Subscribe


class CommentListActivity : BaseActivity() {

    companion object {
        private const val COMMENTS="comment"

        fun start(ctx:Context,comments:ArrayList<Comment>){
            val intent = Intent(ctx,CommentListActivity::class.java)
            intent.putParcelableArrayListExtra(COMMENTS,comments)
            ctx.startActivity(intent)
        }
    }

    override fun enableEventBus() = true

    override fun initView() {
        iv_back.setOnClickListener { finish() }
        val linearLayoutManager = LinearLayoutManager(this)
        comment_list_recycle.layoutManager = linearLayoutManager

    }


    @Subscribe(sticky = true)
    fun onCommentList(list:ArrayList<Comment>) {
        comment_list_recycle.adapter = object : BaseQuickAdapter<Comment,BaseViewHolder>(R.layout.comment_item,list) {
            override fun convert(helper: BaseViewHolder, item: Comment) {
                helper.getView<TextView>(R.id.second_user_name).text = item.username
                helper.getView<TextView>(R.id.second_comment).text = item.content
                helper.getView<TextView>(R.id.second_comment_create_time).text = item.createdAt

            }

        }
    }


    override fun getLayoutId() = R.layout.activity_comment_list

}
