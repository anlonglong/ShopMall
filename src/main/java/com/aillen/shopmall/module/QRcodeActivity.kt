package com.aillen.shopmall.module

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.asynwork.AsynResultListener
import com.aillen.shopmall.module.asynwork.Result
import com.aillen.shopmall.module.asynwork.TaskRunnable
import com.aillen.shopmall.module.asynwork.ThreadHolder
import com.aillen.shopmall.module.permission.IPermission
import com.aillen.shopmall.module.permission.PersmissionDispatch
import com.aillen.shopmall.utils.CommonUtils
import com.aillen.shopmall.utils.PERMISSION_REQUEST_CODE_CAMERA
import kotlinx.android.synthetic.main.activity_qrcode.*
import me.iwf.photopicker.PhotoPicker
import me.iwf.photopicker.PhotoPreview
import java.util.concurrent.Executors


class QRcodeActivity : BaseActivity(), QRCodeView.Delegate, IPermission, View.OnClickListener, AsynResultListener<String> {

    private val persmissionDispatch by lazy { PersmissionDispatch(this) }
    private var isFlashOn = false
    private val isBarCode by lazy { getIntentExtra().getBooleanExtra(IS_BARCODE, false) }

    companion object {
        private const val IS_BARCODE = "isBarcode"
        fun start(ctx: Context, isBarcode: Boolean) {
            val intent = Intent(ctx, QRcodeActivity::class.java)
            intent.putExtra(IS_BARCODE, isBarcode)
            ctx.startActivity(intent)

        }
    }


    override fun initView() {
        persmissionDispatch.checkPermissionForActivity(PERMISSION_REQUEST_CODE_CAMERA, this, "去获取相机权限", Manifest.permission.CAMERA)
        zxingview.setDelegate(this)
        tv_bar_code.text = if (!isBarCode) "条形码" else "二维码"
        if (isBarCode) {
            zxingview.changeToScanBarcodeStyle()
        } else {
            zxingview.changeToScanQRCodeStyle()
        }

    }

    override fun initListener() {
        tv_flash.setOnClickListener(this)
        tv_photo.setOnClickListener(this)
        tv_bar_code.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }


    override fun onScanQRCodeSuccess(result: String) {
        CommonUtils.vibrate(this, 200)
        zxingview.stopSpot()
    }


    override fun onScanQRCodeOpenCameraError() {
        com.aillen.shopmall.utils.println("打开相机出错")
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_flash -> {
                if (isFlashOn) {
                    zxingview.openFlashlight()
                } else {
                    zxingview.closeFlashlight()
                }
                isFlashOn = !isFlashOn
            }
            R.id.tv_photo -> {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setPreviewEnabled(false)
                        .start(this)
            }
            R.id.tv_bar_code -> {
                if (isBarCode) {
                    finish()
                } else {
                    QRcodeActivity.start(this, true)
                }
            }
            R.id.iv_back -> {
                finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                PhotoPicker.REQUEST_CODE, PhotoPreview.REQUEST_CODE -> {
                    if(data == null) return
                    val photoUrl = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
                    if(photoUrl.isEmpty()) return
                    val executorService = Executors.newFixedThreadPool(2)
                    val result = Result<String>()
                    result.mThreadHandler = ThreadHolder.MAIN
                    result.addAsynResultListener(this)
                    val qrCodeRunnable = QRCodeRunnable(result, photoUrl[0])
                    executorService.submit(qrCodeRunnable)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE_CAMERA -> {
                persmissionDispatch.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    }

    override fun hasPermission(requestCode: Int, vararg permission: String) {
        println("requestCode = [${requestCode}], permission = [${permission}]")
        zxingview.startCamera()
        zxingview.startSpotAndShowRect()

    }

    override fun permissionDenied(requestCode: Int, vararg permission: String) {
        println("permissionDenied : requestCode = [$requestCode], permission = [$permission]")
        toast("没有相机权限，某些功能无法正常使用╮(╯_╰)╭")

    }

    override fun onNeverAskAgain(requestCode: Int, vararg permission: String) {
        toast("没有相机权限，某些功能无法正常使用╮(╯_╰)╭")
    }

    class QRCodeRunnable(result: Result<String>, val url: String) : TaskRunnable<String>(result) {
        override fun doWork(): String {
            return QRCodeDecoder.syncDecodeQRCode(url)
        }
    }

    override fun onResult(result: Result<String>) {
        if(result.mSuccess){
            CommonUtils.vibrate(this, 200)
            toast(result.mResult!!)
        }else{
            toast("二维码解析失败，(；′⌒`)")
        }
    }

    override fun onStart() {
        super.onStart()
        zxingview.startCamera()
        zxingview.startSpotAndShowRect()
    }


    override fun onStop() {
        try {
            zxingview.stopCamera()
            super.onStop()
        } catch (e: RuntimeException) {

        }
    }

    override fun onDestroy() {
        zxingview.onDestroy()
        super.onDestroy()
    }

    override fun getLayoutId() = R.layout.activity_qrcode

}
