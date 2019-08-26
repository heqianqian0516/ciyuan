package com.ciyuanplus.mobile.module.account_manage


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.adapter.AccountManageAdapter
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.BindType
import com.ciyuanplus.mobile.net.parameter.AutoLoginApiParameter
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
import com.ciyuanplus.mobile.net.response.BindListResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_account_manage.*
import org.jetbrains.anko.toast


/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/1
 * class  : AccountManageActivity.kt
 * desc   : 账户管理页面
 * version: 1.0
 */


class AccountManageActivity : MyBaseActivity() {

    val data = ArrayList<BindType>()
    lateinit var adapter: AccountManageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ciyuanplus.mobile.R.layout.activity_account_manage)

        initView()
//        UmengTool.checkQQ(this)
    }

    private fun initView() {
        title_bar.setTitle("账号管理")
        title_bar.setOnBackListener(View.OnClickListener { onBackPressed() })

        rcl_account_manage.layoutManager = LinearLayoutManager(this)
        adapter = AccountManageAdapter(data)

        adapter.setOnItemChildClickListener { adapter, view, position ->

            val item = adapter.getItem(position)
            if (item is BindType) {

                when (item.type) {


                    BindType.TYPE_WEIXIN -> {
                        Logger.d("微信授权")
//                        UMShareAPI.get(this).getPlatformInfo(this@AccountManageActivity, SHARE_MEDIA.WEIXIN, umAuthListener)
                        UMShareAPI.get(this).getPlatformInfo(mActivity, SHARE_MEDIA.WEIXIN, umAuthListener)
                    }
                    BindType.TYPE_QQ -> {
                        Logger.d("QQ授权")
//                        UMShareAPI.get(this).getPlatformInfo(this@AccountManageActivity, SHARE_MEDIA.QQ, umAuthListener)
                        UMShareAPI.get(this).getPlatformInfo(mActivity, SHARE_MEDIA.QQ, umAuthListener)
                    }
                    BindType.TYPE_WEIBO -> {
                        Logger.d("微博授权")
                        UMShareAPI.get(this).getPlatformInfo(this@AccountManageActivity, SHARE_MEDIA.SINA, umAuthListener)
                        UMShareAPI.get(this).getPlatformInfo(mActivity, SHARE_MEDIA.SINA, umAuthListener)
                    }
                    else -> {
                        Logger.d("没有授权========")
                    }

                }

            } else {
                toast(item.toString())
            }

        }
        rcl_account_manage.adapter = adapter

        requestBindList()


    }

    private fun requestBindList() {


        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")

        val userUuid = UserInfoData.getInstance().userInfoItem.uuid

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_OTHER_PLATFORM_BIND_LIST)

        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpBody<AbstractRequest<String>>(AutoLoginApiParameter(userUuid, "").requestBody)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                Logger.d(s)
                Logger.d(response.toString())

                val res = BindListResponse(s)
                if (res.mCode != ResponseData.CODE_OK) {

                    toast(res.mMsg)


                } else {

                    res.mBindListBean?.run { updateView(res) }

                }
            }


        })
        LiteHttpManager.getInstance().executeAsync(postRequest)

    }

    private fun updateView(res: BindListResponse) {
        data.clear()

        val bean = res.mBindListBean

        if (0 != bean.loginType) {
            //不是手机登录

            when (bean.loginType) {


                1 -> {
                    if (1 == bean.isWxBind) {

                        val bindType = BindType(BindType.TYPE_TITLE, true, "", "当前登录方式", res.mBindListBean.loginType)
                        data.add(bindType)

                        val bindItem = BindType(BindType.TYPE_WEIXIN, true, bean.wxName, "", bean.loginType)
                        data.add(bindItem)
                    }
                }
                2 -> {

                    val bindType = BindType(BindType.TYPE_TITLE, true, "", "当前登录方式", res.mBindListBean.loginType)
                    data.add(bindType)

                    if (1 == bean.isQQBind) {
                        val bindItem = BindType(BindType.TYPE_QQ, true, bean.qqName, "", bean.loginType)
                        data.add(bindItem)
                    }

                }
                3 -> {

                    val bindType = BindType(BindType.TYPE_TITLE, true, "", "当前登录方式", res.mBindListBean.loginType)
                    data.add(bindType)

                    if (1 == bean.isSinaBind) {
                        val bindItem = BindType(BindType.TYPE_WEIBO, true, bean.sinaName, "", bean.loginType)
                        data.add(bindItem)
                    }
                }

            }
        }


        if (0 == bean.isWxBind || 0 == bean.isQQBind || 0 == bean.isSinaBind) {
            val bindType = BindType(BindType.TYPE_TITLE, false, "", "未绑定账号，绑定后可用于登录", res.mBindListBean.loginType)
            data.add(bindType)


            if (0 == bean.isWxBind) {

                val bindType1 = BindType(BindType.TYPE_WEIXIN, false, "", "", bean.loginType)
                data.add(bindType1)
            }
            if (0 == bean.isQQBind) {

                val bindType2 = BindType(BindType.TYPE_QQ, false, "", "", bean.loginType)
                data.add(bindType2)
            }
            if (0 == bean.isSinaBind) {

                val bindType3 = BindType(BindType.TYPE_WEIBO, false, "", "", bean.loginType)
                data.add(bindType3)
            }
        }

        if (1 == bean.isWxBind || 1 == bean.isQQBind || 1 == bean.isSinaBind) {
            val bindType = BindType(BindType.TYPE_TITLE, true, "", "已绑定", res.mBindListBean.loginType)
            data.add(bindType)


            if (1 == bean.isWxBind) {

                val bindType1 = BindType(BindType.TYPE_WEIXIN, true, bean.wxName, "", bean.loginType)
                data.add(bindType1)
            }
            if (1 == bean.isQQBind) {

                val bindType2 = BindType(BindType.TYPE_QQ, true, bean.qqName, "", bean.loginType)
                data.add(bindType2)
            }
            if (1 == bean.isSinaBind) {

                val bindType3 = BindType(BindType.TYPE_WEIBO, true, bean.sinaName, "", bean.loginType)
                data.add(bindType3)
            }

        }


        adapter.notifyDataSetChanged()
    }

    private val umAuthListener = object : UMAuthListener {
        override fun onStart(platform: SHARE_MEDIA) {
            //授权开始的回调
            Logger.d(platform.toString())
        }


        override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {

            Logger.d(data.toString())
            // 授权成功之后先去获取头像并且上传到服务器

            CommonToast.getInstance("授权成功", Toast.LENGTH_SHORT).show()


            for (key in data.keys) {
                Logger.d("Key: " + key + " Value: " + data.get(key))

            }

            if (platform == SHARE_MEDIA.WEIXIN) {

                val weixinId = data["unionid"]
                val wxName = data["screen_name"]
                val url = ApiContant.REQUEST_BIND_WECHAT
                val map = HashMap<String, String>()
                weixinId?.let { map["unionId"] = weixinId }
                wxName?.let { map["wxName"] = wxName }
                bindOther(url, map)
            } else if (platform == SHARE_MEDIA.QQ) {

                val qqId = data["openid"]
                val qqName = data["screen_name"]
                val url = ApiContant.REQUEST_BIND_QQ
                val map = HashMap<String, String>()
                qqId?.let { map["qqId"] = qqId }
                qqName?.let { map["qqName"] = qqName }
                bindOther(url, map)
            } else if (platform == SHARE_MEDIA.SINA) {

                val sinaId = data["id"]
                val sinaName = data["screen_name"]
                val url = ApiContant.REQUEST_BIND_Sina
                val map = HashMap<String, String>()
                sinaId?.let { map["sinaId"] = sinaId }
                sinaName?.let { map["sinaName"] = sinaName }
                bindOther(url, map)
            }

        }

        override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {

            CommonToast.getInstance("授权失败", Toast.LENGTH_SHORT).show()
        }

        override fun onCancel(platform: SHARE_MEDIA, action: Int) {
            Logger.d(platform.toString())
            CommonToast.getInstance("已取消授权", Toast.LENGTH_SHORT).show()
        }
    }


//    private val umAuthListener = object : UMAuthListener {
//        override fun onStart(platform: SHARE_MEDIA) {
//            //授权开始的回调
//            Logger.d("开始授权")
//        }
//
//        override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {
//            // 授权成功之后先去获取头像并且上传到服务器
//            CommonToast.getInstance("授权成功", Toast.LENGTH_SHORT).show()
//
//            when (platform) {
//                SHARE_MEDIA.WEIXIN -> {
//
//                    for (key in data.keys) {
//                        Logger.d("Key: " + key + " Value: " + data.get(key))
//
//                    }
//                    val weixinId = data["unionid"]
//                    val wxName = data["screen_name"]
//                    val url = ApiContant.REQUEST_BIND_WECHAT
//                    val map = HashMap<String, String>()
//                    weixinId?.let { map["unionId"] = weixinId }
//                    wxName?.let { map["wxName"] = wxName }
//                    bindOther(url, map)
//                }
//                SHARE_MEDIA.SINA -> {
//                    for (key in data.keys) {
//                        Logger.d("Key: " + key + " Value: " + data[key])
//
//                    }
//
//                    val sinaId = data["id"]
//                    val sinaName = data["screen_name"]
//                    val url = ApiContant.REQUEST_BIND_Sina
//                    val map = HashMap<String, String>()
//                    sinaId?.let { map["sinaId"] = sinaId }
//                    sinaName?.let { map["sinaName"] = sinaName }
//                    bindOther(url, map)
//                }
//                SHARE_MEDIA.QQ -> {
//
//                    for (key in data.keys) {
//                        Logger.d("Key: " + key + " Value: " + data.get(key))
//
//                    }
//                    val qqId = data["openId"]
//                    val qqName = data["screen_name"]
//                    val url = ApiContant.REQUEST_BIND_QQ
//                    val map = HashMap<String, String>()
//                    qqId?.let { map["qqId"] = qqId }
//                    qqName?.let { map["qqName"] = qqName }
//                    bindOther(url, map)
//                }
//                else -> {
//                    Logger.d("do nothing")
//                    CommonToast.getInstance("do nothing").show()
//                }
//            }
//        }
//
//        override fun onError(share_media: SHARE_MEDIA, i: Int, throwable: Throwable) {
//
//            CommonToast.getInstance("授权失败", Toast.LENGTH_SHORT).show()
//            Logger.d(throwable.message)
//        }
//
//        override fun onCancel(share_media: SHARE_MEDIA, i: Int) {
//            CommonToast.getInstance("已取消授权", Toast.LENGTH_SHORT).show()
//            Logger.d("已取消授权")
//
//        }
//    }

    //给后台传微信昵称
    private fun bindOther(url: String?, map: HashMap<String, String>) {


        val request = StringRequest(ApiContant.URL_HEAD + url)
        request.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        request.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(map).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) request.addHeader<AbstractRequest<String>>("authToken", sessionKey)

        request.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String, response: Response<String>) {
                super.onSuccess(s, response)

                val responseData = ResponseData(s)
                if (!Utils.isStringEquals(responseData.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(responseData.mMsg).show()
                } else {
                    CommonToast.getInstance("绑定成功").show()
                    requestBindList()
                    EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE))
//                    EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE))

                }
            }

            override fun onFailure(e: HttpException, response: Response<String>) {
                super.onFailure(e, response)

                CommonToast.getInstance(e.toString()).show()
                e.run { Logger.d(e.message) }
            }
        })

        LiteHttpManager.getInstance().executeAsync(request)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
}

