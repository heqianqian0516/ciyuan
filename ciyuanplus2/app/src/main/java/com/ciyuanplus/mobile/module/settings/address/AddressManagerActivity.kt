package com.ciyuanplus.mobile.module.settings.address

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.R2.layout.item
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat
import com.ciyuanplus.mobile.inter.MyOnClickListener
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.GetCommunityListApiParameter
import com.ciyuanplus.mobile.net.response.AddressListResponse
import com.ciyuanplus.mobile.net.response.GetCommunitListResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import crossoverone.statuslib.StatusUtil
import kotlinx.android.synthetic.main.activity_address_manager.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/22
 * class  : AddressManagerActivity.kt
 * desc   : 地址管理
 * version: 1.0
 */


open class AddressManagerActivity : MyBaseActivity(), AddressManagerContract.View, EventCenterManager.OnHandleEventListener {


    @Inject
    open lateinit var mPresenter: AddressManagerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_address_manager)
       /* StatusBarCompat.compat(this, resources.getColor(R.color.title))*/
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"))

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true)
        this.initView()
        mPresenter.initData()

    }

    private fun initView() {

        DaggerAddressManagerPresenterComponent.builder()
                .addressManagerPresenterModule(AddressManagerPresenterModule(this)).build().inject(this)

        titleBar.setOnBackListener(object : MyOnClickListener() {
            public override fun performRealClick(v: View) {

                onBackPressed()
            }
        })

        titleBar.setTitle("地址管理")

        val linearLayoutManager = LinearLayoutManager(this)//   LinearLayoutManager不能共用 真是坑爹
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        addressList!!.layoutManager = linearLayoutManager

        addAddressButton.setOnClickListener { startActivity<AddAddressActivity>( Constants.INTENT_IS_EDIT_MODE to AddAddressActivity.noEditMode) }


        // 添加小区列表变化的监听事件
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this)// 如果用户信息有变化  需要更新下界面
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH, this)
    }

    override fun onResume() {
        super.onResume()

        mPresenter.getAddressList()
    }


    override fun onDestroy() {
        super.onDestroy()
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH, this)
    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        mPresenter.handleEvent(eventMessage)
    }

    override fun getRecyclerView(): RecyclerView? {
        return addressList
    }

    override fun showContent() {
        addressList.visibility = View.VISIBLE
        noAddressLayout.visibility = View.GONE
    }

    override fun showEmptyView() {
        addressList.visibility = View.GONE
        noAddressLayout.visibility = View.VISIBLE
    }

    override fun getDefaultContext(): Context {
        return this@AddressManagerActivity
    }
}
