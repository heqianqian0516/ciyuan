package com.ciyuanplus.mobile.module.home


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.R2.id.rcl_list
import com.ciyuanplus.mobile.R2.id.refresh
import com.ciyuanplus.mobile.activity.MessageCenterActivity
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity
import com.ciyuanplus.mobile.adapter.HomeFragmentAdapter
import com.ciyuanplus.mobile.adapter.HomeHotAdapter
import com.ciyuanplus.mobile.loader.GlideImageLoader
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity
import com.ciyuanplus.mobile.module.home.activity.RankingListActivity
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity
import com.ciyuanplus.mobile.module.release.ReleasePostActivity
import com.ciyuanplus.mobile.module.search.SearchActivity
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.*
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter
import com.ciyuanplus.mobile.net.parameter.GetNoticeCountApiParameter
import com.ciyuanplus.mobile.net.response.NoticeCountResponse
import com.ciyuanplus.mobile.net.response.RequestAdvertisementListResponse.RequestAdvertisementList
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.jaeger.library.StatusBarUtil
import com.kris.baselibrary.base.LazyLoadBaseFragment
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import crossoverone.statuslib.StatusUtil
import kotlinx.android.synthetic.main.fragment_home_new.*
import kotlinx.android.synthetic.main.fragment_shopping_mall.*
import kotlinx.android.synthetic.main.home_bar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject
import kotlin.math.log

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/5
 * class  : HomeFragment.kt
 * desc   : 首页
 * version: 1.0
 */


class HomeFragment : LazyLoadBaseFragment(), HomeFragmentContract.View, EventCenterManager.OnHandleEventListener {

    @Inject
    lateinit var mPresenter: HomeFragmentPresenter
    private lateinit var mHotAdapter: HomeHotAdapter
    private lateinit var mHomePostAdapter: HomeFragmentAdapter
    private var banner: Banner? = null
    private var gridView: GridView? = null
    private var loader: GlideImageLoader? = null
    private var imageActivity :ImageView?=null
    internal lateinit var linearLayoutManager: LinearLayoutManager
    var mItem: ArrayList<HomeADBean.DataBean>? = null
    private var imageSearch:ImageView?=null
    private var ivNotification:ImageView?=null
     private var statusBarAlpha:Int=112;
    private var mSystemMessageCount: Int = 0
    private var mNotificationNumber: TextView? =null
    override fun stopRefreshAndLoad() {

    }

    override fun lazyLoad() {
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_new
    }

    fun initView() {

        val headerView = LayoutInflater.from(activity).inflate(R.layout.header_home_fragment, null)

       // StatusBarUtil.setTranslucent( activity,  statusBarAlpha)
        mHomePostAdapter = HomeFragmentAdapter(ArrayList<FreshNewItem>() as MutableList<FreshNewItem>?)
        banner = headerView?.findViewById(R.id.bannerHome)
        gridView = headerView?.findViewById(R.id.grid_hot)
        imageActivity=headerView?.findViewById(R.id.home_activity_imageview)

        imageSearch=headerView?.findViewById(R.id.image_search)
        ivNotification=headerView?.findViewById(R.id.iv_notification)
        mNotificationNumber= headerView?.findViewById(R.id.tv_notification_num)
        mHotAdapter = HomeHotAdapter(activity, ArrayList<FriendsItem>())
        gridView?.adapter = mHotAdapter
        imageActivityItem();
        mHomePostAdapter.setHeaderView(headerView)
        linearLayoutManager = LinearLayoutManager(defaultContext)
      /*  rcl_list.layoutManager = linearLayoutManager
        rcl_list.adapter = mHomePostAdapter
        (rcl_list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        val animator = rcl_list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false*/

        imageActivity?.setOnClickListener() {
            var intent = Intent(activity,RankingListActivity::class.java)
            intent.putExtra("mUuid", mItem?.get(0)?.uuid)
            startActivity(intent)
        }
        imageSearch?.setOnClickListener(){
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra(Constants.INTENT_SEARCH_TYPE, SearchActivity.SEARCH_TYPE_USER)
            startActivity(intent)
        }
        ivNotification?.setOnClickListener(){
            requestNoticeCount()
            val intent = Intent(activity, MessageCenterActivity::class.java)
            startActivity(intent)

        }



        mHomePostAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->

            val item = mHomePostAdapter.getItem(position)

            item?.let {

                startActivity<TwitterDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)

            }
        }

        mHomePostAdapter.setOnItemChildClickListener { adapter, view, position ->


            val item = adapter?.getItem(position) as FreshNewItem

            when (view.id) {
                R.id.riv_head_image -> {
                    startActivity<OthersActivity>(Constants.INTENT_USER_ID to item.userUuid)
                }
                R.id.tv_add -> {
                    mPresenter.requestFollowUser(item)
                }
                R.id.ll_share -> {
                    startActivity<ShareNewsPopupActivity>(Constants.INTENT_NEWS_ITEM to item)
                }
                R.id.ll_like -> {

                    if (!LoginStateManager.isLogin()) {
                        startActivity<LoginActivity>()
                        return@setOnItemChildClickListener
                    }

                    if (item.isLike == 0) {
                        mPresenter.likePost(item)
                    } else {
                        mPresenter.cancelLikePost(item)
                    }

                }
                else -> {

                }
            }


        }

//

       /* rcl_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var firstVisibleItem: Int = 0
            var lastVisibleItem: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                //大于0说明有播放
                if (GSYVideoManager.instance().playPosition >= 0) {
                    //当前播放的位置
                    val position = GSYVideoManager.instance().playPosition
                    //对应的播放列表TAG
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        if (!GSYVideoManager.isFullState(activity)) {
                            GSYVideoManager.releaseAllVideos()
                            mHomePostAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })


        refresh.setOnRefreshListener {

            mPresenter.doRequest(true)

        }

        refresh.setOnLoadMoreListener {
            mPresenter.doRequest(false)
        }
        iv_release.setOnClickListener { startActivity<ReleasePostActivity>(Constants.INTENT_OPEN_TYPE to 0) }
  */  }





    fun onBackPressed(): Boolean {
        return GSYVideoManager.backFromWindowFull(activity)
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onFragmentPause() {
        super.onFragmentPause()

        GSYVideoManager.onPause()
    }


    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
        requestNoticeCount()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        fistPopuwindow()
        DaggerHomeFragmentPresenterComponent.builder().homeFragmentPresenterModule(HomeFragmentPresenterModule(this)).build().inject(this)
        // 第二个参数是状态栏色值。
        // 第三个参数是兼容5.0到6.0之间的状态栏颜色字体只能是白色，如果沉浸的颜色与状态栏颜色冲突, 设置一层浅色对比能显示出状态栏字体（可以找ui给一个合适颜色值）。
        // 如果您的项目是6.0以上机型或者某些界面不适用沉浸, 推荐使用两个参数的setUseStatusBarColor。
        StatusUtil.setUseStatusBarColor(activity, Color.TRANSPARENT, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(activity, true, false);
        mPresenter.doRequest(true)

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this)// 添加小区列表变化的监听事件
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_SHOW_ACTIVITY_WEBVIEW, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this)
        rcl_list.layoutManager = linearLayoutManager
        rcl_list.adapter = mHomePostAdapter
        (rcl_list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        val animator = rcl_list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false
        rcl_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var firstVisibleItem: Int = 0
            var lastVisibleItem: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                //大于0说明有播放
                if (GSYVideoManager.instance().playPosition >= 0) {
                    //当前播放的位置
                    val position = GSYVideoManager.instance().playPosition
                    //对应的播放列表TAG
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        if (!GSYVideoManager.isFullState(activity)) {
                            GSYVideoManager.releaseAllVideos()
                            mHomePostAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })


        refresh.setOnRefreshListener {

            mPresenter.doRequest(true)

        }

        refresh.setOnLoadMoreListener {
            mPresenter.doRequest(false)
        }
        iv_release.setOnClickListener { startActivity<ReleasePostActivity>(Constants.INTENT_OPEN_TYPE to 0) }

    }

    private fun fistPopuwindow() {
        Thread(Runnable {
            try {
                Thread.sleep(1500)
                runOnUiThread {
                    val view = View.inflate(activity, R.layout.home_sign_everyday, null)
                    val mImg_back = view.findViewById<ImageView>(R.id.home_back)
                    val mLine = view.findViewById<RelativeLayout>(R.id.relative_layout)
                    val mPopu = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT)
                    mPopu.height = ViewGroup.LayoutParams.MATCH_PARENT
                    mLine.background.alpha = 50

                    mImg_back.setOnClickListener {
                        // ToastUtils.makeShortToast(MainActivity.this,"我还没有写内容呢");
                        mPopu.dismiss()
                    }
                    //  mPopu.setOutsideTouchable(true);//判断在外面点击是否有效
                    //mPopu.setFocusable(true);
                    mPopu.showAsDropDown(view)
                    mPopu.isShowing
                }


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }).start()

    }



    override fun updateTopView(bannerList: ArrayList<BannerItem>?) {

        if (bannerList == null || bannerList.size == 0) {
            banner?.visibility = View.GONE
            return
        }

        banner?.visibility = View.VISIBLE

        val images = ArrayList<String>()
        for (item in bannerList) {

            images.add(Constants.IMAGE_LOAD_HEADER + item.img)
        }

        //设置banner样式
        this.banner?.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        if (loader == null) {
            loader = GlideImageLoader()
        }

        this.banner?.setImageLoader(loader)
        //设置banner样式
        //设置banner动画效果
        this.banner?.setBannerAnimation(Transformer.DepthPage)
//        设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles)
//        设置自动轮播，默认为true
       // banner?.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        this.banner?.isAutoPlay(true)
//        设置轮播时间
        banner?.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        this.banner?.setIndicatorGravity(BannerConfig.RIGHT)
        this.banner?.setOnBannerListener {

            clickBanner(bannerList[it])
        }
        //设置图片集合
        this.banner?.setImages(images)
        //banner设置方法全部调用完毕时最后调用
        this.banner?.start()
    }

    private fun clickBanner(banner: BannerItem) {

        when (banner.type) {
            0 -> {
            }
            1 -> {
                startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to banner.param)
            }
        }
    }

    override fun updateHeadLine(headline: ArrayList<FriendsItem>?) {


        if (headline == null || headline.size == 0) {
            gridView?.visibility = View.GONE
            return
        }

        gridView?.visibility = View.VISIBLE
        mHotAdapter = HomeHotAdapter(activity, headline)
        gridView?.adapter = mHotAdapter
        mHotAdapter.notifyDataSetChanged()
    }

    override fun updateListView(postList: ArrayList<FreshNewItem>?) {
        if (postList == null || postList.size == 0) {
            rcl_list.visibility = View.GONE
            return
        }
        rcl_list.visibility = View.VISIBLE
        mHomePostAdapter.setNewData(postList)

    }

    override fun stopRefreshAndRLoadMore(status: Boolean) {

        refresh.finishLoadMore()
        refresh.finishRefresh()

    }

    override fun setLoadMoreEnable(enable: Boolean) {

        refresh.setEnableLoadMore(enable)
    }


    override fun showLoadingDialog() {
    }

    override fun dismissLoadingDialog() {


    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        Logger.d("${this.javaClass.simpleName} handleEventCenterEvent: $eventMessage")
        when (eventMessage.mEvent) {
            Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT->{

            }
            Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH -> {
                // 刷新小区列表
                mPresenter.doRequest(true)
            }
            Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH -> {
                mPresenter.doRequest(true)
            }
            Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST, Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM
                , Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST -> {
                mPresenter.doRequest(true)
//                scrollView.scrollTo(0, 0)0
            }
            Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT -> updateLikeStatus(eventMessage)
            Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE -> deletePost(eventMessage)
            Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE -> {
                updatePeopleInfo(eventMessage)
                mHomePostAdapter.notifyDataSetChanged()
            }
            Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY -> {
                addCommentOrReplya(eventMessage)
            }
            Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY -> {

                deleteCommentOrReply(eventMessage)

            }
            Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT -> {
                updateBrowserCount(eventMessage)
            }
            Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED -> {
                markPost(eventMessage)
            }
            Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED -> {
                cancelMarkPost(eventMessage)
            }
            Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED -> {
                collect(eventMessage)
            }
            Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED -> {
                cancelCollect(eventMessage)
            }
            Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT->{
                requestNoticeCount()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()

        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_SHOW_ACTIVITY_WEBVIEW, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM, this)

    }

    private fun collect(eventMessage: EventCenterManager.EventMessage) {
        val postUuid = eventMessage.mObject as String


        for (i in 0 until mHomePostAdapter.data.size) {

            val item = mHomePostAdapter.data[i]
            item?.let {
                if (Utils.isStringEquals(postUuid, it.postUuid)) {
                    mHomePostAdapter.getItem(i)!!.isDislike = 1
                    mHomePostAdapter.getItem(i)!!.dislikeCount++
                    mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                    return
                }
            }

        }
    }
     fun imageActivityItem() {
         val postRequest = StringRequest(ApiContant.URL_HEAD + "/api/event/getEventList")
         //"http://39.98.205.18:9999"
         postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
         postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this.defaultContext) {
             override fun onSuccess(s: String?, response: Response<String>?) {
                 super.onSuccess(s, response)
                 mItem = RequestAdvertisementList(s)
               //  Log.e("zzzzzzzz", "getInfo: " + mItem!!.get(0).getUuid())
                 EventBus.getDefault().postSticky(mItem)
                 if (mItem != null) {
                     activity?.let {
                         imageActivity?.let { it1 ->
                             Glide.with(it)
                                     .load(Constants.IMAGE_LOAD_HEADER + mItem?.get(0)?.imgs)
                                     .apply(RequestOptions.bitmapTransform(RoundedCorners(13)))
                                     .into(it1)
                         }
                     }
                 }


             }

             override fun onFailure(e: HttpException?, response: Response<String>?) {
                 super.onFailure(e, response)

             }
         })
         LiteHttpManager.getInstance().executeAsync(postRequest)
    }
    private fun cancelCollect(eventMessage: EventCenterManager.EventMessage) {
        val postUuid = eventMessage.mObject as String

        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(postUuid, mHomePostAdapter.data[i].postUuid)) {
                mHomePostAdapter.data[i].isDislike = 0
                mHomePostAdapter.data[i].dislikeCount--
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                return
            }
        }
    }

    private fun cancelMarkPost(eventMessage: EventCenterManager.EventMessage) {
        val postUuid = eventMessage.mObject as String
        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(postUuid, mHomePostAdapter.data[i].postUuid)) {
                mHomePostAdapter.data[i].isRated = 0
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                return
            }
        }
    }

    private fun markPost(eventMessage: EventCenterManager.EventMessage) {
        val postUuid = eventMessage.mObject as String
        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(postUuid, mHomePostAdapter.data[i].postUuid)) {
                mHomePostAdapter.data[i].isRated = 1
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                return
            }
        }
    }

    private fun updateBrowserCount(eventMessage: EventCenterManager.EventMessage) {
        val postUuid = eventMessage.mObject as String
        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(postUuid, mHomePostAdapter.data[i].postUuid)) {
                mHomePostAdapter.data[i].browseCount++
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                return
            }
        }
    }

    private fun deleteCommentOrReply(eventMessage: EventCenterManager.EventMessage) {
        val deleteCount = eventMessage.mObject as DeleteCount
        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(deleteCount.postUUID, mHomePostAdapter.data[i].postUuid)) {
                mHomePostAdapter.data[i].commentCount -= deleteCount.deleteCount
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                return
            }
        }
    }

    private fun addCommentOrReplya(eventMessage: EventCenterManager.EventMessage) {
        val postUuid = eventMessage.mObject as String
        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(postUuid, mHomePostAdapter.data[i].postUuid)) {
                mHomePostAdapter.data[i].commentCount++
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                return
            }
        }
    }

    private fun updatePeopleInfo(eventMessage: EventCenterManager.EventMessage) {
        val friendsItem = eventMessage.mObject as FriendsItem
        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(friendsItem.uuid, mHomePostAdapter.data[i].userUuid)) {
                mHomePostAdapter.data[i].isFollow = friendsItem.followType
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
            }
        }
    }

    private fun deletePost(eventMessage: EventCenterManager.EventMessage) {
        val postUuid = eventMessage.mObject as String
        for (i in mHomePostAdapter.data.indices) {
            if (Utils.isStringEquals(postUuid, mHomePostAdapter.data[i].postUuid)) {
                mHomePostAdapter.data.removeAt(i)
                mHomePostAdapter.notifyItemChanged(i - mHomePostAdapter.headerLayoutCount)
                return
            }
        }
    }

    private fun updateLikeStatus(eventMessage: EventCenterManager.EventMessage) {

        if (eventMessage.mObject is FreshNewItem) {
            val item = eventMessage.mObject

            for (i in mHomePostAdapter.data.indices) {
                if (Utils.isStringEquals(item.postUuid, mHomePostAdapter.data[i].postUuid)) {
                    mHomePostAdapter.data[i].isLike = item.isLike
                    mHomePostAdapter.data[i].likeCount = item.likeCount
                    mHomePostAdapter.notifyItemChanged(i + mHomePostAdapter.headerLayoutCount)
                    return
                }
            }
        }
    }


    fun requestFollowUser(item: FriendsItem) {

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_FOLLOW_USER_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(FollowOtherApiParameter(item.uuid).requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(defaultContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = RequestOtherInfoResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else {
                    // 更新其他页面
                    val friendsItem = FriendsItem()
                    friendsItem.uuid = item.uuid
                    friendsItem.followType = 1
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem))

                    CommonToast.getInstance("关注成功").show()
                    item.followType = 1
                    mHomePostAdapter.notifyDataSetChanged()
                }
            }
            //
            //            @Override
            //            public void onFailure(HttpException e, Response<String> response) {
            //                super.onFailure(e, response);
            //                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
            //                        Toast.LENGTH_SHORT).show();
            //            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }
    fun setNotificationNumber(count: Int) {

        if (count > 0) {
            mNotificationNumber?.visibility = View.VISIBLE
            mNotificationNumber?.text = count.toString()
        } else {
            mNotificationNumber?.visibility = View.GONE
        }
    }
    fun updateUnReadMessage(i: Int) {
        mSystemMessageCount = i
        setNotificationNumber(mSystemMessageCount)

    }
    fun requestNoticeCount() {
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
    override fun getDefaultContext(): Context? {
        return context
    }

}
