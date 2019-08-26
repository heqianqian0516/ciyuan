package com.ciyuanplus.mobile.module.mine.sign

import android.os.Bundle
import android.widget.Toast
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R

import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import kotlinx.android.synthetic.main.activity_change_sign.*
import java.util.*

class ChangeSignActivity : MyBaseActivity() {

    private var oldSign: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_sign)


        intent.extras?.let {
            oldSign = it.getString(Constants.INTENT_SIGN)
        }

        initView()
    }

    private fun initView() {

        closeButton.setOnClickListener { finish() }
        submitButton.setOnClickListener { doCheck() }
    }

    private fun doCheck() {
        val name = signText.text.toString()
        if (Utils.isStringEmpty(name)) {
            CommonToast.getInstance("请输入签名").show()
            return
        }
        if (Utils.isStringEquals(name, oldSign)) {
            CommonToast.getInstance("请先修改签名").show()
            return
        }
        doSubmit()
    }

    private fun doSubmit() {

        val newSign = signText.text.toString()

        val paramsMap = HashMap<String, String>()
        paramsMap["personalizedSignature"] = newSign

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CHANGE_SIGN)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(paramsMap).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ChangeSignActivity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = ResponseData(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    UserInfoData.getInstance().userInfoItem.personalizedSignature = newSign
                    EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE))
                    finish()
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
            }
            //
            //            @Override
            //            public void onFailure(HttpException e, Response<String> response) {
            //                super.onFailure(e, response);
            //                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_name_fail_alert), Toast.LENGTH_SHORT).show();
            //            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }
}

