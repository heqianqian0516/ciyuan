package com.asdfghjjkk.superiordiaryokdsakd.wxapi


import android.content.Intent
import android.os.Bundle
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.dialog.PayResultDialogFragment
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
import com.ciyuanplus.mobile.net.response.PayDetailResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.jetbrains.anko.startActivity

class WXPayEntryActivity : MyBaseActivity(), IWXAPIEventHandler {


    private var api: IWXAPI? = null
    private var payResultInfo: JsWebViewActivity.PayResultInfo? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID)
        api!!.handleIntent(intent, this)

    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {

        Logger.d("req  = $req")
    }

    override fun onResp(resp: BaseResp) {


        Logger.d("resp.errStr  =  ${resp.errStr} , resp.errCode  = ${resp.errCode},resp.transaction  =  ${resp.transaction}")


        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (resp.errCode) {
                0 -> {

                    runOnUiThread {
                        CommonToast.getInstance("付款成功").show()
                        if (resp.transaction is String) {
                            requestDetail(resp)
                        } else {


                            val payInfo = JsWebViewActivity.PayResultInfo(PayResultDialogFragment.paySuccess, "-1", "", (App.mContext as App).oderId)

                            EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_PAY_SUCCESS, payInfo))
                        }

                    }
                    finish()
                }
                -2 -> {
                    Logger.d("微信支付 用户取消 ${payResultInfo?.toString()}")
                    finish()

                }
                1 -> {

                    runOnUiThread {
                        CommonToast.getInstance("付款失败").show()
//

                        if (resp.transaction is String) {
                            requestDetail(resp)

                        } else {

                            val payInfo = JsWebViewActivity.PayResultInfo(PayResultDialogFragment.payFail, "-1", "", (App.mContext as App).oderId)

                            EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_PAY_SUCCESS, payInfo))
//
                        }
                    }
                    finish()
                }
            }
        }
    }

    private fun requestDetail(resp: BaseResp) {

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_PAY_DETAIL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)

        val map = HashMap<String, String>()
        map["transactionId"] = resp.transaction

        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(map).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) {
            postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        }

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {

            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val res = PayDetailResponse(s)

                val payInfo = JsWebViewActivity.PayResultInfo(res.mItem?.status ?: 0, res.mItem.toString(), "", (App.mContext as App).oderId)

                EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_PAY_SUCCESS, payInfo))
                finish()
//

            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                finish()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }


    private fun showPayResultDialog(payResult: Int, money: String?, url: String?) {

        val fragment = PayResultDialogFragment.newInstance(payResult, money)

        fragment.show(supportFragmentManager, "F**k")

        fragment.addListener {

            val payInfo = JsWebViewActivity.PayResultInfo(PayResultDialogFragment.paySuccess, money
                    ?: "0", "", (App.mContext as App).oderId)

            EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_PAY_SUCCESS, payInfo))


//            startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to url)
            finish()
        }
    }


}