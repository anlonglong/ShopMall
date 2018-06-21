package com.aillen.shopmall.module.main.category

import android.Manifest
import android.os.Build
import android.os.Message
import android.view.View
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseFragment
import com.aillen.shopmall.module.QRcodeActivity
import com.aillen.shopmall.module.SearchActivity
import com.aillen.shopmall.module.permission.IPermission
import com.aillen.shopmall.module.permission.PersmissionDispatch
import com.aillen.shopmall.utils.CommonUtils
import com.aillen.shopmall.utils.PERMISSION_REQUEST_CODE_RECORD_AUDIO
import com.iflytek.cloud.InitListener
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import kotlinx.android.synthetic.main.fragment_category.*

/**
 * Created by anlonglong on 2018/4/26.
 * Email： 940752944@qq.com
 */
class CategoryFragment : BaseFragment(), View.OnClickListener ,IPermission{


    private val persmissionDispatch by lazy { PersmissionDispatch(this) }

    private fun hasRecordAutoPermission(){
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.M) {
            CommonUtils.showRecognizerDialog(this.activity)
        }else{
            persmissionDispatch.checkPermissionFragment(PERMISSION_REQUEST_CODE_RECORD_AUDIO,this,"去获取麦克风权限", Manifest.permission.RECORD_AUDIO)
        }
    }

    companion object {
        fun getInstance() = CategoryFragment()
    }

    override fun loadData(mRootView: View?) {

    }

    override fun intListener() {
        iv_qrscan.setOnClickListener(this)
        tv_search_shop_and_goods.setOnClickListener(this)
        iv_voice.setOnClickListener(this)
    }



    override fun onClick(v: View) {
        when(v.id) {
            R.id.iv_qrscan -> QRcodeActivity.start(v.context,false)
            R.id.tv_search_shop_and_goods ->{ SearchActivity.start(v.context,"") }
            R.id.iv_voice ->{
                hasRecordAutoPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE_RECORD_AUDIO ->{
                persmissionDispatch.onRequestPermissionsResult(requestCode,permissions,grantResults)
            }
        }
    }

    override fun hasPermission(requestCode: Int, vararg permission: String) {
           CommonUtils.showRecognizerDialog(this.activity)
    }

    override fun permissionDenied(requestCode: Int, vararg permission: String) {
        println("requestCode = [$requestCode], permission = [$permission]")
    }

    override fun onNeverAskAgain(requestCode: Int, vararg permission: String) {
        toast("没有录音权限，某些功能无法正常使用╮(╯_╰)╭")
    }

    override fun getLayoutId() = R.layout.fragment_category
}