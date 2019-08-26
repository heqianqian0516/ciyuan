package com.ciyuanplus.mobile.module.search

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.OnClick
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ciyuanplus.mobile.App
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.activity.news.NewsSearchPopUpActivity
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity
import com.ciyuanplus.mobile.adapter.HomeFragmentAdapter
import com.ciyuanplus.mobile.adapter.WorldUserAdapter
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.forum_detail.daily_detail.DailyDetailActivity
import com.ciyuanplus.mobile.module.forum_detail.food_detail.FoodDetailActivity
import com.ciyuanplus.mobile.module.forum_detail.note_detail.NoteDetailActivity
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity
import com.ciyuanplus.mobile.module.store.commodity_list.CommodityItemRecyclerViewAdapter
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.DeleteCount
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.bean.FriendsItem
import com.ciyuanplus.mobile.net.bean.WorldUserItem
import com.ciyuanplus.mobile.net.parameter.*
import com.ciyuanplus.mobile.net.response.CommodityListItemRes
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse
import com.ciyuanplus.mobile.net.response.SearchUserResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.litesuits.http.exception.HttpException
import com.litesuits.http.listener.HttpListener
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/2/26
 * class  : SearchActivity.kt
 * desc   : 搜索页面
 * version: 1.0
 */


class SearchActivity : MyBaseActivity(), EventCenterManager.OnHandleEventListener {

    private val pageSize = 20
    private var pageIndex: Int = 0
    private var mSearchType = 0
    private var mLastId = ""
    private val mNewsList = ArrayList<FreshNewItem>()
    private val mUserList = ArrayList<WorldUserItem>()
    private val mCommodityList = ArrayList<CommodityListItemRes.CommodityItem>()
    private var mNewsAdapter: HomeFragmentAdapter? = null
    private var mWorldUserAdapter: WorldUserAdapter? = null
    private lateinit var mCommodityAdapter: CommodityItemRecyclerViewAdapter

    private var isEnableLoadMore = false

    companion object {
        const val SEARCH_TYPE_USER = 0
        const val SEARCH_TYPE_POST = 1
        const val SEARCH_TYPE_COMMODITY = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        Logger.d(this@SearchActivity.javaClass.toString() + "        onCreate.............")
        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
        smartRefreshLayout.setOnRefreshListener(){
            pageIndex=1
            requestWorldPost()
        }
        smartRefreshLayout.setOnLoadMoreListener(){
            pageIndex++
            requestWorldPost()

        }

    }

    private fun initView() {

        mSearchType = intent.getIntExtra(Constants.INTENT_SEARCH_TYPE, 0)
        rcl_news_list.visibility = View.GONE
        rcl_user_list.visibility = View.GONE
        commodityList.visibility = View.GONE

        when (mSearchType) {
            0, 1 -> {
                postUserSearchLayout.visibility = View.VISIBLE

            }
            2 -> {
                postUserSearchLayout.visibility = View.GONE
            }
        }

        iv_back_icon.setOnClickListener { onBackPressed() }

       /* smartRefreshLayout.setOnRefreshListener(this)
        smartRefreshLayout.setOnLoadMoreListener(this)*/



        mNewsAdapter = HomeFragmentAdapter(mNewsList)
        mNewsAdapter?.setHasStableIds(true)

        mNewsAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            if (position == -1) {
                return@OnItemClickListener
            }


            val item = mNewsAdapter?.getItem(position)
            //宝贝
            when (item?.bizType) {
                FreshNewItem.FRESH_ITEM_STUFF -> startActivity<StuffDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)

                FreshNewItem.FRESH_ITEM_DAILY -> startActivity<DailyDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)

                FreshNewItem.FRESH_ITEM_POST -> if (item.renderType == 1) {
                    // 长文和说说
                    startActivity<TwitterDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)

                } else {
                    startActivity<TwitterDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)

//                    startActivity<PostDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)
                }
                FreshNewItem.FRESH_ITEM_NEWS
                    , FreshNewItem.FRESH_ITEM_NOTE
                    , FreshNewItem.FRESH_ITEM_NEWS_COLLECTION -> startActivity<NoteDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)

                FreshNewItem.FRESH_ITEM_FOOD
                    , FreshNewItem.FRESH_ITEM_LIVE
                    , FreshNewItem.FRESH_ITEM_COMMENT -> startActivity<FoodDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)
                FreshNewItem.FRESH_ITEM_ANSWER -> startActivity<PostDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)
                else -> Logger.d(item?.bizType.toString())

            }

        }

        mNewsAdapter?.setOnItemChildClickListener { adapter, view, position ->

            val item = adapter.getItem(position) as FreshNewItem

            when (view.id) {


                R.id.riv_head_image -> {
                    startActivity<OthersActivity>(Constants.INTENT_USER_ID to item.userUuid)
                }
                R.id.tv_add -> {

                    requestFollowUser(item)
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
                        likePost(item)
                    } else {
                        cancelLikePost(item)
                    }

                }
                else -> {

                }

            }

        }


        mWorldUserAdapter = WorldUserAdapter(this, mUserList) { v ->
            val postion = rcl_user_list.getChildAdapterPosition(v)
            val item = mWorldUserAdapter?.getItem(postion)
            if (Utils.isStringEquals(item?.uuid, UserInfoData.getInstance().userInfoItem.uuid)) {
                return@WorldUserAdapter
            }


            val intent = Intent(this@SearchActivity, OthersActivity::class.java)
            intent.putExtra(Constants.INTENT_USER_ID, item?.uuid)
            this@SearchActivity.startActivity(intent)
        }
        mWorldUserAdapter?.setHasStableIds(true)



        when (mSearchType) {
            0 -> {
                this.iv_user_bottom.visibility = View.VISIBLE
                this.iv_post_bottom.visibility = View.INVISIBLE

                firstQuery()

            }
            1 -> {
                this.iv_user_bottom.visibility = View.VISIBLE
                this.iv_post_bottom.visibility = View.INVISIBLE

                firstQuery()
            }
            2 -> {
                this.iv_user_bottom.visibility = View.VISIBLE
                this.iv_post_bottom.visibility = View.INVISIBLE

                firstQuery()
            }

        }

        rl_user.setOnClickListener {
            GSYVideoManager.releaseAllVideos()
            this.iv_user_bottom.visibility = View.VISIBLE
            this.iv_post_bottom.visibility = View.INVISIBLE
            mSearchType = SEARCH_TYPE_USER
            pageIndex = 0
            mLastId = ""
            isEnableLoadMore = false
            mNewsList.clear()
            mUserList.clear()
            mCommodityList.clear()
            et_search.hint = "搜索用户"
            doSearchQuery()

        }
        rl_post.setOnClickListener {
            GSYVideoManager.releaseAllVideos()
            this.iv_user_bottom.visibility = View.INVISIBLE
            this.iv_post_bottom.visibility = View.VISIBLE
            mSearchType = SEARCH_TYPE_POST
            pageIndex = 0
            mLastId = ""
            mNewsList.clear()
            mUserList.clear()
            mCommodityList.clear()
            et_search.hint = "搜索帖子"
            isEnableLoadMore = false
            doSearchQuery()
        }

        rcl_news_list.layoutManager = LinearLayoutManager(this)
        rcl_user_list.layoutManager = LinearLayoutManager(this)

        commodityList.layoutManager = GridLayoutManager(mActivity, 2)
        commodityList.addItemDecoration(object : RecyclerView.ItemDecoration() {


            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = Utils.dip2px(10F)
                outRect.right = Utils.dip2px(10F)
            }
        })

        mCommodityAdapter = CommodityItemRecyclerViewAdapter(mActivity, mCommodityList)

        mCommodityAdapter.setOnItemClickListener { adapter, view, position ->

            val item = adapter.getItem(position) as CommodityListItemRes.CommodityItem
            val userUuid = UserInfoData.getInstance().userInfoItem.uuid
            val sessionKey = SharedPreferencesManager.getString(
                    Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")

            val url = "${ApiContant.WEB_DETAIL_VIEW_URL}cyplus-share/prod.html?userUuid=$userUuid&authToken=$sessionKey&p=${item.prodId}"
            startActivity<JsWebViewActivity>(Constants.INTENT_OPEN_URL to url)
        }
        commodityList.adapter = mCommodityAdapter
        commodityList.isNestedScrollingEnabled = false

        et_search.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(et_search)
                val mSearchValue = et_search.text.toString()
                if (!Utils.isStringEmpty(mSearchValue)) {
//                    pageIndex = 0
//                    mLastId = ""
//                    mNewsList.clear()
//                    mUserList.clear()
//                    mCommodityList.clear()
//                    doSearchQuery()
                    firstQuery()
                }

            }
            false
        }

        firstQuery()


        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this)

    }

    //搜索商品帖子
    private fun requestCommodityList(categoryId: Int) {

        val searchText = et_search.text.toString()

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COMMODITY_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequstCommodityListParameter(pageSize.toString(), pageIndex.toString(), "", searchText).requestBody)

        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

//                val parent = parentFragment as ShoppingMallFragment
//                parent.finishRefreshAndLoadMore()
               // finishRefreshAndLoadMore()

                val response1 = CommodityListItemRes(s)

                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if (response1.communityListItem?.list != null) {
                        if (response1.communityListItem?.list!!.size >= pageSize) {
                            isEnableLoadMore = true
                            pageIndex++

                        } else {
                            isEnableLoadMore = false
                        }

//                        rcl_news_list.visibility = View.GONE
//                        rcl_user_list.visibility = View.GONE
//                        commodityList.visibility = View.VISIBLE
                    } else {
                        isEnableLoadMore = false
                    }

                    response1.communityListItem?.list?.let { mCommodityList.addAll(it) }
                    mCommodityAdapter.notifyDataSetChanged()
                    updateList()

                } else {
                    isEnableLoadMore = false
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                isEnableLoadMore = false
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)

    }


    private fun firstQuery() {

        when (mSearchType) {
            0 -> {

                et_search.hint = "搜索用户"
                rcl_user_list.adapter = mWorldUserAdapter
            }
            1 -> {

                et_search.hint = "搜索帖子"
                rcl_news_list.adapter = mNewsAdapter
            }
            2 -> {

                et_search.hint = "搜索商品"
                rcl_news_list.adapter = mNewsAdapter
            }
        }
        pageIndex = 0
        mLastId = ""
        mNewsList.clear()
        mUserList.clear()
        mCommodityList.clear()

        doSearchQuery()
    }

    private fun doSearchQuery() {
        val searchText = et_search.text.toString()
        if (Utils.isStringEmpty(searchText)) return
        when (mSearchType) {
            0 -> requestSearchUser()
            1 -> requestWorldPost()
            2 -> requestCommodityList(0)
        }
    }

    //隐藏虚拟键盘
    private fun hideKeyboard(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)

        }
    }


    private fun requestWorldPost() {
        val searchText = et_search.text.toString()
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_all)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestWorldPostsApiParameter(searchText, pageIndex.toString() + "", pageSize.toString() + "", mLastId, "", "").requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                // 必须要加的
                //                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                //                rcl_list.setRefreshing(false);
                //finishRefreshAndLoadMore()

                val response1 = RequestFreshNewsResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.isNotEmpty()) {

//                    rcl_news_list.visibility = View.VISIBLE
//                    rcl_user_list.visibility = View.GONE
//                    commodityList.visibility = View.GONE

                    if (pageIndex == 0) {
                        mNewsList.clear()
                    }
                    Collections.addAll(mNewsList, *response1.freshNewsListItem.list)

                    mLastId = mNewsList[mNewsList.size - 1].id
                    isEnableLoadMore = mNewsList.size >= pageSize
                    pageIndex++

                }

                smartRefreshLayout.finishRefresh()
                smartRefreshLayout.finishLoadMore()
                updateList()
            }


            override fun onFailure(e: HttpException?, response: Response<String>?) {
                // 必须要加的
                //                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                //                rcl_list.setRefreshing(false);

                super.onFailure(e, response)
                //finishRefreshAndLoadMore()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun requestSearchUser() {
        val searchText = et_search.text.toString()
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SEARCH_ALL_USER)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(RequestSearchAllUserApiParameter(searchText, pageIndex.toString() + "", pageSize.toString() + "").requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                // 必须要加的
                //                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                //                rcl_list.setRefreshing(false);
                //finishRefreshAndLoadMore()

                val response1 = SearchUserResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else if (response1.worldUserListInfo.list != null && response1.worldUserListInfo.list.isNotEmpty()) {
                    if (pageIndex == 0) {
                        mUserList.clear()
                    }

//                    rcl_news_list.visibility = View.GONE
//                    rcl_user_list.visibility = View.VISIBLE
//                    commodityList.visibility = View.GONE
                    mUserList.addAll(response1.worldUserListInfo.list)
                    pageIndex++
                }
//                rcl_list.adapter = mWorldUserAdapter
//                mWorldUserAdapter?.notifyDataSetChanged()
                updateList()
            }


            override fun onFailure(e: HttpException?, response: Response<String>?) {
                // 必须要加的
                //                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                //                rcl_list.setRefreshing(false);

                //finishRefreshAndLoadMore()
                super.onFailure(e, response)
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun updateList() {

        if (mSearchType == SEARCH_TYPE_USER) {
            if (mUserList.size > 0) {
                ll_no_data_layout.visibility = View.GONE

                rcl_user_list.visibility = View.VISIBLE
                rcl_user_list.adapter = mWorldUserAdapter
                rcl_news_list.visibility = View.GONE
                mWorldUserAdapter?.notifyDataSetChanged()
            } else {
                ll_no_data_layout.visibility = View.VISIBLE
                rcl_user_list.visibility = View.GONE
                rcl_news_list.visibility = View.GONE
                rcl_user_list.adapter = mWorldUserAdapter
                mWorldUserAdapter?.notifyDataSetChanged()
                tv_message.text = "未搜索到相关用户"
                iv_no_data.setImageResource(R.drawable.image_serach_no_user)
            }
        } else if (mSearchType == SEARCH_TYPE_POST) {

            if (mNewsList.size > 0) {
                ll_no_data_layout.visibility = View.GONE
                rcl_news_list.visibility = View.VISIBLE
                rcl_user_list.visibility = View.GONE
              //  rcl_news_list.adapter = mNewsAdapter
                mNewsAdapter?.notifyDataSetChanged()
            } else {
                rcl_user_list.visibility = View.GONE
                ll_no_data_layout.visibility = View.VISIBLE
                rcl_news_list.visibility = View.GONE
                //rcl_news_list.adapter = mNewsAdapter
                mNewsAdapter?.notifyDataSetChanged()
                tv_message.text = "未搜索到相关帖子"
                iv_no_data.setImageResource(R.drawable.image_serach_no_post)
            }
        } else {

            if (mCommodityList.size > 0) {
                ll_no_data_layout.visibility = View.GONE
                rcl_news_list.visibility = View.GONE
                rcl_user_list.visibility = View.GONE
                commodityList.visibility = View.VISIBLE
                commodityList.adapter = mCommodityAdapter
                mCommodityAdapter.notifyDataSetChanged()
            } else {
                rcl_user_list.visibility = View.GONE
                ll_no_data_layout.visibility = View.VISIBLE
                commodityList.visibility = View.GONE
                rcl_news_list.visibility = View.GONE
                commodityList.adapter = mCommodityAdapter
                mCommodityAdapter.notifyDataSetChanged()
                tv_message.text = "未搜索到相关商品"
                iv_no_data.setImageResource(R.drawable.image_serach_no_commodity)
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this)


    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        when {
            eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT -> {
                val uuid: String
                val isLike: Int
                val likeCount: Int
                if (eventMessage.mObject is FreshNewItem) {
                    val item = eventMessage.mObject
                    uuid = item.postUuid
                    isLike = item.isLike
                    likeCount = item.likeCount
                } else {
                    val item = eventMessage.mObject as FreshNewItem
                    uuid = item.postUuid
                    isLike = item.isLike
                    likeCount = item.likeCount
                }
                for (i in mNewsList.indices) {
                    if (Utils.isStringEquals(uuid, mNewsList[i].postUuid)) {
                        mNewsList[i].isLike = isLike
                        mNewsList[i].likeCount = likeCount
                        mNewsAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
            eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE -> {
                val postUuid = eventMessage.mObject as String
                for (i in mNewsList.indices) {
                    if (Utils.isStringEquals(postUuid, mNewsList[i].postUuid)) {
                        mNewsList.removeAt(i)
                        mNewsAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
            eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE -> {
                val friendsItem = eventMessage.mObject as FriendsItem
                for (i in mUserList.indices) {
                    if (Utils.isStringEquals(friendsItem.uuid, mUserList[i].uuid)) {
                        mUserList[i].isFollow = friendsItem.followType
                    }
                }
                mWorldUserAdapter?.notifyDataSetChanged()
            }
            eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY -> {
                val postUuid = eventMessage.mObject as String
                for (i in mNewsList.indices) {
                    if (Utils.isStringEquals(postUuid, mNewsList[i].postUuid)) {
                        mNewsList[i].commentCount++
                        mNewsAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
            eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY -> {
                val deleteCount = eventMessage.mObject as DeleteCount
                for (i in mNewsList.indices) {
                    if (Utils.isStringEquals(deleteCount.postUUID, mNewsList[i].postUuid)) {
                        mNewsList[i].commentCount -= deleteCount.deleteCount
                        mNewsAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
            eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT -> {
                val postUuid = eventMessage.mObject as String
                for (i in mNewsList.indices) {
                    if (Utils.isStringEquals(postUuid, mNewsList[i].postUuid)) {
                        mNewsList[i].browseCount++
                        mNewsAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
            eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED -> {
                val postUuid = eventMessage.mObject as String
                for (i in mNewsList.indices) {
                    if (Utils.isStringEquals(postUuid, mNewsList[i].postUuid)) {
                        mNewsList[i].isDislike = 1
                        mNewsList[i].dislikeCount++
                        mNewsAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
            eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED -> {
                val postUuid = eventMessage.mObject as String
                for (i in mNewsList.indices) {
                    if (Utils.isStringEquals(postUuid, mNewsList[i].postUuid)) {
                        mNewsList[i].isDislike = 0
                        mNewsList[i].dislikeCount--
                        mNewsAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
        }
    }

    @OnClick(R.id.m_search_news_cancel, R.id.m_search_news_search_drop_img)
    override fun onViewClicked(view: View) {
        super.onViewClicked(view)
        when (view.id) {
            R.id.m_search_news_cancel -> onBackPressed()
            R.id.m_search_news_search_drop_img -> {
                val intent = Intent(this@SearchActivity, NewsSearchPopUpActivity::class.java)
                intent.putExtra(Constants.INTENT_SEARCH_NEWS_TYPE, mSearchType)
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_SEARCH_TYPE)
            }
        }
    }

     fun onLoadMore(refreshlayout: RefreshLayout) {

        doSearchQuery()
    }

     fun onRefresh(refreshlayout: RefreshLayout) {

       /* pageIndex = 0
        mLastId = ""
        mNewsList.clear()
        mUserList.clear()
        mCommodityList.clear()*/
        doSearchQuery()
    }

    /*private fun finishRefreshAndLoadMore() {
        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()
    }*/

    private fun setLoadMoreEnable(enable: Boolean) {
        smartRefreshLayout.setEnableLoadMore(enable)
    }


    fun requestFollowUser(item: FreshNewItem) {
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
                CommonToast.getInstance(resources.getString(R.string.string_get_fresh_news_fail_alert), Toast.LENGTH_SHORT).show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    // 赞新鲜事
    fun likePost(item: FreshNewItem) {
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
    fun cancelLikePost(item: FreshNewItem) {
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


    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()

        GSYVideoManager.releaseAllVideos()
    }

}

