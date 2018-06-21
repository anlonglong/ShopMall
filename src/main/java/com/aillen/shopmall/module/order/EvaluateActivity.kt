package com.aillen.shopmall.module.order

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UploadBatchListener
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.module.main.disvocery.Find
import com.aillen.shopmall.module.main.disvocery.ItemType
import com.aillen.shopmall.module.user.UserManager
import com.aillen.shopmall.module.store.Shop
import com.aillen.shopmall.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_evaluate.*
import me.iwf.photopicker.PhotoPicker
import me.iwf.photopicker.PhotoPreview
import org.jetbrains.anko.progressDialog
import java.util.ArrayList

class EvaluateActivity : BaseActivity(), TextWatcher, View.OnClickListener {


    private val shop: Shop by lazy { getIntentExtra().getSerializableExtra(SHOP) as Shop }

    private var list:ArrayList<String>? = null

    override fun afterTextChanged(s: Editable) {
        word_count.text = resources.getString(R.string.word_count,s.toString().length.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

   }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

     }

    companion object {
        private val SHOP = "shop"
        fun start(ctx: Context, shop: Shop) {
            val intent = Intent(ctx, EvaluateActivity::class.java)
            intent.putExtra(SHOP,shop)
            ctx.startActivity(intent)
        }
    }

    override fun initView() {

    }

    override fun initListener() {
        et_comment.addTextChangedListener(this)
        ly_add_img.setOnClickListener(this)
        tv_good_goods.setOnClickListener(this)
        bt_send.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }

    override fun onClick(v: View) {
          when(v.id) {
              R.id.ly_add_img ->{
                  PhotoPicker.builder()
                      .setPhotoCount(3)
                      .setShowCamera(true)
                      .setPreviewEnabled(false)
                      .start(this)
              }
              R.id.tv_good_goods ->{
                  tv_good_goods.setBackgroundResource(R.drawable.common_bg_orange_8x4)
                  tv_good_goods.setTextColor(Color.WHITE)}
              R.id.bt_send ->{insertEva()}
              R.id.iv_back ->{finish()}
          }

    }
    private val progressBar:ProgressDialog by lazy {
       progressDialog("正在上传","上传图片"){progress = 0}
    }
    private fun insertEva() {
        if (et_comment.text.toString().trim().isEmpty()) {
            toast("评价不能为空")
            return
        }

        if (!(null != list && !list!!.isEmpty())) {
            toast("图片不能为空")
            return
        }

        if (list!!.size != 3){
            toast("图片必须为3张")
            return
        }
        val find = Find(shop.S_OID,et_comment.text.toString().trim(), UserManager.userName,bt_send.text.toString(),
                list,120, ItemType.TYPE_USER.getTypeCode(), arrayListOf(),arrayListOf())
        BmobFile.uploadBatch(list?.toArray(arrayOf<String>()),object : UploadBatchListener{
            override fun onSuccess(bmobFileList: MutableList<BmobFile>, urls: MutableList<String>) {
                   if (bmobFileList.size == list?.size) {
                       toast("上传结束")
                       progressBar.dismiss()
                       val saveFind = find.copy(user_pic_url = bmobFileList.flatMap { arrayListOf(it.fileUrl) })
                       saveFind.save(object :SaveListener<String>(){
                           override fun done(objId: String?, e: BmobException?) {
                               if (null != e) {
                                   toast("服务器异常，评价失败")
                               }else{
                                   finish()
                               }
                           }

                       })
                   }
            }

            override fun onProgress(index: Int, progress: Int, picCount: Int, totalProgress: Int) {
                progressBar.setMessage("正在上传低${index}张图片")
                progressBar.progress = totalProgress
            }

            override fun onError(code: Int, msg: String) {
                toast(msg)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK &&(requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)){
            list = data?.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
            if (null != list)
            createImageView(list!!)
        }
    }

    private fun createImageView(listExtra: ArrayList<String>) {
        if (ly_pic.childCount != 0) {
            ly_pic.removeAllViews()
        }
        var imageView:ImageView
        val lp = LinearLayout.LayoutParams(CommonUtils.dp2Px(this,100),CommonUtils.dp2Px(this,100))
        listExtra.forEach { imageView = ImageView(this)
            with(imageView) {
                layoutParams = lp
                setPadding(0,0,CommonUtils.dp2Px(this@EvaluateActivity,10),0)
                scaleType = ImageView.ScaleType.FIT_XY
                setImageBitmap(BitmapFactory.decodeFile(it))
                ly_pic.addView(this)
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_evaluate

}
