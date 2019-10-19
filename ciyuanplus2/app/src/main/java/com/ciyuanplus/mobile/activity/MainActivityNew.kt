package com.ciyuanplus.mobile.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.transition.Explode
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.activity.mine.MyProfileActivity
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat
import com.ciyuanplus.mobile.manager.*
import com.ciyuanplus.mobile.module.home.HomeFragment
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.mine.mine.MineFragmentNew
import com.ciyuanplus.mobile.module.search.SearchActivity
import com.ciyuanplus.mobile.module.search.SearchActivity.Companion.SEARCH_TYPE_COMMODITY
import com.ciyuanplus.mobile.module.search.SearchActivity.Companion.SEARCH_TYPE_USER
import com.ciyuanplus.mobile.module.store.ShoppingMallFragment
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
import com.ciyuanplus.mobile.net.parameter.GetNoticeCountApiParameter
import com.ciyuanplus.mobile.net.parameter.GetShareConfigApiParameter
import com.ciyuanplus.mobile.net.response.GetAppConfigResponse
import com.ciyuanplus.mobile.net.response.NoticeCountResponse
import com.ciyuanplus.mobile.statistics.StatisticsManager
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.ciyuanplus.mobile.widget.CustomDialog
import com.jaeger.library.StatusBarUtil
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.statistics.common.DeviceConfig
import crossoverone.statuslib.StatusUtil
import kotlinx.android.synthetic.main.activity_main_new.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Alen on 2017/5/9.
 */

class MainActivityNew : MyBaseActivity(), EventCenterManager.OnHandleEventListener {

    private var mSelectedTab = 0
    private var mShoppingMallFragment: ShoppingMallFragment? = null
    private var mMineFragment: MineFragmentNew? = null
    private var myHomeFragment: HomeFragment? = null
    private var mSystemMessageCount: Int = 0
    //再按一次退出程序
    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main_new)
        requestSplashImage()
//        Logger.d(getTestDeviceInfo(this))



        if (savedInstanceState != null) {
            //            mNewsFragment = (NewsFragment) fm.findFragmentByTag("0");
            //            mAroundFragment = (AroundFragment) fm.findFragmentByTag("1");
            //            homePageFragment = (HomePageFragment) fm.findFragmentByTag("4");

            mShoppingMallFragment = supportFragmentManager.findFragmentByTag("2")?.let { it as ShoppingMallFragment }
            mMineFragment = supportFragmentManager.findFragmentByTag("3")?.let { it as MineFragmentNew }
            myHomeFragment = supportFragmentManager.findFragmentByTag("5")?.let { it as HomeFragment }
        }

        AppVersionManager.requestAppVersion(this)

        initView()
        mSelectedTab = intent.getIntExtra(Constants.INTENT_ACTIVITY_TYPE, 0)

        this.initData()
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT, this)//
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_RONG_HOT_RED_DOT, this)//
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_USER_FORCE_LOGOUT, this)//
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this)// 如果用户信息有变化  需要更新下界面

    }

    private fun initView() {
        ButterKnife.bind(this)

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mSelectedTab = getIntent().getIntExtra(Constants.INTENT_ACTIVITY_TYPE, 0)
        switchFragment()
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mSelectedTab = savedInstanceState.getInt("position")
        switchFragment()
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //记录当前的position
        super.onSaveInstanceState(outState)
        outState.putInt("position", mSelectedTab)
    }

    /**
     * 切换fragment
     * base on mSelectedTab;
     */
    private fun switchFragment() {

        GSYVideoManager.releaseAllVideos()
        val transaction = supportFragmentManager.beginTransaction()
        mShoppingMallFragment?.let { transaction.hide(it) }
        mMineFragment?.let { transaction.hide(it) }
        myHomeFragment?.let { transaction.hide(it) }

        when (mSelectedTab) {
            0 -> {


                if (myHomeFragment == null) {//重复利用，否则会oom
                    myHomeFragment = HomeFragment()
                    transaction.add(R.id.m_main_fragment_container, myHomeFragment!!, "5")
                }
                transaction.show(myHomeFragment!!)
            }
            1 -> {
                if (mShoppingMallFragment == null) {//重复利用，否则会oom
                    mShoppingMallFragment = ShoppingMallFragment()
                    transaction.add(R.id.m_main_fragment_container, mShoppingMallFragment!!, "2")
                }
                transaction.show(mShoppingMallFragment!!)
            }
            2 -> {
                if (mMineFragment == null) {//重复利用，否则会oom
                    mMineFragment = MineFragmentNew()
                    transaction.add(R.id.m_main_fragment_container, mMineFragment!!, "3")
                }
                transaction.show(mMineFragment!!)
            }
        }
        transaction.commitAllowingStateLoss()// 使用 commitAllowingStateLoss 代替commit 解决bug: Can not perform this action after onSaveInstanceState
    }

    // 默认选中第一个tab
    private fun initData() {
        changeTabSelected(mSelectedTab)

    }

    /**
     * 切换tab时候的响应函数
     * which ：被点击的tab种类、int
     */
    private fun changeTabSelected(which: Int) {
        mSelectedTab = which
        when (mSelectedTab) {
            0 -> {
                m_main_news_tab_lp.isSelected = true
                m_main_chat_tab_lp.isSelected = false
                m_main_mine_tab_lp.isSelected = false
            }
            1 -> {
                m_main_news_tab_lp.isSelected = false
                m_main_chat_tab_lp.isSelected = true
                m_main_mine_tab_lp.isSelected = false
            }
            2 -> {
                m_main_news_tab_lp.isSelected = false
                m_main_chat_tab_lp.isSelected = false
                m_main_mine_tab_lp.isSelected = true
            }
        }
        initTitleBar(mSelectedTab)
        switchFragment()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {

            if (GSYVideoManager.backFromWindowFull(this)) {
                return true
            }
            if (System.currentTimeMillis() - exitTime > 2000) {
                CommonToast.getInstance("再按一次退出程序", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
                MobclickAgent.onKillProcess(this) // Umeng 统计， 及时保存信息

                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT, this)//
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_RONG_HOT_RED_DOT, this)//
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_USER_FORCE_LOGOUT, this)//
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this)
    }


    private fun requestNoticeCount() {
        // 获取临时用户的系统消息个数
        if (!LoginStateManager.isLogin()) {
            val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SYSTEM_MESSAGE_COUNT_URL)
            postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
            postRequest.setHttpBody<AbstractRequest<String>>(GetNoticeCountApiParameter().requestBody)
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
                override fun onSuccess(s: String?, response: Response<String>?) {
                    super.onSuccess(s, response)
                    val response1 = NoticeCountResponse(s)
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        updateUnReadMessage(response1.countItem.postCount + response1.countItem.followCount
                                + response1.countItem.systemMessageCount + response1.countItem.feedbackMessageCount)
                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
            LiteHttpManager.getInstance().executeAsync(postRequest)

        } else {
            val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_NOTICE_COUNT_URL)
            postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
            postRequest.setHttpBody<AbstractRequest<String>>(GetNoticeCountApiParameter().requestBody)
            val sessionKey = SharedPreferencesManager.getString(
                    Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
            if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
                override fun onSuccess(s: String?, response: Response<String>?) {
                    super.onSuccess(s, response)
                    val response1 = NoticeCountResponse(s)
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        updateUnReadMessage(response1.countItem.postCount + response1.countItem.followCount
                                + response1.countItem.systemMessageCount + response1.countItem.feedbackMessageCount)
                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
            LiteHttpManager.getInstance().executeAsync(postRequest)
        }
    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT) {
            //            mMainNewsChatDot.setVisibility(View.VISIBLE);
            requestNoticeCount()
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_RONG_HOT_RED_DOT) {
            //            mMainNewsChatDot.setVisibility(View.VISIBLE);
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_USER_FORCE_LOGOUT) {
            runOnUiThread { showForceLogoutDialog() }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE) {
        }
    }


    private fun updateUnReadMessage(i: Int) {

        mSystemMessageCount = i
        title_bar.setNotificationNumber(mSystemMessageCount)
    }

    private fun initTitleBar(index: Int) {

        title_bar.setRightImageVisible(View.VISIBLE)

        when (index) {

            0 -> {

                title_bar.setTitleVisible(true)
                title_bar.setTitle("次元PLUS")
//                title_bar.setNotificationNumber(0)
                title_bar.setNotificationClickListener(View.OnClickListener {

                    val intent = Intent(this@MainActivityNew, MessageCenterActivity::class.java)
                    startActivity(intent)
                })

                title_bar.setLefButtonImage(R.drawable.icon_search_title_bar)
                title_bar.setLeftImageListener(View.OnClickListener {
                    val intent = Intent(this@MainActivityNew, SearchActivity::class.java)
                    intent.putExtra(Constants.INTENT_SEARCH_TYPE, SEARCH_TYPE_USER)
                    startActivity(intent)

                })
            }
            1 -> {

                title_bar.setTitleVisible(true)
                title_bar.setTitle("次元PLUS")
//                title_bar.setNotificationNumber(0)

                title_bar.setNotificationClickListener(View.OnClickListener {
                    val intent = Intent(this@MainActivityNew, MessageCenterActivity::class.java)
                    startActivity(intent)
                })

                title_bar.setLefButtonImage(R.drawable.icon_search_title_bar)

                title_bar.setLeftImageListener(View.OnClickListener {
                    val intent = Intent(this@MainActivityNew, SearchActivity::class.java)
                    intent.putExtra(Constants.INTENT_SEARCH_TYPE, SEARCH_TYPE_COMMODITY)
                    startActivity(intent)
                })
            }
            2 -> {
                title_bar.setTitleVisible(false)
//                title_bar.setNotificationNumber(0)
                title_bar.setLefButtonImage(R.drawable.icon_my_setting)
                title_bar.setNotificationClickListener(View.OnClickListener {

                    val intent = Intent(this@MainActivityNew, MessageCenterActivity::class.java)
                    startActivity(intent)
                })
                title_bar.setLeftImageListener(View.OnClickListener {
                    val intent = Intent(this@MainActivityNew, MyProfileActivity::class.java)
                    intent.putExtra(Constants.INTENT_SEARCH_TYPE, SEARCH_TYPE_USER)
                    startActivity(intent)
                })
            }
        }
    }


    private fun showForceLogoutDialog() {
        val builder = CustomDialog.Builder(this)
        builder.setMessage("您的账号在其他设备登陆")
        builder.setPositiveButton("确定") { dialog, which ->
            dialog.dismiss()
            if (LoginStateManager.isLogin()) {
                LoginStateManager.logout()
            }
            val intent = Intent(App.mContext, LoginActivity::class.java)//去登陆页面
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            App.mContext.startActivity(intent)
        }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    @OnClick(R.id.m_main_news_tab_lp, R.id.m_main_chat_tab_lp, R.id.m_main_mine_tab_lp)
    override fun onViewClicked(view: View) {
        super.onViewClicked(view)
        when (view.id) {
            R.id.m_main_news_tab_lp -> {
                changeTabSelected(0)
                initTitleBar(0)
            }
            R.id.m_main_chat_tab_lp -> {
                changeTabSelected(1)
                initTitleBar(1)
            }
            R.id.m_main_mine_tab_lp -> {
                changeTabSelected(2)
                initTitleBar(2)
            }
        }
    }


    private fun requestAppConfig() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_APP_CONFIG)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(GetShareConfigApiParameter().requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = GetAppConfigResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    response1.appConfigItem?.let {
                        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_SHARE_TITLE, it.shareLinkTitle)
                        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_SHARE_LINK, it.mobilePortalUrl)
                        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_SHARE_CONTENT, it.shareLinkContent)
                        SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_TIMEOUT, it.flashPicTimeout)
                    }
                    if (!Utils.isStringEmpty(response1.postTypeString)) {
                        PostTypeManager.getInstance().insertOrReplace(response1.postTypeString)
                    }
                    if (!Utils.isStringEmpty(response1.wikiTypeDictString)) {
                        WikiTypeManager.getInstance().insertOrReplace(response1.wikiTypeDictString)
                    }
                }  //CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();

            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    fun showBottomBar() {
        bottomBarLayout.visibility = View.VISIBLE
        view_line.visibility = View.VISIBLE
        window.decorView.requestLayout()
    }

    fun hideBottomBar() {
        bottomBarLayout.visibility = View.GONE
        view_line.visibility = View.GONE
        window.decorView.requestLayout()
    }



    override fun onResume() {
        super.onResume()
        requestNoticeCount()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.releaseAllVideos()
    }

    private fun requestSplashImage() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + "/api/main/getActivityFlash")
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)

        val map = HashMap<String, String>()
        map["showSection"] = "0"
        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(map).requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = GetAppConfigResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    Logger.d("appConfigItem ${response1.appConfigItem}")
                    if (null == response1.appConfigItem) {
                        val path = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE_PATH, "")
                        val file = File(path)
                        Logger.d("图片地址 $path")
                        if (file.exists() && file.isFile) {
                            val delete = file.delete()
                            file.deleteOnExit()
                            file.deleteRecursively()
                            Logger.d("path = $path   ${if (delete) "删除成功" else "删除失败"}")
//                            FileUtils.delete(file)
//                            FileUtils.delete(path)
//                            FileUtils.deleteFile(path)
                        }
                        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE_PATH, "")

                    } else {
                        response1.appConfigItem?.let {

                            it.flashPicImage?.let { it2 -> downloadSplashImage(it2, it.flashPicUrl) }

                        }
                    }
                }
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    // 下载闪屏图片  准备下次使用
    private fun downloadSplashImage(url: String, linkUrl: String?) {

        if (Utils.isStringEmpty(url)) return
        val savedUrl = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE, "")

        if (Utils.isStringEquals(url, savedUrl)) {// 防止重复下载
            val fileName = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE_PATH, "")
            val file = File(fileName)
            if (file.exists()) return
        }
        val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        if (this.isDestroyed || this.isFinishing) {
            return
        }

        Glide.with(this).asBitmap().load(Constants.IMAGE_LOAD_HEADER + url).apply(options).listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, isFirstResource: Boolean): Boolean {

                Logger.d(e?.message)
                return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: com.bumptech.glide.request.target.Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE, url)
                SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_LINK_URL, linkUrl)
                Logger.d("成功")
                resource?.let { saveImageToCard(it) }
                return false
            }


        }).submit(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
    }


    private fun saveImageToCard(bmp: Bitmap) {
        // 首先保存图片
        val appDir = File(Environment.getExternalStorageDirectory(), Constants.SETTLE_FILE_PATH)
        if (!appDir.exists()) {
            val mkdir = appDir.mkdir()
            if (!mkdir) return
        }
        val fileName = "splash_img" + System.currentTimeMillis() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "")
            e.printStackTrace()
        } catch (e: IOException) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "")
            e.printStackTrace()
        }

        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE_PATH, file.absolutePath)
    }

    fun getTestDeviceInfo(context: Context?): Array<String?> {
        val deviceInfo = arrayOfNulls<String>(2)
        try {
            if (context != null) {


                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context)
                deviceInfo[1] = DeviceConfig.getMac(context)
            }
        } catch (e: Exception) {
        }
        return deviceInfo
    }
}
