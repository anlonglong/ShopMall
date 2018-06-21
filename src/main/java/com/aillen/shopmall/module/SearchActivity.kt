package com.aillen.shopmall.module

import android.content.Context
import android.content.Intent
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.iflytek.cloud.InitListener
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    companion object {
        private const val SEARCH_CONTENT = "searchcontent"
        fun start(ctx: Context, content: String) {
            val intent = Intent(ctx, SearchActivity::class.java)
            intent.putExtra(SEARCH_CONTENT, content)
            ctx.startActivity(intent)

        }
    }

    override fun initListener() {
        tv_search.setOnClickListener { toast("此功能尚未开通！") }

    }

    override fun initView() {
        auto_comp.setText(getIntentExtra().getStringExtra(SEARCH_CONTENT))
        auto_comp.setSelection(getIntentExtra().getStringExtra(SEARCH_CONTENT).length)
    }

    override fun getLayoutId() = R.layout.activity_search

}
