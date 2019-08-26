package com.ciyuanplus.mobile.module.home

import android.widget.Toast
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.manager.CacheManager
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.ActivityListItem
import com.ciyuanplus.mobile.net.bean.BannerItem
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.bean.FriendsItem
import com.ciyuanplus.mobile.net.parameter.*
import com.ciyuanplus.mobile.net.response.*
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.exception.HttpException
import com.litesuits.http.listener.HttpListener
import com.litesuits.http.parser.DataParser
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.CacheMode
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/2/28
 * class  : HomeFragmentPresenter.kt
 * desc   : HomeFragmentPresenter
 * version: 1.0
 */


open class HomeFragmentPresenter @Inject
constructor(var mView: HomeFragmentContract.View) : HomeFragmentContract.Presenter {
    override fun requestActivity() {
    }


    private val mTopList = ArrayList<BannerItem>()
    private val mHotList = ArrayList<FriendsItem>()
    private val pageSize = 20
    private var mNextPage: Int = 0
    private var mLastId = ""
    private val mNewsList = ArrayList<FreshNewItem>()
    private var mIsFreshing = false
    private var mActivityList=ArrayList<ActivityListItem>()


    init {
        mView.showLoadingDialog()

    }


    override fun requestBanner() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_BANNER_LIST_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestBannerApiParameter("2").requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(mView.defaultContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                val response1 = RequestBannerListResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    mTopList.clear()
                    mTopList.addAll(response1.bannerListItem.list)
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
                mView.updateTopView(mTopList)
                requestHeadline()
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                mView.updateTopView(mTopList)
                requestHeadline()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }


    override fun detachView() {}


    override fun doRequest(reset: Boolean) {
        if (reset) {
            mNextPage = 0
            mLastId = ""
            mNewsList.clear()
        }

        requestBanner()
    }

    override fun requestHeadline() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_HEAD_LINE_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestWorldPostsApiParameter("", mNextPage.toString() + "", pageSize.toString(), mLastId, "", "").requestBody)
        // 设置缓存
        postRequest.setCacheMode<AbstractRequest<String>>(CacheMode.NetFirst)
        postRequest.setCacheExpire<AbstractRequest<String>>(-1, TimeUnit.SECONDS)
        postRequest.setCacheDir<AbstractRequest<String>>(CacheManager.getInstance().settleDirectory)
        postRequest.setCacheKey<AbstractRequest<String>>(null)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(mView.defaultContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                // 必须要加的


                val response1 = HomeHotListResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else if (response1.mFriendsListItem.isNotEmpty()) {
                    mHotList.clear()
                    mHotList.addAll(response1.mFriendsListItem)
                }

                mView.updateHeadLine(mHotList)
                requestItemList()
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {

                if (response!!.isCacheHit) { // 中了缓存就不要在折腾了，直接加载数据
                    mHotList.clear()
                    val s = response.getRequest<AbstractRequest<String>>().getDataParser<DataParser<String>>().data
                    val response1 = HomeHotListResponse(s)
                    if (response1.mFriendsListItem != null && response1.mFriendsListItem.isNotEmpty()) {
                        if (mNextPage == 0) mHotList.clear()
                        mHotList.addAll(response1.mFriendsListItem)

                        mView.updateHeadLine(mHotList)
                        requestItemList()
                    }
                }
                super.onFailure(e, response)

            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    override fun requestItemList() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_WORLD_POST_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestWorldPostsApiParameter("", mNextPage.toString() + "", pageSize.toString(), mLastId, "", "").requestBody)
        // 设置缓存
        postRequest.setCacheMode<AbstractRequest<String>>(CacheMode.NetFirst)
        postRequest.setCacheExpire<AbstractRequest<String>>(-1, TimeUnit.SECONDS)
        postRequest.setCacheDir<AbstractRequest<String>>(CacheManager.getInstance().settleDirectory)
        postRequest.setCacheKey<AbstractRequest<String>>(null)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(mView.defaultContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                mView.stopRefreshAndRLoadMore(true)
                val response1 = RequestFreshNewsResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.isNotEmpty()) {

                    mNewsList.addAll(response1.freshNewsListItem.list)
                    if (mNewsList.size > 0) {
                        mLastId = mNewsList[mNewsList.size - 1].id
                    }
                    mView.setLoadMoreEnable(mNewsList.size >= pageSize)

                    mNextPage++
                }

                mView.updateListView(mNewsList)
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {

                mView.stopRefreshAndRLoadMore(true)

                super.onFailure(e, response)

            } })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    override fun requestFollowUser(item: FreshNewItem) {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_FOLLOW_USER_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(FollowOtherApiParameter(item.userUuid).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : HttpListener<String>() {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = RequestOtherInfoResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else {
                    CommonToast.getInstance("关注成功").show()
                    // 更新状态
                    val friendsItem = FriendsItem()
                    friendsItem.uuid = item.userUuid
                    friendsItem.followType = 1
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem))
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                CommonToast.getInstance(mView.defaultContext.resources.getString(R.string.string_get_fresh_news_fail_alert), Toast.LENGTH_SHORT).show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    // 赞新鲜事
    override fun likePost(item: FreshNewItem) {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_zan)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(LikeOperaApiParameter(item.bizType.toString() + "", item.postUuid).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : HttpListener<String>() {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = ResponseData(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else {

                    item.isLike = 1
                    item.likeCount++
                    // 更新状态
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, item))
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                CommonToast.getInstance("操作失败").show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    // 取消赞新鲜事
    override fun cancelLikePost(item: FreshNewItem) {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CANCEL_LIKE_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(ItemOperaApiParameter(item.postUuid).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : HttpListener<String>() {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = ResponseData(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else {
                    item.isLike = 0
                    if (item.likeCount > 0) item.likeCount--
                    // 更新状态
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, item))
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                CommonToast.getInstance("操作失败").show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

}
