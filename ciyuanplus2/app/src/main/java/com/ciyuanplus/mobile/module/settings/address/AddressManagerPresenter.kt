package com.ciyuanplus.mobile.module.settings.address

import android.widget.Toast
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.R2.layout.item
import com.ciyuanplus.mobile.adapter.MyAddressAdapterNew
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.AddressItem
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
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
import org.jetbrains.anko.startActivity
import javax.inject.Inject

/**
 * Created by Alen on 2017/12/11.
 */

open class AddressManagerPresenter @Inject
constructor(var mView: AddressManagerContract.View) : AddressManagerContract.Presenter {


    private var mCommunityItems = ArrayList<AddressItem>()
    private lateinit var mAdapter: MyAddressAdapterNew

    override fun initData() {// 初始化数据，
        mAdapter = MyAddressAdapterNew(mCommunityItems)

        mAdapter.setOnItemChildClickListener { adapter, view, position ->

            when (view.id) {
                R.id.btnDelete -> {
                    removeAddress(position)
                }
                R.id.deleteLayout -> {
//                    CommonToast.getInstance("什么鬼情况。。。。。。").show()
                }
                R.id.contentLayout->{

                    val item = adapter.getItem(position) as AddressItem
                    mView.defaultContext.startActivity<AddAddressActivity>(
                            Constants.INTENT_IS_EDIT_MODE to AddAddressActivity.isEditMode
                            , Constants.INTENT_USER_NAME to item.name
                            , Constants.INTENT_USER_PHONE to item.mobile
                            , Constants.INTENT_USER_ADDRESS to item.address
                            , Constants.INTENT_ADDRESS_ID to item.id)
                }
                else -> {
//                    CommonToast.getInstance("什么鬼情况。。。。。。").show()
                }
            }
        }
        mView.getRecyclerView()?.adapter = mAdapter
    }

    override fun handleEvent(eventMessage: EventCenterManager.EventMessage) {

        mAdapter.notifyDataSetChanged()
    }

    override fun getAddressList() {

        mCommunityItems.clear()
        if (!LoginStateManager.isLogin()) {
            return
        }
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_ADDRESS_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(GetCommunityListApiParameter().requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = GetCommunitListResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    val addRes = AddressListResponse(s)

                    if (addRes.mItemList.size > 0) {
                        mCommunityItems.addAll(addRes.mItemList)
                        mView.showContent()

                    } else {
                        mView.showEmptyView()
                    }

                    mAdapter.notifyDataSetChanged()
                    Logger.d(s)
                    //通知UI改变
                    EventCenterManager.asynSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH))

                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)


                Logger.d(e.toString())
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    override fun removeAddress(position: Int) {

        val map = HashMap<String, String>()
        map["addressId"] = mAdapter.getItem(position)?.id.toString()

        val postRequest = StringRequest(ApiContant.URL_HEAD + "/api/server/shoppingAddress/delShoppingAddress")
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(map).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(mView.defaultContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = ResponseData(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    mCommunityItems.removeAt(position)


                    if (mCommunityItems.size > 0) {
                        mView.showContent()

                    } else {
                        mView.showEmptyView()
                    }
                    mAdapter.notifyDataSetChanged()


                } else {
                    CommonToast.getInstance(response1.mMsg).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)

            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    override fun detachView() {}
}
