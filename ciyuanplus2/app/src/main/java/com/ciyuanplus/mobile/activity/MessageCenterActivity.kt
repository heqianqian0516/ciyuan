package com.ciyuanplus.mobile.activity

import android.os.Bundle
import android.widget.Toast
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.fragement.ChatFragment
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.RequestUserInfoDotApiParameter
import com.ciyuanplus.mobile.net.response.RequestUserInfoDotResponse
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response

class MessageCenterActivity : MyBaseActivity() {

    lateinit var mDotInfoResponse: RequestUserInfoDotResponse
    private var mRongMessageCount: Int = 0
    private var mSystemMessageCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_center)

//        requestUserInfoRedDot()

        supportFragmentManager.beginTransaction().add(R.id.fl_container, ChatFragment()).commitAllowingStateLoss()
    }

    private fun requestUserInfoRedDot() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_USER_INFO_RED_DOT_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestUserInfoDotApiParameter().requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = RequestUserInfoDotResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else {
                    mDotInfoResponse = response1
                    //                    if (mMineFragment != null) mMineFragment.updateDotView();
                }
            }


            override fun onFailure(e: HttpException, response: Response<String>) {
                super.onFailure(e, response)
                CommonToast.getInstance(App.mContext.resources.getString(R.string.string_get_fresh_news_fail_alert),
                        Toast.LENGTH_SHORT).show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }


}
