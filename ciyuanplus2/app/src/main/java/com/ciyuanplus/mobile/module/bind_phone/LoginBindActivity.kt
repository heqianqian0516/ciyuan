package com.ciyuanplus.mobile.module.bind_phone

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.activity.MainActivityNew
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.net.response.LoginResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import kotlinx.android.synthetic.main.activity_lgoin_bind.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class LoginBindActivity : MyBaseActivity(), LoginBindPhoneContract.View {


    private val mBindPhoneAccountView: EditText by lazy { m_bind_phone_account_view }
    private val mBindPhoneVerifyView: EditText by lazy { m_bind_phone_verify_view }
    private val mBindPhoneSendVerifyText: TextView by lazy { m_bind_phone_send_verify_text }
    private var mType = 0

    @Inject
    lateinit var mPresenter: LoginBindPhonePresenter
    private var mBindType = 0

    private var mOtherPlatformId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lgoin_bind)
        if (intent != null && intent.extras != null) {
            mBindType = intent.getIntExtra(Constants.INTENT_BIND_MOBILE, 0)
            mType = intent.getIntExtra(Constants.SHARED_PREFERENCES_LOGIN_TYPE, -1)
            mOtherPlatformId = intent.getStringExtra(Constants.SHARED_PREFERENCES_OTHER_PLATRORM_ACCOUNT)
        }

        this.initView()
    }

    private fun initView() {
        ButterKnife.bind(this)
        DaggerLoginPhonePresenterComponent.builder().loginPhonePresenterModule(LoginPhonePresenterModule(this)).build().inject(this)

        title_bar.setOnBackListener(View.OnClickListener {
            startActivity<LoginActivity>()
            finish()
        })

    }

    override fun getResetSendCode(isOnclick: Boolean, timeCount: Int) {
        if (isOnclick) {
            this@LoginBindActivity.mBindPhoneSendVerifyText.text = resources.getString(R.string.string_forget_password_resend_verify_alert)
            this@LoginBindActivity.mBindPhoneSendVerifyText.isClickable = true
        } else {
            this@LoginBindActivity.mBindPhoneSendVerifyText.text = "$timeCount${resources.getString(R.string.string_forget_password_resend_verify_hint)}"
            this@LoginBindActivity.mBindPhoneSendVerifyText.isClickable = false
        }
    }

    override fun showSuccessMsg() {

        if (mBindType == 1) {
            CommonToast.getInstance("绑定手机号成功").show()
        } else {
            CommonToast.getInstance(resources.getString(R.string.string_my_profile_change_phone_success_alert), Toast.LENGTH_SHORT).show()

        }

    }


    @OnClick(R.id.m_bind_phone_close_btn, R.id.m_bind_phone_send_verify_text, R.id.m_bind_phone_confirm_text)
    override fun onViewClicked(view: View) {
        super.onViewClicked(view)
        when (view.id) {
            R.id.m_bind_phone_close_btn -> {
                startActivity<LoginActivity>()
                finish()
            }
            R.id.m_bind_phone_send_verify_text -> {
                val mPhone = mBindPhoneAccountView.text.toString()
                if (Utils.isStringEmpty(mPhone)) {
                    CommonToast.getInstance(resources.getString(R.string.string_my_profile_change_phone_empty_alert)).show()
                    return
                }
                if (Utils.isStringEquals(mPhone, UserInfoData.getInstance().userInfoItem.mobile)) {
                    CommonToast.getInstance("当前手机号已存在").show()
                    return
                }
                mPresenter.sendCode(mPhone)
            }
            R.id.m_bind_phone_confirm_text -> {
                val phone = mBindPhoneAccountView.text.toString()
                if (Utils.isStringEmpty(phone)) {
                    CommonToast.getInstance(resources.getString(R.string.string_my_profile_change_phone_empty_alert)).show()
                    return
                }
                val mCode = mBindPhoneVerifyView.text.toString()
                if (Utils.isStringEmpty(mCode)) {
                    CommonToast.getInstance(resources.getString(R.string.string_my_profile_change_verify_empty_alert)).show()
                    return
                }
                mPresenter.changePhone(phone, mCode)
            }
        }
    }

    override fun jumpToMain(response1: LoginResponse) {
        SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_TYPE, mType)
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_OTHER_PLATRORM_ACCOUNT, mOtherPlatformId)

        UserInfoData.getInstance().insertOrReplace(response1.userInfoItem.uuid, response1.rawData)
        LoginStateManager.saveLoginInfo(response1.userInfoItem.uuid, "", response1.userInfoItem.uuid, response1.token)
        LoginStateManager.registerUmengPushDeviceToken()
        CommonToast.getInstance("绑定成功").show()
        SharedPreferencesManager.putString("User", "uuid", response1.userInfoItem.uuid)
        SharedPreferencesManager.putString("Pass", "communityUuid", response1.userInfoItem.currentCommunityUuid)
        SharedPreferencesManager.putString("ShaZi", "tok", response1.token)
        SharedPreferencesManager.putString("MyAddress", "address", response1.userInfoItem.currentCommunityName)

        startActivity<MainActivityNew>()
    }

    override fun getDefaultContext(): Context {
        return this
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<LoginActivity>()
        finish()
    }
}
