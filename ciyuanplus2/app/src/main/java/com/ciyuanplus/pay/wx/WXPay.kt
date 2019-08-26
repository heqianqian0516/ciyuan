package com.ciyuanplus.pay.wx

import android.content.Context
import android.widget.Toast
import com.ciyuanplus.mobile.utils.Constants
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.json.JSONObject

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/26 5:05 AM
 * class   : WXPay
 * desc   :
 * version: 1.0
 */
class WXPay(private val mContext: Context) {


    // IWXAPI 是第三方app和微信通信的openapi接口
    private val api: IWXAPI = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID)

    init {
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID)
    }

    fun pay(content: String?) {

        Logger.d("订单信息$content")

//        Toast.makeText(mContext, "获取订单中...", Toast.LENGTH_SHORT).show()
        try {

            if (content is String) {

                Logger.e("get server pay params:", content)
                val json = JSONObject(content)
                if (!json.has("retcode")) {
                    val req = PayReq()
                    req.appId = json.getString("appid")
                    req.partnerId = "1520009891"
                    req.prepayId = json.getString("prepayid")
                    req.nonceStr = json.getString("noncestr")
                    req.timeStamp = json.getString("timestamp")
                    req.packageValue = "Sign=WXPay"
                    req.sign = json.getString("sign")
//                    req.extData = "app data" // optional

                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req)
                } else {
                    Logger.d("PAY_GET", "返回错误" + json.getString("retmsg"))
                    Toast.makeText(mContext, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show()
                }
            } else {
                Logger.d("PAY_GET", "服务器请求错误")
                Toast.makeText(mContext, "服务器请求错误", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Logger.e("PAY_GET", "异常：" + e.message)
            Toast.makeText(mContext, "异常：" + e.message, Toast.LENGTH_SHORT).show()
        }

    }
}
