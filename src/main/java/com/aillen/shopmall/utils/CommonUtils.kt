package com.aillen.shopmall.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.support.annotation.ColorRes
import android.support.annotation.Nullable
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.aillen.shopmall.R
import com.aillen.shopmall.module.SearchActivity
import com.aillen.shopmall.module.category.VoiceBean
import com.aillen.shopmall.module.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.iflytek.cloud.InitListener
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import org.jetbrains.anko.AlertDialogBuilder
import java.text.MessageFormat
import java.util.*

/**
 * Created by anlonglong on 2018/5/4.
 * Email： 940752944@qq.com
 */
object CommonUtils {

    fun resetStatusBarBackground(activity: Activity, @ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(color)
        }
    }




    fun hideStateBar(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun dp2Px(ctx: Context, dp: Int) = (ctx.resources.displayMetrics.density * dp + 0.5).toInt()


    fun addRateImage(rate: Int, container: LinearLayout) {
        if (rate == -1) {
            if (container.childCount != 0) {
                container.removeAllViews()
            }
            return@addRateImage
        }

        var rt = rate
        val p: Pair<Int, Int> = when (rate) {
            in 0..5 -> Pair(R.drawable.detail_mid_ic_rate_red, rt)
            in 5..10 -> {
                rt -= 5
                Pair(R.drawable.detail_mid_ic_rate_cap, rt)
            }
            in 10..15 -> {
                rt -= 10
                Pair(R.drawable.detail_mid_ic_rate_cap, rt)
            }
            else -> Pair(-1, -1)
        }
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 0, DensityUtil.dp2px(5f), 0)
        loop@ for (i in 1..p.second) {
            if (p.second == -1) return@loop
            val rateImg = ImageView(container.context.applicationContext)
            rateImg.layoutParams = lp
            rateImg.setImageResource(p.first)
            container.addView(rateImg)
        }
    }


    fun disPlayImageWithGlide(ctx: Context, url: String, view: ImageView) = Glide.with(ctx).load(url).into(view)


    fun fillHtmlString(ctx: Context, stringResID:Int, vararg args: Any) = Html.fromHtml(String.format(ctx.resources.getString(stringResID), *args))


    fun fillFormatString(ctx: Context, strResId: Int, vararg args: Any): String = ctx.resources.getString(strResId, *args)
    fun getString(ctx: Context, strResId: Int): String = ctx.resources.getString(strResId)

    fun formatString(ctx: Context, strResId: Int,vararg args: Any): String {
        val string = getString(ctx, strResId)
        return MessageFormat.format(string, *args)
    }


    fun formatDate(ctx: Context, strResId: Int):String{
        val string = getString(ctx, strResId)
        val temp = MessageFormat(string, Locale.CHINA)
        return temp.format(arrayOf(Date()))
    }


     fun showProgress(ctx:Context,show: Boolean,progressBar: ProgressBar,@Nullable viewGroup: ViewGroup? = null) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = ctx.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            viewGroup?.visibility = if (show) View.GONE else View.VISIBLE
            viewGroup?.animate()
                    ?.setDuration(shortAnimTime)
                    ?.alpha((if (show) 0 else 1).toFloat())
                    ?.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            viewGroup.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            progressBar.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            progressBar.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            viewGroup?.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    fun showLoginDialog(ctx: Context){
        AlertDialogBuilder(ctx).apply {
            title("提示")
            message("未登录，是否去登录页面")
            positiveButton("确定",callback = { LoginActivity.start(ctx)})
            negativeButton("取消",callback = {dismiss()})
            show()
        }
    }

    fun getOrderNumber():String{
        val machinedId = 1
        var hashCode = UUID.randomUUID().toString().hashCode()
        if (hashCode < 0) hashCode = -hashCode
        return machinedId.toString() + String.format("%010d",hashCode)
    }

    fun vibrate(ctx: Context,time:Long){
        val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(time)
    }


    fun showRecognizerDialog(activity: Activity){
        val recognizerDialog = RecognizerDialog(activity, InitListener {
            com.aillen.shopmall.utils.println("it = $it")
        })
        with(recognizerDialog) {
            setParameter(SpeechConstant.LANGUAGE,"zh_cn")
            setParameter(SpeechConstant.ACCENT,"mandarin")
            setListener(object : RecognizerDialogListener {
                override fun onResult(result: RecognizerResult?, isLast: Boolean) {
                    if (isLast) return
                    kotlin.io.println("result = " + result?.resultString)
                    val gson = Gson()
                    val voiceBean = gson.fromJson(result?.resultString, VoiceBean::class.java)
                    val joinToString = voiceBean.ws.joinToString(separator = "") {
                        it.cw[0].w
                    }
                    com.aillen.shopmall.utils.println("joinToString = $joinToString")
                    SearchActivity.start(activity,joinToString)
                }

                override fun onError(e: SpeechError?) {
                    println(e?.message)
                }

            })
        }
        recognizerDialog.show()
    }
}