package com.ciyuanplus.mobile.widget

import android.os.CountDownTimer
import android.widget.Button

class TimeCount
/**
 * @param millisInFuture    倒计时总时长
 * @param countDownInterval 倒计时单位 毫秒.
 */
(millisInFuture: Long, countDownInterval: Long,
 private val button: Button, private val tickText: String, private val finishText: String) : CountDownTimer(millisInFuture, countDownInterval) {


    override fun onTick(millisUntilFinished: Long) {
        button.text = String.format("%d%s", millisUntilFinished / 1000, tickText)
        button.isEnabled = false
    }

    override fun onFinish() {
        button.isEnabled = true
        button.text = finishText
    }

}
