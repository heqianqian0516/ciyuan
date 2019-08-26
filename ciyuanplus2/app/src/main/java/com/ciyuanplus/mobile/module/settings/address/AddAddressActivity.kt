package com.ciyuanplus.mobile.module.settings.address

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.InputType.*
import android.text.Spanned
import android.view.View
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.MyOneLineView
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import kotlinx.android.synthetic.main.activity_add_address.*
import java.util.regex.Pattern

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/23
 * class  : AddAddressActivity.kt
 * desc   : 添加地址
 * version: 1.0
 */


class AddAddressActivity : MyBaseActivity(), MyOneLineView.OnRootClickListener, MyOneLineView.OnArrowClickListener {

    private var editMode: Int = 0
    private var userName: String? = null
    private var userPhone: String? = null
    private var userAddress: String? = null
    private var addressId: Int = 0

    companion object {
        const val noEditMode = 0
        const val isEditMode = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        intent?.let {
            it.extras?.let { bundle ->
                editMode = bundle.getInt(Constants.INTENT_IS_EDIT_MODE, 0)
                userName = bundle.getString(Constants.INTENT_USER_NAME, "")
                userPhone = bundle.getString(Constants.INTENT_USER_PHONE, "")
                userAddress = bundle.getString(Constants.INTENT_USER_ADDRESS, "")
                addressId = bundle.getInt(Constants.INTENT_ADDRESS_ID, -1)
            }
        }

        titleBar.setOnBackListener(View.OnClickListener { onBackPressed() })

        if (editMode == isEditMode) {
            titleBar.setTitle("修改地址")
            userNameText.initItemWidthEdit(R.mipmap.ic_launcher, "收货人", "请输入姓名")
                    .setRootPaddingTopBottom(20, 20).showLeftIcon(false)
                    .showDivider(false, false).setInputLength(20)
            userNameText.editContent.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME

//            userNameText.editContent.filters = arrayOf<InputFilter>(object : InputFilter {
//                override fun filter(charSequence: CharSequence, i: Int, i1: Int, spanned: Spanned, i2: Int, i3: Int): CharSequence? {
//                    val regex = "^[\u4E00-\u9FA5]+$"
//                    val isChinese = Pattern.matches(regex, charSequence.toString())
//                    if (Character.isLetterOrDigit(charSequence.elementAt(i)) && !isChinese) {
//                        return ""
//                    }
//                    return charSequence
//                }
//            })
            userName?.let { userNameText.setEditContent(userName) }


            userPhoneText.initItemWidthEdit(R.mipmap.ic_launcher, "联系电话", "请输入手机号")
                    .setRootPaddingTopBottom(20, 20).showLeftIcon(false)
                    .showDivider(false, false).setInputLength(11)
            userPhoneText.editContent.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
            userPhone?.let { userPhoneText.setEditContent(userPhone) }


            userAddressText.initItemWidthEdit(R.mipmap.ic_launcher, "详细地址", "请输入详细地址")
                    .setRootPaddingTopBottom(20, 20).showLeftIcon(false)
                    .showDivider(false, false)
            userAddressText.setEditContent(userAddress)
            userAddressText.editContent.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_POSTAL_ADDRESS or TYPE_TEXT_FLAG_MULTI_LINE

            addAddressButton.setOnClickListener { addAddress() }

        } else {

            titleBar.setTitle("新建地址")

            userNameText.initItemWidthEdit(R.mipmap.ic_launcher, "收货人", "请输入姓名")
                    .setRootPaddingTopBottom(20, 20).showLeftIcon(false)
                    .showDivider(false, false).setInputLength(20)
            userNameText.editContent.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME

//            userNameText.editContent.filters = arrayOf<InputFilter>(object : InputFilter {
//                override fun filter(charSequence: CharSequence, i: Int, i1: Int, spanned: Spanned, i2: Int, i3: Int): CharSequence? {
//                    val regex = "^[\u4E00-\u9FA5]+$"
//                    val isChinese = Pattern.matches(regex, charSequence.toString())
//                    return if (!isChinese) {
//                        null
//                    } else charSequence
//                }
//            })

            userPhoneText.initItemWidthEdit(R.mipmap.ic_launcher, "联系电话", "请输入手机号")
                    .setRootPaddingTopBottom(20, 20).showLeftIcon(false)
                    .showDivider(false, false).setInputLength(11)
            userPhoneText.editContent.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL


            userAddressText.initItemWidthEdit(R.mipmap.ic_launcher, "详细地址", "请输入详细地址")
                    .setRootPaddingTopBottom(20, 20).showLeftIcon(false)
                    .showDivider(false, false)
            userAddressText.editContent.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_POSTAL_ADDRESS or TYPE_TEXT_FLAG_MULTI_LINE

            addAddressButton.setOnClickListener { addAddress() }
        }

    }

    private fun addAddress() {
        userName = userNameText.editContent.text.trim().toString()
        userPhone = userPhoneText.editContent.text.trim().toString()
        userAddress = userAddressText.editContent.text.trim().toString()
        if (StringUtils.isEmpty(userName)) {

            showToast("请输入收货人")
            return
        }
        if (StringUtils.isEmpty(userPhone) || !RegexUtils.isMobileSimple(userPhone)) {

            showToast("请输入正确的手机号")
            return
        }
        if (StringUtils.isEmpty(userAddress)) {

            showToast("请输入收货人地址")
            return
        }

        val map = HashMap<String, String>()
        map["name"] = userName!!
        map["mobile"] = userPhone!!
        map["address"] = userAddress!!

        var api: String = when (editMode) {
            isEditMode -> {
                map["addressId"] = addressId.toString()
                "/api/server/shoppingAddress/editShoppingAddress"

            }
            noEditMode -> {
                map["addressId"] = addressId.toString()
                "/api/server/shoppingAddress/addShoppingAddress"
            }
            else -> {
                map["addressId"] = addressId.toString()
                ""
            }
        }

        val postRequest = StringRequest(ApiContant.URL_HEAD + api)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(map).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = ResponseData(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    val successMsg = if (editMode == noEditMode) "添加成功" else "修改成功"
                    showToast(successMsg)
                    finish()
                } else {
                    showToast(response1.mMsg)
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                showToast("添加失败")
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }


    override fun onRootClick(view: View?) {
        var position = 0
        when (view?.tag as Int) {
            1 -> position = 1
            2 -> position = 2
        }
//        Toast.makeText(this, "点击了第" + position + "行", Toast.LENGTH_SHORT).show()
    }

    override fun onArrowClick(view: View?) {

        var position = 0
        when (view?.tag as Int) {
            1 -> position = 1
            2 -> position = 2
        }
//        Toast.makeText(this, "点击了第" + position + "行右边的箭头", Toast.LENGTH_SHORT).show()
    }

}
