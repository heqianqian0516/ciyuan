package com.kot.myapplication


import android.content.Intent
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciyuanplus.mobile.R

import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity
import com.ciyuanplus.mobile.adapter.HomeQualityPostListAdapter
import com.ciyuanplus.mobile.adapter.MyPostListAdapter
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface
import com.ciyuanplus.mobile.manager.*
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity
import com.ciyuanplus.mobile.module.home.HomeFragment
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.mine.mine.MineFragmentNew
import com.ciyuanplus.mobile.module.mine.news.MineNewsFragment
import com.ciyuanplus.mobile.module.mine.stuff.MineLikeFragment
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.DeleteCount
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.bean.FriendsItem
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter
import com.ciyuanplus.mobile.net.parameter.RequestFreshNewsApiParameter
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.kris.baselibrary.base.LazyLoadBaseFragment
import com.litesuits.http.exception.HttpException
import com.litesuits.http.listener.HttpListener
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.CacheMode
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.fragment_others_post.*
import kotlinx.android.synthetic.main.fragment_others_post.m_others_post_null_lp
import kotlinx.android.synthetic.main.fragment_others_post.rcl_post_list
import kotlinx.android.synthetic.main.fragment_qualitygoods.*
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 *  精品页面
 */
class QualitygoodsFragment : LazyLoadBaseFragment(), EventCenterManager.OnHandleEventListener {
    override fun lazyLoad() {
    }

    private var mPostNextPage: Int = 0
    private lateinit var mPostAdapter: HomeQualityPostListAdapter

    private val mQualitygoodList = ArrayList<FreshNewItem>()
    var isLoadMoreEnable: Boolean = false
    private var mLoadMoreStatusInterface: LoadMoreStatusInterface? = null
    private val mContext = activity
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun getLayoutResId(): Int {
        return R.layout.fragment_qualitygoods
    }

    fun setLoadMoreStatusInterface(loadMoreStatusInterface: LoadMoreStatusInterface) {
        mLoadMoreStatusInterface = loadMoreStatusInterface
    }

    fun newInstance(): QualitygoodsFragment {
        val bundle = Bundle()
        val contentFragment = QualitygoodsFragment()
        contentFragment.arguments = bundle
        return contentFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(context)
        mPostAdapter = HomeQualityPostListAdapter(mQualitygoodList)
        rcl_post_list.adapter = mPostAdapter
        rcl_post_list.layoutManager = LinearLayoutManager(activity)
        rcl_post_list.isNestedScrollingEnabled = true
        rcl_post_list.recycledViewPool
        rcl_post_list.recycledViewPool.setMaxRecycledViews(0, 10)
        rcl_post_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                            mPostAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })

        mPostAdapter.setOnItemClickListener { adapter, view1, position ->
            if (position == -1) {
                return@setOnItemClickListener
            }

            val item = adapter.getItem(position) as FreshNewItem?
            val intent = Intent(activity, TwitterDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item?.postUuid)
            intent.putExtra(Constants.INTENT_BIZE_TYPE, item?.bizType)
            activity?.startActivity(intent)
        }
        mPostAdapter.setOnItemChildClickListener { adapter, view12, position ->

            val item = adapter.getItem(position) as FreshNewItem?

            if (view12.id == R.id.riv_head_image) {

                val intent = Intent(context, OthersActivity::class.java)
                intent.putExtra(Constants.INTENT_USER_ID, item?.userUuid)
                startActivity(intent)

            } else if (view12.id == R.id.ll_share) {
                val intent = Intent(context, ShareNewsPopupActivity::class.java)
                intent.putExtra(Constants.INTENT_NEWS_ITEM, item)
                startActivity(intent)
            } else if (view12.id == R.id.tv_add) {

                requestFollowUser(item);


            } else if (view12.id == R.id.ll_like) {
                if (!LoginStateManager.isLogin()) {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    return@setOnItemChildClickListener
                }

                if (item != null) {
                    if (item.isLike == 0) {
                        likePost(item)
                    } else {

                        cancelLikePost(item)
                    }
                }
            }
        }

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this)
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this)

        requestPostList()
      /*  //开启下拉加载
        rcl_post_list_test.setEnableLoadMore(true);
        //下拉加载监听
        rcl_post_list_test.setOnLoadMoreListener {
            mPostNextPage++
            requestPostList()

        }*/

    }
    private fun finishParentLoading() {
        val parentFragment = parentFragment as HomeFragment?
       // parentFragment?.finishLoadMoreAndRefresh()
    }
    private fun requestFollowUser(item: FreshNewItem?) {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_FOLLOW_USER_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(FollowOtherApiParameter(item?.userUuid).requestBody)
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
                    friendsItem.uuid = item?.userUuid
                    friendsItem.followType = 1
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem))
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                CommonToast.getInstance(activity!!.resources.getString(R.string.string_get_fresh_news_fail_alert),
                        Toast.LENGTH_SHORT).show()
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }



    override fun onFragmentPause() {
        super.onFragmentPause()

        GSYVideoManager.onPause()
    }


    fun requestPost(reset: Boolean) {
        if (reset) {
            mPostNextPage = 0
            mQualitygoodList.clear()
        }

        requestPostList()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()

        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this)
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this)
        requestPostList()
    }

    override fun onDetach() {
        super.onDetach()

        GSYVideoManager.releaseAllVideos()
    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT) {
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
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(uuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].isLike = isLike
                    mQualitygoodList[i].likeCount = likeCount
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            val postUuid = eventMessage.mObject as String
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(postUuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList.removeAt(i)
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            val postUuid = eventMessage.mObject as String
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(postUuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].browseCount++
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
            val postUuid = eventMessage.mObject as String
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(postUuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].commentCount++
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY) {
            //            String postUuid = (String) eventMessage.mObject;
            //            for (int i = 0; i < mOtherPublishList.size(); i++) {
            //                if (Utils.isStringEquals(postUuid, mOtherPublishList.get(i).postUuid)) {
            //                    mOtherPublishList.get(i).commentCount--;
            //                    mPostAdapter.notifyDataSetChanged();
            //                    return;
            //                }
            //            }


            val deleteCount = eventMessage.mObject as DeleteCount
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(deleteCount.postUUID, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].commentCount -= deleteCount.deleteCount
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }

        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED) {
            val postUuid = eventMessage.mObject as String
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(postUuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].isRated = 1
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED) {
            val postUuid = eventMessage.mObject as String
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(postUuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].isRated = 0
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED) {
            val postUuid = eventMessage.mObject as String
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(postUuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].isDislike = 1
                    mQualitygoodList[i].dislikeCount++
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED) {
            val postUuid = eventMessage.mObject as String
            for (i in mQualitygoodList.indices) {
                if (Utils.isStringEquals(postUuid, mQualitygoodList[i].postUuid)) {
                    mQualitygoodList[i].isDislike = 0
                    mQualitygoodList[i].dislikeCount--
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        }else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
            val friendsItem = eventMessage.mObject as FriendsItem
            for (i in mPostAdapter.data.indices) {
                if (Utils.isStringEquals(friendsItem.uuid, mPostAdapter.data[i].userUuid)) {
                    mPostAdapter.data[i].isFollow = friendsItem.followType
                    mPostAdapter.notifyItemChanged(i - mPostAdapter.headerLayoutCount)
                }
            }
        }

    }

    fun requestStuffList(reset: Boolean) {
        if (reset) {
            mPostNextPage = 0
            mQualitygoodList.clear()
        }
        requestPostList()
    }

    //列表请求
    fun requestPostList() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_HIGHT_LIGHT_LIST)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        val PAGE_SIZE = 5
        postRequest.setHttpBody<AbstractRequest<String>>(RequestFreshNewsApiParameter(mPostNextPage.toString() + "", PAGE_SIZE.toString() + "", UserInfoData.getInstance().userInfoItem.uuid).requestBody)
        //设置缓存
        postRequest.setCacheMode<AbstractRequest<String>>(CacheMode.NetFirst)
        postRequest.setCacheExpire<AbstractRequest<String>>(-1, TimeUnit.SECONDS)
        postRequest.setCacheDir<AbstractRequest<String>>(CacheManager.getInstance().settleDirectory)
        postRequest.setCacheKey<AbstractRequest<String>>(null)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                // 必须要加的
                finishParentLoading();

                val response1 = RequestFreshNewsResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.size > 0) {
                    if (mPostNextPage == 0) mQualitygoodList.clear()

                    isLoadMoreEnable = response1.freshNewsListItem.list.size >= PAGE_SIZE
                    Collections.addAll(mQualitygoodList, *response1.freshNewsListItem.list)
                    mPostAdapter.notifyDataSetChanged()
                    mPostNextPage++
                }
                mLoadMoreStatusInterface?.onFinishLoadMore(isLoadMoreEnable)
                updateNullView(mQualitygoodList.size)
            }


            override fun onFailure(e: HttpException?, response: Response<String>?) {

                super.onFailure(e, response)

                              finishParentLoading();

                mLoadMoreStatusInterface?.onFinishLoadMore(isLoadMoreEnable)
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
        //关闭下拉加载
      //  rcl_post_list_test.finishLoadMore();
    }

    private fun updateNullView(size: Int) {
        if (size > 0) {
            this.m_others_post_null_lp.visibility = View.GONE
            rcl_post_list.visibility = View.VISIBLE
        } else {
            this.m_others_post_null_lp.visibility = View.VISIBLE
            rcl_post_list.visibility = View.GONE
        }

    }

    //赞新鲜事
    private fun likePost(item: FreshNewItem) {
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

    private fun cancelLikePost(item: FreshNewItem) {
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

    companion object {

        fun newInstance(): QualitygoodsFragment {
            val bundle = Bundle()
            val contentFragment = QualitygoodsFragment()
            contentFragment.arguments = bundle
            return contentFragment
        }
    }
}
