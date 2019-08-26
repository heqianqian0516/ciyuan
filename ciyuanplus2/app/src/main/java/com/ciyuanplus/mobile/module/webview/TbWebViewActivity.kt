package com.ciyuanplus.mobile.module.webview

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.Toast
import butterknife.ButterKnife
import com.blankj.utilcode.util.StringUtils
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.R2.layout.item
import com.ciyuanplus.mobile.activity.chat.FeedBackActivity
import com.ciyuanplus.mobile.activity.mine.MineWelfareActivity
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity
import com.ciyuanplus.mobile.dialog.PayResultDialogFragment
import com.ciyuanplus.mobile.inter.MyOnClickListener
import com.ciyuanplus.mobile.manager.*
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.popup.share_game_invite_popup.ShareH5GameInvitePopup
import com.ciyuanplus.mobile.module.popup.share_invite_popup.ShareInvitePopupActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.bean.HomeADBean
import com.ciyuanplus.mobile.net.parameter.RequesAliPayOrderInfoApiParameter
import com.ciyuanplus.mobile.net.response.CommodityListItemRes
import com.ciyuanplus.mobile.statistics.StatisticsManager
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.ciyuanplus.mobile.widget.CustomDialog
import com.ciyuanplus.pay.ali.AliPay
import com.ciyuanplus.pay.ali.PayResult
import com.ciyuanplus.pay.wx.WXPay
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.activity_js_webview.*
import kotlinx.android.synthetic.main.activity_js_webview.m_js_common_title
import kotlinx.android.synthetic.main.activity_js_webview.m_js_webview
import kotlinx.android.synthetic.main.activity_tb_webview.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by Alen on 2017/7/3.
 *
 *
 * 通用的注册 与 H5 调用的 webview Activity
 */

class TbWebViewActivity : MyBaseActivity(), EventCenterManager.OnHandleEventListener {

    private var mUrl: String? = null
    private var mParams: String? = null//可能需要传给页面一些参数
    private var mPrePage: String? = null
    private var isShowTitleBar = true
    private var configerManagner: ConfigerManagner? = null
    private var title: String? = null
    private var uploadFile: ValueCallback<Uri>? = null
    private var uploadFiles: ValueCallback<Array<Uri>>? = null
    private var money: String? = null
    private var taobaoLink:String?=null
    private var couponLink:String?=null
    private var type:Int?=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_tb_webview)

        mUrl = intent.getStringExtra(Constants.INTENT_OPEN_URL)
        mParams = intent.getStringExtra(Constants.INTENT_JS_WEB_VIEW_PARAM)
        mPrePage = intent.getStringExtra("prePage")
        isShowTitleBar = intent.getBooleanExtra(Constants.INTENT_SHOW_TITLE_BAR, true)
        title = intent.getStringExtra(Constants.INTENT_TITLE_BAR_TITLE)
        money = intent.getStringExtra(Constants.INTENT_PAY_TOTAL_MONEY)
        taobaoLink=intent.getStringExtra(Constants.TAO_BAO_LINK)
        couponLink=intent.getStringExtra(Constants.COUPON_LINK)
        clerWebView()
        if (Utils.isStringEmpty(mUrl)) finish()

        window.setFormat(PixelFormat.TRANSLUCENT)// X5 网页中的视频，上屏幕的时候，可能出现闪烁的情况
        enter_taobao.setOnClickListener(){
            type=1
            m_js_webview.loadUrl(taobaoLink)
            clerWebView()
            linear_taobao.visibility=View.GONE

           // startActivity<TblinkWebviewActivity>(Constants.TAO_BAO_LINK to taobaoLink,Constants.COUPON_LINK to couponLink)

        }
        enter_coupons.setOnClickListener(){

               type=2
               m_js_webview.loadUrl(couponLink)
               clerWebView()
               linear_taobao.visibility=View.GONE

            //startActivity<TblinkWebviewActivity>(Constants.TAO_BAO_LINK to taobaoLink,Constants.COUPON_LINK to couponLink)

        }
        initView()
    }

    private fun initView() {
        ButterKnife.bind(this)

        initTitleBar()

        initWebView()
        m_js_webview.resumeTimers()
        m_js_webview.addJavascriptInterface(this, "Android")
        m_js_webview.loadUrl(mUrl)

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_PAY_SUCCESS, this)

    }
    private fun clerWebView(){
        m_js_webview.postDelayed(Runnable{
            m_js_webview.clearHistory()
        },1000)

    }
    private fun initWebView() {


        m_js_webview!!.webChromeClient = object : WebChromeClient() {

            // For Android < 3.0
            fun openFileChooser(valueCallback: ValueCallback<Uri>) {
                uploadFile = valueCallback
                openImageChooserActivity()
            }

            // For Android  >= 3.0
            fun openFileChooser(valueCallback: ValueCallback<Uri>, acceptType: String) {
                uploadFile = valueCallback
                openImageChooserActivity()
            }

            //For Android  >= 4.1
            override fun openFileChooser(valueCallback: ValueCallback<Uri>, acceptType: String?, capture: String?) {
                uploadFile = valueCallback
                openImageChooserActivity()
            }

            // For Android >= 5.0
            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: WebChromeClient.FileChooserParams?): Boolean {
                uploadFiles = filePathCallback
                openImageChooserActivity()
                return true
            }

        }
    }

    private fun initTitleBar() {
        m_js_common_title!!.visibility = if (isShowTitleBar) View.VISIBLE else View.GONE
        if (!StringUtils.isEmpty(title)) {
            m_js_common_title!!.setTitle(title!!)
        }

        if (!Utils.isStringEmpty(mPrePage) && Utils.isStringEquals(mPrePage, "NewsFragmentPresenter")) {
            m_js_common_title!!.setBackgroundColor(-0x1)
        } else {
            m_js_common_title!!.setBackgroundColor(-0x1000000)
        }

        m_js_common_title!!.setOnBackListener(object : MyOnClickListener() {
            public override fun performRealClick(v: View) {
                onBackPressed()
            }
        })
    }

    private fun openFileChooseProcess() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/*"
        startActivityForResult(Intent.createChooser(i, "test"), 0)
    }


    private fun openImageChooserActivity() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadFile && null == uploadFiles) {
                return
            }
            val result = if (data == null || resultCode != Activity.RESULT_OK) null else data.data
            if (uploadFiles != null) {
                onActivityResultAboveL(requestCode, resultCode, data)
            } else if (uploadFile != null) {
                uploadFile!!.onReceiveValue(result)
                uploadFile = null
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadFiles == null) {
            return
        }
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
                if (clipData != null) {

                    results = emptyArray()

                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results[i] = item.uri
                    }
                }
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        uploadFiles!!.onReceiveValue(results)
        uploadFiles = null
    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        if (requestCode == Constants.REQUEST_CODE_POP_INVITE_SHARE_CODE) {// 邀请弹框  邀请成功
    //            if (resultCode == RESULT_OK) {
    //                //原生 通知 JS页面  分享成功的方法。 非常不准确，十分不可靠，
    //                String JS_METHOD_INVITE_POP_UP = "shareCallBack";
    //                m_js_webview.loadUrl("javascript:" + JS_METHOD_INVITE_POP_UP
    //                        + "(" + ")");
    //            }
    //        }
    //
    //
    //    }

    override fun onPause() {
        m_js_webview!!.onPause()
        super.onPause()
    }

    override fun onResume() {
        m_js_webview!!.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        destroyWebView()
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_PAY_SUCCESS, this)
        super.onDestroy()
    }
    override fun onBackPressed() {
        if (type == 0) {
            super.onBackPressed()
            return
        }  else  {
            if (m_js_webview!!.canGoBack()) {
                m_js_webview!!.goBack()
            } else {
                type=0
                m_js_webview.loadUrl(mUrl)
                clerWebView()
                linear_taobao.visibility = View.VISIBLE
            }
        }

    }

    //由于安全原因 targetSdkVersion>=17需要加 @JavascriptInterface
    //JS调用Android
    // 调用这个接口可以直接关闭当前的Activity
    @JavascriptInterface
    fun onBackImagePress() {

        runOnUiThread {
            CommonToast.getInstance("成功调用了 原生的接口，如果没有关闭页面是原生的bug").show()
            this@TbWebViewActivity.finish()
        }
    }


    // 测试方法
    @JavascriptInterface
    fun startFunction() {

        runOnUiThread { CommonToast.getInstance("成功调用了 原生的接口").show() }
    }

    // 弹出分享 邀请好友页面
    @JavascriptInterface
    fun popInviteShareActivity(urls: String, icon: String, title: String, desc: String) {

        runOnUiThread {
            if (!LoginStateManager.isLogin()) {
                val intent = Intent(this@TbWebViewActivity, LoginActivity::class.java)
                this@TbWebViewActivity.startActivity(intent)
                return@runOnUiThread
            }

            val intent = Intent(this@TbWebViewActivity, ShareInvitePopupActivity::class.java)
            intent.putExtra("url", urls)
            intent.putExtra("icon", icon)
            intent.putExtra("title", title)
            intent.putExtra("desc", desc)
            startActivityForResult(intent, Constants.REQUEST_CODE_POP_INVITE_SHARE_CODE)
        }
    }

    // 弹出分享 邀请好友页面
    @JavascriptInterface
    //    public void doShare(final String urls, final String icon, final String title, final String desc) {
    fun doShare(title: String, desc: String, urls: String, icon: String) {

        runOnUiThread {
            if (!LoginStateManager.isLogin()) {
                val intent = Intent(this@TbWebViewActivity, LoginActivity::class.java)
                this@TbWebViewActivity.startActivity(intent)
                return@runOnUiThread
            }

            val intent = Intent(this@TbWebViewActivity, ShareH5GameInvitePopup::class.java)
            intent.putExtra("url", urls)
            intent.putExtra("icon", icon)
            intent.putExtra("title", title)
            intent.putExtra("desc", desc)
            startActivityForResult(intent, Constants.REQUEST_CODE_POP_INVITE_SHARE_CODE)
        }
    }

    //联系客服的按钮
    @JavascriptInterface
    fun sendProgramSession() {
        runOnUiThread {
            val intent = Intent(this@TbWebViewActivity, FeedBackActivity::class.java)
            startActivity(intent)
        }
    }

    //设置中间title的接口
    @JavascriptInterface
    fun setTopBarCenterText(text: String) {
        runOnUiThread { m_js_common_title!!.setTitle(text) }
    }

    //获取用户信息的接口
    // 使用Json 封装 用户名(nickname)  用户id(uuid)  头像(photo)  临时/正式用户(userState)
    @JavascriptInterface
    fun getCurrentUserInfo() {
        val nickname = UserInfoData.getInstance().userInfoItem.nickname
        val uuid = UserInfoData.getInstance().userInfoItem.uuid
        val photo = UserInfoData.getInstance().userInfoItem.photo
        val userState = if (LoginStateManager.isLogin()) "1" else "2"
        val JS_METHOD_SET_CURRENT_USER_INFO = "setCurrentUserInfo"
        m_js_webview!!.loadUrl("javascript:" + JS_METHOD_SET_CURRENT_USER_INFO
                + "('" + nickname + "','" + uuid + "','" + photo + "','" + userState + "')")

    }

    @JavascriptInterface
    fun PayOrder(merId: String, orderId: String, payType: Int) {

        getAliPayOrderInfo(merId, orderId, payType)
    }

    // 获取用户uuid 和gift id
    @JavascriptInterface
    fun getUserUuidAndUserGiftId() {
        val uuid = UserInfoData.getInstance().userInfoItem.uuid
        val idAndGiftInfo = "setUserUuidAndUserGiftId"
        m_js_webview!!.loadUrl("javascript:" + idAndGiftInfo
                + "('" + uuid + "','" + mParams + "')")
    }

    // 分享成功，检查是否要弹框
    @JavascriptInterface
    fun checkGift(type1: String, alert: String) {
        val type = Integer.parseInt(type1)
        if (type == -1) {// -1 错误信息
            StatisticsManager.onErrorInfo(type.toString() + "", alert + "")
            return
        }
        val builder = CustomDialog.Builder(this)
        if (type > 0) {
            builder.setMessage(alert + "")
            builder.setPositiveButton("立刻查看") { dialog, which ->
                val intent = Intent(this@TbWebViewActivity, MineWelfareActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            builder.setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
        } else {
            builder.setMessage("分享成功")
            builder.setNegativeButton("确定") { dialog, which -> dialog.dismiss() }
        }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun destroyWebView() {
        m_js_webview?.destroy()
    }

    private fun getAliPayOrderInfo(merId: String, orderId: String, payType: Int) {

        (App.mContext as App).oderId = orderId

        val urlApi = if (1 == payType) ApiContant.REQUEST_ALI_PAY_ORDER_INFO else ApiContant.REQUEST_WEIXIN_ORDER_INFO

        val postRequest = StringRequest(ApiContant.URL_HEAD + urlApi)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequesAliPayOrderInfoApiParameter(merId, orderId).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) {
            postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        }

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {

            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val res = ResponseData(s)

                val payInfo = PayResultInfo(PayResultDialogFragment.paySuccess, money
                        ?: "0", "", orderId)
                EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_PAY_INFO, payInfo))


                Logger.d(s)
                Logger.d("data =  " + res.mMsg)
                if (TextUtils.isEmpty(res.mRawDataString)) {

                    Logger.d("mRawDataString = " + res.mRawDataString)
                    return
                }

                if (1 == payType) {
                    AliPay().pay(mActivity, res.mRawDataString, object : PayResult {
                        override fun onSuccess() {
                            Logger.d("支付成功返回信息 = $s")

                            val payInfo = PayResultInfo(PayResultDialogFragment.paySuccess, money
                                    ?: "-1", "", orderId)

                            EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_PAY_SUCCESS, payInfo))

                        }

                        override fun onError(error_code: Int) {
                            val payInfo = PayResultInfo(PayResultDialogFragment.payFail, money
                                    ?: "-1", "https://www.kotlincn.net/docs/reference/", orderId)
                            EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_PAY_SUCCESS, payInfo))
                            Logger.d("支付失败$error_code")

                        }

                        override fun onCancel() {
                            Logger.d("支付取消")

                        }
                    })
                } else {
                    WXPay(this@TbWebViewActivity).pay(res.mRawDataString)

                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun showPayResultDialog(payResult: Int, money: String?) {

        val fragment = PayResultDialogFragment.newInstance(payResult, money)

        fragment.show(supportFragmentManager, "F**k")

        fragment.addListener {

            val userUuid = UserInfoData.getInstance().userInfoItem.uuid
            val authToken = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_TEMP_USER_RONG_SESSION_KEY, "")
            val url = "${ApiContant.WEB_DETAIL_VIEW_URL}cyplus-share/order.html?userUuid=$userUuid&authToken=$authToken&orderId=${(App.mContext as App).oderId}"

            startActivity<TbWebViewActivity>(Constants.INTENT_OPEN_URL to url)
            finish()
        }

    }

    @JavascriptInterface
    fun openAndroid(msg: String) {
        Toast.makeText(this@TbWebViewActivity, "返回按钮监控", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    @JavascriptInterface
    fun writeData(msg: String) {
        configerManagner = ConfigerManagner.getInstance(this)
        configerManagner!!.setString("js", msg)
    }

    @JavascriptInterface
    fun giveInformation(msg: String): String {
        configerManagner = ConfigerManagner.getInstance(this)
        return configerManagner!!.getString("js")
    }

    @JavascriptInterface
    fun goBack() {
        finish()
    }

    @JavascriptInterface
    fun share(title: String, desc: String, img: String, prodId: String) {

        val item = FreshNewItem()
        item.title = title
        item.description = desc
        item.imgs = img
        item.postUuid = prodId
        val intent = Intent(this, ShareNewsPopupActivity::class.java)
        intent.putExtra(Constants.INTENT_NEWS_ITEM, item)
        startActivity(intent)
    }


    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage?) {
        val payResultInfo = eventMessage?.mObject as PayResultInfo

        when (eventMessage.mEvent) {
            Constants.EVENT_MESSAGE_PAY_SUCCESS -> {
                showPayResultDialog(payResultInfo.payResult, payResultInfo.money)
            }
        }
    }

    data class PayResultInfo(var payResult: Int, var money: String?, var url: String, var orderId: String)

    companion object {
        private val FILE_CHOOSER_RESULT_CODE = 10000
    }

}
