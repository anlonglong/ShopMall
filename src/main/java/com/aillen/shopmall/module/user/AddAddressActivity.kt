package com.aillen.shopmall.module.user

import android.content.Context
import android.content.Intent
import com.aillen.shopmall.R
import com.aillen.shopmall.base.BaseActivity
import com.aillen.shopmall.repository.Address
import com.aillen.shopmall.repository.DAOManager
import kotlinx.android.synthetic.main.activity_add_address.*
import java.util.regex.Pattern

class AddAddressActivity : BaseActivity() {

    companion object {
        private const val ADDRESS = "user"
        fun start(ctx: Context,address: Address?){
            val intent = Intent(ctx, AddAddressActivity::class.java)
            intent.putExtra(ADDRESS,address)
            ctx.startActivity(intent)
        }
    }

    override fun initView() {
            if (getIntentExtra().getSerializableExtra(ADDRESS)!= null) {
                val user = getIntentExtra().getSerializableExtra(ADDRESS) as Address
                user_name.setText(user.realName)
                cellphone.setText(user.phone)
                area.setText(user.area)
                stress.setText(user.street)
                detail_address.setText(user.address)
                default_address.isChecked = user.isDefault!!
                save.text = "保存"
            }
    }

    override fun initListener() {
      default_address.setOnClickListener { default_address.toggle() }
      save.setOnClickListener {
         if (getIntentExtra().getSerializableExtra(ADDRESS)!= null){ upDate(getIntentExtra().getSerializableExtra(ADDRESS) as Address) } else { saveAddress()}
      }
    }

    private fun upDate(address: Address) {
        address.realName = user_name.text.toString()
        address.phone = cellphone.text.toString()
        address.area = area.text.toString()
        address.street =  stress.text.toString()
        address.address = detail_address.text.toString()
        address.isDefault = default_address.isChecked
        DAOManager.get(this).addressDao().update(address)
        toast("修改成功")
        finish()
    }


    private fun saveAddress() {
        if (user_name.text.toString().isEmpty()) {
            user_name.error = "收货人姓名不能为空"
            user_name.requestFocus()
            return
        }
        if (isValidPhone(cellphone.text.toString())) {
            cellphone.error = "电话号码有误"
            cellphone.requestFocus()
            return
        }

        if (area.text.toString().isEmpty()) {
            area.error = "地区不能为空"
            area.requestFocus()
            return
        }
        if (stress.text.toString().isEmpty()) {
            stress.error = "街道不能为空"
            stress.requestFocus()
            return
        }

        if (isValidAddress(detail_address.text.toString())) {
            detail_address.error = "地址不能为空"
            detail_address.requestFocus()
            return
        }
        val username = UserManager.userName
        val address = Address(0,
                              username,
                              user_name.text.toString(),
                              cellphone.text.toString(),
                              area.text.toString(),
                              stress.text.toString(),
                              detail_address.text.toString(),
                              default_address.isChecked)
        DAOManager.get(this).addressDao().insert(address)
        toast("添加成功")
        finish()
    }

    private fun isValidAddress(address: String): Boolean {
        return address.isEmpty()
    }

    private fun isValidPhone(phone: String): Boolean {
        val pattern = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,5-9])|(177))\\\\d{8}\$")
        return pattern.matcher(phone).matches()
    }

    override fun getLayoutId() = R.layout.activity_add_address


}
