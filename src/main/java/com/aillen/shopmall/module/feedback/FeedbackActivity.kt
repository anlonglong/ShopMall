package com.aillen.shopmall.module.feedback

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : BaseActivity(), View.OnClickListener, TextWatcher {


    companion object {
        fun start(ctx: Context) {
            val intent = Intent(ctx, FeedbackActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        iv_back.setOnClickListener(this)
        tv1.setOnClickListener(this)
        tv2.setOnClickListener(this)
        tv3.setOnClickListener(this)
        commit.setOnClickListener(this)
        et_feedback_content.addTextChangedListener(this)
    }


    private var selected1 = false
    private var selected2 = false
    private var selected3 = false
    override fun onClick(v: View) {
            when(v.id){
                R.id.iv_back ->{finish()}
                R.id.tv1 ->{
                    tv1.setBackgroundResource(if (!selected1) R.drawable.common_bg_orange_24x6 else R.drawable.round_rect_bg)
                    selected1 = !this.selected1
                    setFeedbackContent(selected1,tv1.text)
                }
                R.id.tv2 ->{
                    tv2.setBackgroundResource(if (!selected2) R.drawable.common_bg_orange_24x6 else R.drawable.round_rect_bg)
                    selected2 = !this.selected2
                    setFeedbackContent(selected2,tv2.text)
                }
                R.id.tv3 ->{
                    tv3.setBackgroundResource(if (!selected3) R.drawable.common_bg_orange_24x6 else R.drawable.round_rect_bg)
                    selected3 = !this.selected3
                    setFeedbackContent(selected3,tv3.text)
                }

                R.id.commit ->{
                    if (et_phone.text.toString().isEmpty()) {
                        toast("电话号码不能为空")
                        return
                    }
                    CommonUtils.showProgress(this,true,progress)
                    postDelayedTime({
                        CommonUtils.showProgress(this,false,progress)
                        toast("提交成功")
                        finish()
                    },1000)
                }
            }
    }

    private fun setFeedbackContent(selected:Boolean, feedbackItem:CharSequence){
        if (selected && feedbackItem !in et_feedback_content.text.toString()) {
            et_feedback_content.apply {
                append(feedbackItem)
                append(",")
            }
        }else{
            et_feedback_content.setText(et_feedback_content.text.toString().replace(feedbackItem.toString()+",",""))
        }
        et_feedback_content.setSelection(et_feedback_content.text.toString().count())
    }


    @SuppressLint("SetTextI18n")
    override fun afterTextChanged(s: Editable) {
        //字数显示的时候不用包含逗号，所以长度减一，但是在没有字的时候会显示-1，所以分两种情况，有字无字。
        word_count.text = "${(if ((s.toString().dropWhile { it == ',' }.count() - 1) < 0) 0 else s.toString().dropWhile { it.equals(other = ",") }.count() - 1)}/200"
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun getLayoutId() = R.layout.activity_feedback


}
