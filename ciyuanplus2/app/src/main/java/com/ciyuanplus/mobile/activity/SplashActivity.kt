package com.ciyuanplus.mobile.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import butterknife.ButterKnife
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.inter.MyOnClickListener
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.statistics.UpLoadLogManager
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_splash.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*

/**
 * Created by Alen on 2017/5/9.
 * 闪屏页面
 */

class SplashActivity : MyBaseActivity(), EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private var mCountDownTime = 3

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MESSAGE_LOGIN) {
                LoginStateManager.tryToLogin()
            } else if (msg.what == MESSAGE_COUNT_DOWN) {// 倒计时
                mCountDownTime--
                m_splash_skip_btn!!.text = String.format(Locale.getDefault(), "%ds跳过", mCountDownTime)
                goNextActivity()
            } else if (msg.what == MESSAGE_SELECT_SEX) {
                removeCallbacksAndMessages(null)
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取权限
        storageAndCameraTask()
    }

    private fun initView() {
        val hasOpen = SharedPreferencesManager.getBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_HAS_OPEN, false)
//        requestSplashImage()

        if (!hasOpen) {// 如果是第一次打开app
            val intent = Intent(this@SplashActivity, GuideActivity::class.java)
            startActivity(intent)
            //j记录下已经打开过该app
            SharedPreferencesManager.putBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_HAS_OPEN, true)
            finish()
        } else {
            setContentView(R.layout.activity_splash)
            ButterKnife.bind(this)

            m_splash_img.setOnClickListener(object : MyOnClickListener() {
                public override fun performRealClick(v: View) {
                    val link = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_LINK_URL, "")

                    mHandler.removeMessages(MESSAGE_COUNT_DOWN)
                    mCountDownTime = 0
                    mHandler.removeCallbacksAndMessages(null)

                    if (!Utils.isStringEmpty(link)) {
                        val intent = Intent(this@SplashActivity, JsWebViewActivity::class.java)
                        intent.putExtra(Constants.INTENT_OPEN_URL, link)
                        startActivity(intent)
                    }
                }
            })
            m_splash_skip_btn.setOnClickListener(object : MyOnClickListener() {
                public override fun performRealClick(v: View) {
                    mHandler.removeMessages(MESSAGE_COUNT_DOWN)
                    mCountDownTime = 0
                    mHandler.removeCallbacksAndMessages(null)
                    goNextActivity()
                }
            })


            showSplashImage()
        }

        initLogUpload()// 上传上一次保存在本地的log
    }

    private fun initLogUpload() {
        UpLoadLogManager.getInstance().upload()
    }

    private fun goNextActivity() {

        when {
            mCountDownTime > 0 -> mHandler.sendEmptyMessageDelayed(MESSAGE_COUNT_DOWN, 1000) // 倒计时
            LoginStateManager.isLogin() -> mHandler.sendEmptyMessageDelayed(MESSAGE_LOGIN, 0)
            else -> mHandler.sendEmptyMessage(MESSAGE_SELECT_SEX)
        }

    }



    // 闪屏图片加载逻辑
    private fun showSplashImage() {
        val path = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE_PATH, "")
        val file = File(path)

        Logger.d("path = $path")
        val options: RequestOptions
        if (Utils.isStringEmpty(path) && !file.exists()) {
//            options = RequestOptions().dontAnimate()
//            Glide.with(App.mContext).load(R.drawable.default_splash).apply(options).into(m_splash_img)
            mCountDownTime = SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET,
                    Constants.SHARED_PREFERENCES_FLASH_TIMEOUT, 0) // 默认3s倒计时
            goNextActivity()
            m_splash_skip_btn.visibility = View.GONE
        } else {
            m_splash_skip_btn.visibility = View.VISIBLE
            options = RequestOptions().dontAnimate()
            Glide.with(this).load(path).apply(options).into(m_splash_img)
            mCountDownTime = SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET,
                    Constants.SHARED_PREFERENCES_FLASH_TIMEOUT, mCountDownTime) // 默认3s倒计时
            m_splash_skip_btn.text = mCountDownTime.toString() + "s跳过"
            goNextActivity()
        }

    }



    @AfterPermissionGranted(RC_STORAGE_AND_CAMERA_PERM)
    fun storageAndCameraTask() = if (hasStorageAndCameraPermission()) {
        // Have permission, do the thing!
        initView()
    } else {

        EasyPermissions.requestPermissions(
                this,
                "应用需要获取存储权限和相机功能才能正常使用",
                RC_STORAGE_AND_CAMERA_PERM,
                *STORAGE_AND_CAMERA)
    }

    private fun hasStorageAndCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, *STORAGE_AND_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, @NonNull perms: List<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
//        initView()
    }

    override fun onPermissionsDenied(requestCode: Int, @NonNull perms: List<String>) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size)

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).setTitle(resources.getString(R.string.title_settings_dialog)).setRationale(resources.getString(R.string.rationale_ask_again)).build().show()
        }else{
            AppUtils.exitApp()
            System.exit(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasStorageAndCameraPermission()) {
                initView()
            } else {
                AppUtils.exitApp()
                System.exit(0)
            }
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.d(TAG, "onRationaleAccepted:$requestCode")
    }

    override fun onRationaleDenied(requestCode: Int) {
        Log.d(TAG, "onRationaleDenied:$requestCode")
    }

    companion object {
        private const val MESSAGE_LOGIN = 10000
        private const val MESSAGE_SELECT_SEX = 10002
        private const val MESSAGE_COUNT_DOWN = 10003
        private const val TAG = "SplashActivity"
        private val STORAGE_AND_CAMERA = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        private const val RC_STORAGE_AND_CAMERA_PERM = 124
    }



}
