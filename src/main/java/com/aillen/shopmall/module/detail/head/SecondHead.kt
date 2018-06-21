package com.aillen.shopmall.module.detail.head

import android.view.View
import android.widget.Toast
import com.aillen.shopmall.module.commen.CommentListActivity
import com.aillen.shopmall.module.detail.Comment
import com.aillen.shopmall.module.detail.DetailAdapter
import kotlinx.android.synthetic.main.second_head_layout.view.*
import org.greenrobot.eventbus.EventBus
import kotlin.properties.Delegates

/**
 * Created by anlonglong on 2018/5/7.
 * Email： 940752944@qq.com
 */
class SecondHead(private var contentView: View): IHead {


    private var mAdapter by Delegates.notNull<DetailAdapter>()
    private var mlist:ArrayList<Comment> by Delegates.notNull()

    override fun setAdapter(adapter: DetailAdapter) {
        mAdapter = adapter
        addHead()
    }

    override fun addHead() {
        mAdapter.addHeaderView(contentView,1)
    }


    override fun <T> initData(data: T) {
        mlist =   data as ArrayList<Comment>
        val comment = mlist[0]

        contentView.run {
            second_comment_count.text = "宝贝评价(${mlist.size})"
            second_user_name.text = comment.username
            second_comment.text = comment.content
            second_comment_create_time.text = comment.createdAt
        }
        initListener()
    }

    override fun initListener() {
        contentView.second_all_comment.setOnClickListener{
            EventBus.getDefault().postSticky(mlist)
            if (mlist.isEmpty())
                Toast.makeText(contentView.context,"没有更多的评论了",Toast.LENGTH_SHORT).show()
            else
                CommentListActivity.start(contentView.context,mlist)
        }
    }
}