package com.ciyuanplus.pay.ali;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.Map;

public class AliPay {

    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "2016091900549310";
    private static final int SDK_PAY_FLAG = 1;

    private PayResult mCallBack;

    public void pay(final Activity activity, final String orderInfo, PayResult callBack) {
        pay(activity, orderInfo, callBack,"");
    }

    /**
     * 支付宝支付业务示例
     */
    public void pay(final Activity activity, final String orderInfo, PayResult callBack, String content) {

        this.mCallBack = callBack;

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, true,content);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//        String sign = OrderInfoUtil2_0.getSign(params, Constants.ALIPAY_RSA2_PRIVATE, true);
//        final String orderInfo2 = orderParam + "&" + sign;
        Logger.d(orderInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Logger.d("msp "+ result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    Logger.d(payResult.toString());
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, "支付成功: " + payResult);
                        ToastUtils.showShort("支付成功");
                        if (mCallBack != null) {
                            mCallBack.onSuccess();
                        }
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        // 用户中途取消
//                        showAlert(PayDemoActivity.this, "取消支付: " + payResult);
                        ToastUtils.showShort("取消支付");
                        if (mCallBack != null) {
                            mCallBack.onCancel();
                        }
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        //正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, "支付失败: " + payResult);
                        ToastUtils.showShort("支付失败");
                        if (mCallBack != null) {
                            mCallBack.onError(Integer.parseInt(resultStatus));
                        }
                    }
                    break;
                }
                default:
                    Logger.d(msg.toString());
                    break;

            }
        }

    };

    public void setCallBack(PayResult callBack) {
        this.mCallBack = callBack;
    }


}
