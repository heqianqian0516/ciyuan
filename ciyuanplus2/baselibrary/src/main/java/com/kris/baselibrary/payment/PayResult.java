package com.kris.baselibrary.payment;

/**
 * Created by Administrator on 2017/9/4.
 *
 * 支付接口
 */

public interface PayResult {
    void onSuccess(); //支付成功

    void onError(int error_code);   //支付失败

    void onCancel();    //支付取消
}
