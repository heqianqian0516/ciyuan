package com.ciyuanplus.mobile.module.store


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.StringUtils
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.activity.MessageCenterActivity
import com.ciyuanplus.mobile.loader.GlideImageLoader
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.search.SearchActivity
import com.ciyuanplus.mobile.module.store.commodity_list.CommodityItemFragment
import com.ciyuanplus.mobile.module.store.shop_car.ShopCarActivity
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.BannerItem
import com.ciyuanplus.mobile.net.parameter.AutoLoginApiParameter
import com.ciyuanplus.mobile.net.parameter.GetNoticeCountApiParameter
import com.ciyuanplus.mobile.net.parameter.RequestBannerApiParameter
import com.ciyuanplus.mobile.net.parameter.SaveDeviceTokenParameter
import com.ciyuanplus.mobile.net.response.CategoryResponse
import com.ciyuanplus.mobile.net.response.NoticeCountResponse
import com.ciyuanplus.mobile.net.response.RequestBannerListResponse
import com.ciyuanplus.mobile.statistics.StatisticsManager
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.kris.baselibrary.base.LazyLoadBaseFragment
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_mine_new.*
import kotlinx.android.synthetic.main.fragment_shopping_mall.*
import kotlinx.android.synthetic.main.fragment_shopping_mall.smartRefreshLayout
import kotlinx.android.synthetic.main.home_bar.*
import kotlinx.android.synthetic.main.title_bar.view.*
import org.jetbrains.anko.support.v4.startActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * 商城页面
 * */
class ShoppingMallFragment : LazyLoadBaseFragment(), ViewPager.OnPageChangeListener, EventCenterManager.OnHandleEventListener {


    private var mTitles = ArrayList<String>()
    private var mIds = ArrayList<String>()
    private var mFragments = ArrayList<CommodityItemFragment>()
    private var mAdapter: FragmentPagerAdapter? = null
    private val mTopList = ArrayList<BannerItem>()
    private var selectTab = 0
    private var mSystemMessageCount: Int = 0
    override fun lazyLoad() {

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_shopping_mall
    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage?) {
        when (eventMessage?.mEvent) {
            Constants.EVENT_MESSAGE_PAY_SUCCESS -> requestCount()

        }

        if (eventMessage?.mEvent == Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT) {
            //            mMainNewsChatDot.setVisibility(View.VISIBLE);
            requestNoticeCount()
        }
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        requestCount()
    }
    override fun onResume() {
        super.onResume()
        requestNoticeCount()
    }
    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        requestBanner()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_mall, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contentView.addOnPageChangeListener(this@ShoppingMallFragment)
        initData()
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_PAY_SUCCESS, this)
    }

    private fun initData() {

        smartRefreshLayout.setOnRefreshListener {

            Logger.d("刷新")
            requestBanner()
        }

        smartRefreshLayout.setOnLoadMoreListener {

            val commodityItemFragment = mFragments[selectTab]
            commodityItemFragment.loadMore()
        }

        smartRefreshLayout.isNestedScrollingEnabled = true
        shopCarLayout.setOnClickListener { startActivity<ShopCarActivity>() }
        image_search?.setOnClickListener(){
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra(Constants.INTENT_SEARCH_TYPE, SearchActivity.SEARCH_TYPE_USER)
            startActivity(intent)
        }
        iv_notification?.setOnClickListener(){
            requestNoticeCount()
            val intent = Intent(activity, MessageCenterActivity::class.java)
            startActivity(intent)
        }
    }
    fun setNotificationNumber(count: Int) {

        if (count > 0) {
            tv_notification_num.visibility = View.VISIBLE
            tv_notification_num.text = count.toString()
        } else {
            tv_notification_num.visibility = View.GONE
        }
    }
    private fun updateUnReadMessage(i: Int) {

        mSystemMessageCount = i
        //title_bars.setNotificationNumber(mSystemMessageCount)
        setNotificationNumber(mSystemMessageCount)


    }

    private fun requestNoticeCount() {
        // 获取临时用户的系统消息个数
        if (!LoginStateManager.isLogin()) {
            val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SYSTEM_MESSAGE_COUNT_URL)
            postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
            postRequest.setHttpBody<AbstractRequest<String>>(GetNoticeCountApiParameter().requestBody)
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
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
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
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

    private fun requestBanner() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_BANNER_LIST_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestBannerApiParameter("3").requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                Logger.d("banner $s")
                val response1 = RequestBannerListResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    mTopList.clear()
                    mTopList.addAll(response1.bannerListItem.list)
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }

                banner.visibility = if (mTopList.size == 0) View.GONE else View.VISIBLE
                requestTypes()
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                banner.visibility = if (mTopList.size == 0) View.GONE else View.VISIBLE
                requestTypes()

            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    fun updateTopView(bannerList: ArrayList<BannerItem>) {

        val images = ArrayList<String>()
        for (item in bannerList) {
            images.add(Constants.IMAGE_LOAD_HEADER + item.img)
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        banner.setImageLoader(GlideImageLoader())
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage)
//        设置自动轮播，默认为true
        this.banner.isAutoPlay(true)
//        设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.setOnBannerListener {

            clickBanner(bannerList[it])
        }
        //设置图片集合
        banner.setImages(images)
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }

    private fun requestCount() {

        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")

        val userUuid = UserInfoData.getInstance().userInfoItem.uuid

        val postRequest = StringRequest(ApiContant.URL_HEAD + "/api/server/cartItem/getCartCount")

        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpBody<AbstractRequest<String>>(AutoLoginApiParameter(userUuid, "").requestBody)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                Logger.d(s)
                Logger.d(response.toString())
                val res = CountResponse(s)
                if (res.count > 0) {

                    newMsg.visibility = View.VISIBLE
                    newMsg.text = if (res.count > 100) "99+" else res.count.toString()
                } else {
                    newMsg.visibility = View.GONE
                }
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)

    }

    private fun clickBanner(banner: BannerItem) {

        when (banner.type) {
            0 -> {
            }
            1 -> {
//                startActivity<>(Constants.INTENT_OPEN_URL to banner.param)
                startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to banner.param)
            }
        }
    }

    private fun requestTypes() {

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COMMODITY_TYPE)
        Logger.d("分类url = ${ApiContant.URL_HEAD + ApiContant.REQUEST_COMMODITY_TYPE}")
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(SaveDeviceTokenParameter("").requestBody)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                //子fragment取消刷新
//                val response1 = CategoryRes(s)
                val ss = StringUtils.toDBC(s)
                val response1 = JSON.parseObject(ss, CategoryResponse::class.java)
                Logger.d("s = $ss ,response = $response1")
                updateTopView(mTopList)
                //清除数据
                clearData()
               //手动添加全部
                mTitles.add("全部")
                mIds.add("")

                if (Utils.isStringEquals(response1.code, ResponseData.CODE_OK)) {

                    for (i in 0 until response1.data.size) {
                        response1.data[i].name?.let {
                            mTitles.add(it)
                            mIds.add(response1.data[i].id.toString())
                        }
                    }
                    for (id in mIds) {
                        val fragment2 = CommodityItemFragment.newInstance(id, "")
                        mFragments.add(fragment2)
                    }
                    mAdapter = object : FragmentPagerAdapter(childFragmentManager) {

                        override fun getCount(): Int {
                            return Math.min(mTitles.size, mIds.size)
                        }

                        override fun getItem(position: Int): Fragment {
                            return mFragments[position]
                        }

                        override fun getPageTitle(position: Int): CharSequence? {
                            return mTitles[position]
                        }
                    }
                    contentView.adapter = mAdapter
                    contentView.adapter?.notifyDataSetChanged()
                    tab.setViewPager(contentView)

                } else {
                    CommonToast.getInstance(response1.msg, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                finishRefreshAndLoadMore()
                updateTopView(mTopList)

            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun clearData() {
        mTitles.clear()
        mIds.clear()
        mFragments.clear()
    }

    fun finishRefreshAndLoadMore() {
        smartRefreshLayout.finishLoadMore()
        smartRefreshLayout.finishRefresh()
    }

    internal fun setLoadMoreEnable(enable: Boolean) {


        smartRefreshLayout.setEnableLoadMore(enable)
    }

    internal fun setNoMoreData(noMore: Boolean) {

        smartRefreshLayout.setNoMoreData(noMore)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        selectTab = position
        val fragment = mAdapter?.getItem(position) as CommodityItemFragment
        setLoadMoreEnable(fragment.isLoadMoreEnable)
        setNoMoreData(fragment.noMore)
    }

    inner class CountResponse(data: String?) : ResponseData(data) {

        var count = 0

        init {


            if (Utils.isStringEquals(mCode, ResponseData.CODE_OK)) {

                //到这里已经解析出来 code  和 msg 了
                try {
                    val mObject = JSONObject(data)//
                    count = mObject.getInt("data")
//                    val mObject1 = JSONObject(data1)//

//                    placeId = mObject1.getString("id")

                } catch (e: JSONException) {
                    StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "")

                    e.printStackTrace()
                }
            }
        }
    }
}



