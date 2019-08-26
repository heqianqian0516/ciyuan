package com.ciyuanplus.mobile.widget

import android.content.Context
import android.util.AttributeSet


class CountDownButton : androidx.appcompat.widget.AppCompatButton {

    private var mTimeCount: TimeCount? = null
    private var millisInFuture: Long = 60000
    private var countDownInterval: Long = 1000
    private var mTickText = "s后重新发送"
    private var mFinishText = "重新获取"

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

    }

    /**
     * 设置倒计时多少毫秒结束
     * 默认60秒
     *
     * @param millisInFuture 毫秒
     */
    fun setMillisInFuture(millisInFuture: Long) {
        this.millisInFuture = millisInFuture
    }

    /**
     * 设置倒计时间隔
     * 默认1秒
     *
     * @param countDownInterval 倒计时间隔
     */
    fun setCountDownInterval(countDownInterval: Long) {
        this.countDownInterval = countDownInterval
    }

    /**
     * 设置倒计时过程中button显示内容
     *
     * @param text 默认 s后重新获取
     */
    fun setOnTickText(text: String) {
        this.mTickText = text
    }

    /**
     * 设置倒计时结束button显示内容
     *
     * @param finishText 默认 重新获取
     */
    fun setOnFinishText(finishText: String) {
        this.mFinishText = finishText
    }


    fun startCount() {
        mTimeCount = TimeCount(millisInFuture, countDownInterval, this, mTickText, mFinishText)
        mTimeCount!!.start()
    }

}
