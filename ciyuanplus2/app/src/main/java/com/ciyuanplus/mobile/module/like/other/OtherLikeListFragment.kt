package com.ciyuanplus.mobile.module.like.other


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity
import com.ciyuanplus.mobile.adapter.HomeFragmentAdapter
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface
import com.ciyuanplus.mobile.manager.*
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.bean.FriendsItem
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter
import com.ciyuanplus.mobile.net.parameter.RequestMineStuffListApiParameter
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse
import com.ciyuanplus.mobile.net.response.RequestStuffListResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.kris.baselibrary.base.LazyLoadBaseFragment
import com.litesuits.http.exception.HttpException
import com.litesuits.http.listener.HttpListener
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.fragment_other_stuff.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
@SuppressLint("ValidFragment")
class OtherLikeListFragment : LazyLoadBaseFragment(), EventCenterManager.OnHandleEventListener {
    override fun lazyLoad() {
    }

    override fun getLayoutResId(): Int {
        return 0
    }

    private lateinit var mAdapter: HomeFragmentAdapter
    private val mLikeList = ArrayList<FreshNewItem>()
    private var mStuffNextPage: Int = 0
    private val pageSize = 20
    var isLoadMoreEnable: Boolean = false


    companion object {
        @JvmStatic
        fun newInstance(): OtherLikeListFragment {

            return OtherLikeListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_other_like_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity)
        m_others_stuff_list.layoutManager = linearLayoutManager
        m_others_stuff_list.isNestedScrollingEnabled = true//解决ScrollView嵌套RecyclerView导致滑动不流畅的问题
        m_others_stuff_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })


        mAdapter = HomeFragmentAdapter(mLikeList)

        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->

            val item = adapter.getItem(position) as FreshNewItem

            item.let {

                startActivity<TwitterDetailActivity>(Constants.INTENT_NEWS_ID_ITEM to item.postUuid, Constants.INTENT_BIZE_TYPE to item.bizType)

            }
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->


            val item = adapter?.getItem(position) as FreshNewItem

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

        m_others_stuff_list.adapter = mAdapter

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)
        requestLikeList()

    }


    fun requestLikeList(reset: Boolean) {
        if (reset) {
            mStuffNextPage = 0
            mLikeList.clear()
        }
        requestLikeList()
    }


    private fun requestLikeList() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_USER_LIKE_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)

//        postRequest.setHttpBody<AbstractRequest<String>>(CommentParameter("", (activity as OthersActivity).userUuid, "", mStuffNextPage.toString() + "", pageSize.toString()).requestBody)

        postRequest.setHttpBody<AbstractRequest<String>>(RequestMineStuffListApiParameter((activity as OthersActivity).userUuid, AMapLocationManager.getInstance().longitude,
                AMapLocationManager.getInstance().latitude, mStuffNextPage.toString() + "", pageSize.toString(), UserInfoData.getInstance().userInfoItem.uuid).requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                (activity as OthersActivity).stopLoadMoreAndRefresh()
                val response1 = RequestStuffListResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    //                    CommonToast.getInstance(response1.mMsg).show();
                    if (mStuffNextPage == 0) mLikeList.clear()
                    if (response1.stuffListItem.list != null && response1.stuffListItem.list.isNotEmpty()) {
                        Collections.addAll(mLikeList, *response1.stuffListItem.list)

                        isLoadMoreEnable = response1.stuffListItem.list.size >= pageSize
                        mStuffNextPage++
                        mAdapter.notifyDataSetChanged()
                    }
                    updateNullView(mLikeList.size)
                } else {

                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
                mLoadMoreStatusInterface?.let { it.onFinishLoadMore(isLoadMoreEnable) }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                (activity as OthersActivity).stopLoadMoreAndRefresh()
                mLoadMoreStatusInterface?.let { it.onFinishLoadMore(isLoadMoreEnable) }
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun updateNullView(size: Int) {
        if (size > 0) {
            this.m_others_stuff_null_lp.visibility = View.GONE
            m_others_stuff_list.visibility = View.VISIBLE
        } else {
            this.m_others_stuff_null_lp.visibility = View.VISIBLE
            m_others_stuff_list.visibility = View.GONE
        }
    }


    override fun onDestroy() {

        GSYVideoManager.onPause()
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)

        super.onDestroy()
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
        GSYVideoManager.onPause()
    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            val postUuid = eventMessage.mObject as String
            for (i in mLikeList.indices) {
                if (Utils.isStringEquals(postUuid, mLikeList[i].postUuid)) {
                    mLikeList.removeAt(i)
                    mAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM) {
            val item = eventMessage.mObject as FreshNewItem
            for (i in mLikeList.indices) {
                if (Utils.isStringEquals(item.postUuid, mLikeList[i].postUuid)) {
                    mLikeList[i] = item
                    mAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            val postUuid = eventMessage.mObject as String
            for (i in mLikeList.indices) {
                if (Utils.isStringEquals(postUuid, mLikeList[i].postUuid)) {
                    mLikeList[i].browseCount++
                    mAdapter.notifyDataSetChanged()
                    return
                }
            }
        }
    }


    private var mLoadMoreStatusInterface: LoadMoreStatusInterface? = null

    fun setLoadMoreStatusInterface(loadMoreStatusInterface: LoadMoreStatusInterface) {
        mLoadMoreStatusInterface = loadMoreStatusInterface
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
                CommonToast.getInstance(activity?.resources?.getString(R.string.string_get_fresh_news_fail_alert), Toast.LENGTH_SHORT).show()
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
                    for (index in mLikeList.indices) {

                        if (item.id == mLikeList[index].id) {
                            mLikeList[index].isLike = item.isLike
                            mLikeList[index].likeCount = item.likeCount
                            mAdapter.notifyItemChanged(index)
                        }
                    }

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
                    for (index in mLikeList.indices) {

                        if (item.id == mLikeList[index].id) {
                            mLikeList[index].isLike = item.isLike
                            mLikeList[index].likeCount = item.likeCount
                            mAdapter.notifyItemChanged(index)
                        }
                    }
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


    fun refresh() {
        requestLikeList(true)
    }

    fun loadMore() {
        requestLikeList(false)
    }

}
