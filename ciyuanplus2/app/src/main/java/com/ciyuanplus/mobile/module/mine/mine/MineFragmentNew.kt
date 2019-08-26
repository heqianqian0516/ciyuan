package com.ciyuanplus.mobile.module.mine.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.activity.MessageCenterActivity
import com.ciyuanplus.mobile.activity.mine.MyProfileActivity
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface
import com.ciyuanplus.mobile.inter.MyOnClickListener
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.mine.friends.MyFriendsActivity
import com.ciyuanplus.mobile.module.mine.friends.MyFriendsPresenter
import com.ciyuanplus.mobile.module.mine.my_order.MyOrderListActivity
import com.ciyuanplus.mobile.module.mine.news.MineNewsFragment
import com.ciyuanplus.mobile.module.mine.stuff.MineLikeFragment
import com.ciyuanplus.mobile.module.search.SearchActivity
import com.ciyuanplus.mobile.module.settings.about.AboutActivity
import com.ciyuanplus.mobile.module.settings.address.AddressManagerActivity
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.GetNoticeCountApiParameter
import com.ciyuanplus.mobile.net.response.NoticeCountResponse
import com.ciyuanplus.mobile.net.response.SocialCountItem
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.flyco.tablayout.listener.OnTabSelectListener
import com.kris.baselibrary.base.LazyLoadBaseFragment
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main_new.*
import kotlinx.android.synthetic.main.fragment_mine_new.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*
import javax.inject.Inject

/**
 * ************************************************************************
 * **                              _oo0oo_                               **
 * **                             o8888888o                              **
 * **                             88" . "88                              **
 * **                             (| -_- |)                              **
 * **                             0\  =  /0                              **
 * **                           ___/'---'\___                            **
 * **                        .' \\\|     |// '.                          **
 * **                       / \\\|||  :  |||// \\                        **
 * **                      / _ ||||| -:- |||||- \\                       **
 * **                      | |  \\\\  -  /// |   |                       **
 * **                      | \_|  ''\---/''  |_/ |                       **
 * **                      \  .-\__  '-'  __/-.  /                       **
 * **                    ___'. .'  /--.--\  '. .'___                     **
 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
 * **                              '=---='                               **
 * ************************************************************************
 * **                        佛祖保佑      镇类之宝                         **
 * ************************************************************************
 *
 */
class MineFragmentNew : LazyLoadBaseFragment(), MineContract.View,
        EventCenterManager.OnHandleEventListener, OnRefreshListener, OnLoadMoreListener, LoadMoreStatusInterface {
    override fun lazyLoad() {
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_mine_new
    }


    @Inject
    lateinit var mPresenter: MinePresenter

    private lateinit var mPostFragment: MineNewsFragment
    private lateinit var mLikeListFragment: MineLikeFragment
    private val mFragments = ArrayList<Fragment>()
    private val mTitles = arrayOf("动态", "喜欢")
    var mSelectedTab = 0
    private var mSystemMessageCount: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine_new, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        ll_my_orders.setOnClickListener { startActivity<MyOrderListActivity>() }
        ll_follow.setOnClickListener { startActivity<MyFriendsActivity>(Constants.INTENT_MY_FRIENDS_TYPE to MyFriendsPresenter.FOLLOW_TYPE) }
        ll_fan.setOnClickListener { startActivity<MyFriendsActivity>(Constants.INTENT_MY_FRIENDS_TYPE to MyFriendsPresenter.FAN_TYPE) }
        ll_address_manager.setOnClickListener {

            startActivity<AddressManagerActivity>()
        }
        ll_service.setOnClickListener {
            startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to "https://tb.53kf.com/code/client/10188509/1?device=android", Constants.INTENT_SHOW_TITLE_BAR to true)
        }
        ll_about.setOnClickListener { startActivity<AboutActivity>() }

        mPresenter.requestPersonInfo()
        mPresenter.requestCount()
    }

    private fun initView() {
        DaggerMinePresenterComponent.builder().minePresenterModule(MinePresenterModule(this)).build().inject(this)
        initTitleBar(2)
        mPostFragment = MineNewsFragment.newInstance()
        this.mFragments.add(mPostFragment)
        mPostFragment.setLoadMoreStatusInterface(this)

        mLikeListFragment = MineLikeFragment.newInstance()
        mLikeListFragment.setLoadMoreStatusInterface(this)
        this.mFragments.add(mLikeListFragment)

        val adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getCount(): Int {
                return this@MineFragmentNew.mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }

            override fun getItem(arg0: Int): Fragment {
                return this@MineFragmentNew.mFragments[arg0]
            }
        }
        viewPager.adapter = adapter

        stl_tabs.setViewPager(viewPager)
        stl_tabs.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {

                mSelectedTab = position
                switchTabSelect(position)

            }

            override fun onTabReselect(position: Int) {

            }
        })

        switchTabSelect(0)

        smartRefreshLayout.setOnRefreshListener(this)
        smartRefreshLayout.setOnLoadMoreListener(this)
//        smartRefreshLayout.setEnableNestedScroll(true)

        iv_head.setOnClickListener(myOnClickListener)

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this)

    }

    // 更新页面的状态
    override fun updateView() {
        if (!Utils.isStringEmpty(UserInfoData.getInstance().userInfoItem.photo)) { // 照片如果为空 使用默认头像
            defaultContext?.let {
                Glide.with(it).load(Constants.IMAGE_LOAD_HEADER + UserInfoData.getInstance().userInfoItem.photo).apply(RequestOptions().placeholder(R.mipmap.default_head_).error(R.mipmap.default_head_)
                        .dontAnimate().centerCrop()).into(iv_head)
            }
            iv_head.setTag(R.id.glide_item_tag, UserInfoData.getInstance().userInfoItem.photo)
        }

        tv_name.text = UserInfoData.getInstance().userInfoItem.nickname

        iv_head_sex.setImageResource(UserInfoData.getInstance().userInfoItem.sexImageResource)


        if (!StringUtils.isEmpty(UserInfoData.getInstance().userInfoItem.personalizedSignature)) {
            tv_sign.text = UserInfoData.getInstance().userInfoItem.personalizedSignature
        } else {
            tv_sign.hint = "我是签名，我是就爱签名"
        }
    }


    override fun switchTabSelect(position: Int) {

        if (mSelectedTab == 0) {
            smartRefreshLayout.setEnableLoadMore(mPostFragment.isLoadMoreEnable)
        } else {
            smartRefreshLayout.setEnableLoadMore(mLikeListFragment.isLoadMoreEnable)
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.requestPersonInfo()
        mPresenter.requestCount()
        requestNoticeCount()
    }

    override fun updateInfo(item: SocialCountItem) {

        tv_like_count.text = item.likeCount.toString()
        tv_fans.text = item.fansCount.toString()
        tv_follow.text = item.followCount.toString()
    }

    override fun finishLoadMoreAndRefresh() {
        smartRefreshLayout.finishLoadMore()
        smartRefreshLayout.finishRefresh()
    }


    override fun onDestroy() {
        super.onDestroy()
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this)
    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        mPresenter.handleEvent(eventMessage)
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT) {
            //            mMainNewsChatDot.setVisibility(View.VISIBLE);
            requestNoticeCount()
        }
    }

    override fun onRefresh(refreshlayout: RefreshLayout) {

        mPresenter.requestPersonInfo()
        mPresenter.requestCount()

        if (mSelectedTab == 0) {
            mPostFragment.requestPost(true)
        } else {
            mLikeListFragment.requestStuffList(true)
        }
    }

    override fun onLoadMore(refreshlayout: RefreshLayout) {
        if (mSelectedTab == 0) {
            mPostFragment.requestPost(false)
        } else {
            mLikeListFragment.requestStuffList(false)
        }
    }


    override fun getDefaultContext(): Context? {
        return activity
    }

    override fun onFinishLoadMore(enable: Boolean) {

        finishLoadMoreAndRefresh()
        if (mSelectedTab == 0) {
            smartRefreshLayout.setEnableLoadMore(enable)
        } else {
            smartRefreshLayout.setEnableLoadMore(enable)
        }
    }

    override fun onLoadMoreError() {
    }

    private val myOnClickListener = object : MyOnClickListener() {
        public override fun performRealClick(v: View) {

            mPresenter.handleClick(v.id)
        }
    }
    private fun updateUnReadMessage(i: Int) {
        mSystemMessageCount = i
        title_bars.setNotificationNumber(mSystemMessageCount)
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



    private fun initTitleBar(index: Int) {

        title_bars.setRightImageVisible(View.VISIBLE)

        when (index) {

            0 -> {

                title_bars.setTitleVisible(true)
                title_bars.setTitle("次元PLUS")
//                title_bar.setNotificationNumber(0)
                title_bars.setNotificationClickListener(View.OnClickListener {

                    val intent = Intent(activity, MessageCenterActivity::class.java)
                    startActivity(intent)
                })

                title_bars.setLefButtonImage(R.drawable.icon_search_title_bar)
                title_bars.setLeftImageListener(View.OnClickListener {
                    val intent = Intent(activity, SearchActivity::class.java)
                    intent.putExtra(Constants.INTENT_SEARCH_TYPE, SearchActivity.SEARCH_TYPE_USER)
                    startActivity(intent)

                })
            }
            1 -> {

                title_bars.setTitleVisible(true)
                title_bars.setTitle("次元PLUS")
//                title_bar.setNotificationNumber(0)

                title_bars.setNotificationClickListener(View.OnClickListener {
                    val intent = Intent(activity, MessageCenterActivity::class.java)
                    startActivity(intent)
                })

                title_bars.setLefButtonImage(R.drawable.icon_search_title_bar)

                title_bars.setLeftImageListener(View.OnClickListener {
                    val intent = Intent(activity, SearchActivity::class.java)
                    intent.putExtra(Constants.INTENT_SEARCH_TYPE, SearchActivity.SEARCH_TYPE_COMMODITY)
                    startActivity(intent)
                })
            }
            2 -> {
                title_bars.setTitleVisible(false)
//                title_bar.setNotificationNumber(0)
                title_bars.setLefButtonImage(R.drawable.icon_my_setting)
                title_bars.setNotificationClickListener(View.OnClickListener {

                    val intent = Intent(activity, MessageCenterActivity::class.java)
                    startActivity(intent)
                })
                title_bars.setLeftImageListener(View.OnClickListener {
                    val intent = Intent(activity, MyProfileActivity::class.java)
                    intent.putExtra(Constants.INTENT_SEARCH_TYPE, SearchActivity.SEARCH_TYPE_USER)
                    startActivity(intent)
                })
            }
        }
    }

}

