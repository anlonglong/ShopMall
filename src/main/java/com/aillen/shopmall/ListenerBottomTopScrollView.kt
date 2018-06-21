package com.aillen.shopmall

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * Created by anlonglong on 2018/5/3.
 * Email： 940752944@qq.com
 */
class ListenerBottomTopScrollView(ctx:Context, attrs: AttributeSet?, defStyleAttr:Int):ScrollView(ctx,attrs,defStyleAttr) {

    var mListener:OnScrollToBottomAndOrListener? = null

    constructor(ctx: Context):this(ctx,null)

    constructor(ctx: Context,attrs: AttributeSet?):this(ctx,attrs,-1)

    fun setOnScrollToBottomOrTopListener(listener:OnScrollToBottomAndOrListener?) {
        this.mListener = listener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (scrollY == 0) {
            mListener?.onScrollToTop()
        }

        if (getChildAt(0).height ==scrollY+height-paddingBottom - paddingTop) {
            mListener?.onScrollToBottom()
        }
        println("-----------------------------------------------------------")
        println("l = [$l], t = [$t], oldl = [$oldl], oldt = [$oldt]")
        println("ListenerScrollViewHeight = $height")
        println("computeVerticalScrollRange = ${computeVerticalScrollRange()}")
        println("childHeight = ${getChildAt(0).height}")
        println("computeVerticalScrollOffset = ${computeVerticalScrollOffset()}")
        println("getScrollY() = $scrollY")
        println("paddingBottom() = $paddingBottom")
        println("paddingTop() = $paddingTop")
        println("-----------------------------------------------------------")
    }

    interface OnScrollToBottomAndOrListener {
        fun onScrollToBottom()
        fun onScrollToTop()
    }
}