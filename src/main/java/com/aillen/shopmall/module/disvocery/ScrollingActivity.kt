package com.aillen.shopmall.module.disvocery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.aillen.shopmall.R
import com.aillen.shopmall.module.order.EvaluateActivity
import com.aillen.shopmall.module.store.Shop
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity() {

    companion object {
        fun start(ctx: Context) {
            val intent = Intent(ctx, ScrollingActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
    }
}
