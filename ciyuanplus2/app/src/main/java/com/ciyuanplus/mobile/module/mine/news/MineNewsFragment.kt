package com.ciyuanplus.mobile.module.mine.news

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.Unbinder
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity
import com.ciyuanplus.mobile.adapter.MyPostListAdapter
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.LoginStateManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity
import com.ciyuanplus.mobile.module.login.LoginActivity
import com.ciyuanplus.mobile.module.mine.mine.MineFragmentNew
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.DeleteCount
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter
import com.ciyuanplus.mobile.net.parameter.RequestFreshNewsApiParameter
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse
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
import kotlinx.android.synthetic.main.fragment_others_post.*
import java.util.*

/**
 * Created by Alen on 2018/1/26.
 * 我的页面动态Fragment
 */

class MineNewsFragment : LazyLoadBaseFragment(), EventCenterManager.OnHandleEventListener {

    override fun lazyLoad() {
    }


    private lateinit var mPostAdapter: MyPostListAdapter
    private val mOtherPublishList = ArrayList<FreshNewItem>()
    private var mPostNextPage: Int = 0
    private var mUnBinder: Unbinder? = null
    var isLoadMoreEnable: Boolean = false
    private var mLoadMoreStatusInterface: LoadMoreStatusInterface? = null
    private val mContext = activity

    fun setLoadMoreStatusInterface(loadMoreStatusInterface: LoadMoreStatusInterface) {
        mLoadMoreStatusInterface = loadMoreStatusInterface
    }


    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun getLayoutResId(): Int {
        return R.layout.fragment_others_post
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        mPostAdapter = MyPostListAdapter(mOtherPublishList)

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
    }

    override fun onFragmentPause() {
        super.onFragmentPause()

        GSYVideoManager.onPause()
    }


    fun requestPost(reset: Boolean) {
        if (reset) {
            mPostNextPage = 0
            mOtherPublishList.clear()
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
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(uuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].isLike = isLike
                    mOtherPublishList[i].likeCount = likeCount
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            val postUuid = eventMessage.mObject as String
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(postUuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList.removeAt(i)
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            val postUuid = eventMessage.mObject as String
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(postUuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].browseCount++
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
            val postUuid = eventMessage.mObject as String
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(postUuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].commentCount++
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
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(deleteCount.postUUID, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].commentCount -= deleteCount.deleteCount
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }

        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED) {
            val postUuid = eventMessage.mObject as String
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(postUuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].isRated = 1
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED) {
            val postUuid = eventMessage.mObject as String
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(postUuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].isRated = 0
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED) {
            val postUuid = eventMessage.mObject as String
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(postUuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].isDislike = 1
                    mOtherPublishList[i].dislikeCount++
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED) {
            val postUuid = eventMessage.mObject as String
            for (i in mOtherPublishList.indices) {
                if (Utils.isStringEquals(postUuid, mOtherPublishList[i].postUuid)) {
                    mOtherPublishList[i].isDislike = 0
                    mOtherPublishList[i].dislikeCount--
                    mPostAdapter.notifyDataSetChanged()
                    return
                }
            }
        }
    }

    private fun requestPostList() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_OTHER_PUBLISH_POST_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        val PAGE_SIZE = 20
        postRequest.setHttpBody<AbstractRequest<String>>(RequestFreshNewsApiParameter(mPostNextPage.toString() + "", PAGE_SIZE.toString() + "", UserInfoData.getInstance().userInfoItem.uuid).requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(activity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                // 必须要加的
                //                finishParentLoading();

                val response1 = RequestFreshNewsResponse(s)
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.size > 0) {
                    if (mPostNextPage == 0) mOtherPublishList.clear()

                    isLoadMoreEnable = response1.freshNewsListItem.list.size >= PAGE_SIZE
                    Collections.addAll(mOtherPublishList, *response1.freshNewsListItem.list)
                    mPostAdapter.notifyDataSetChanged()
                    mPostNextPage++
                }
                mLoadMoreStatusInterface?.onFinishLoadMore(isLoadMoreEnable)
                updateNullView(mOtherPublishList.size)
            }


            override fun onFailure(e: HttpException?, response: Response<String>?) {

                super.onFailure(e, response)

                //                finishParentLoading();

                mLoadMoreStatusInterface?.onFinishLoadMore(isLoadMoreEnable)
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    private fun finishParentLoading() {
        val parentFragment = parentFragment as MineFragmentNew?
        parentFragment?.finishLoadMoreAndRefresh()
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


    // 赞新鲜事
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

    // 取消赞新鲜事
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

        fun newInstance(): MineNewsFragment {
            val bundle = Bundle()
            val contentFragment = MineNewsFragment()
            contentFragment.arguments = bundle
            return contentFragment
        }
    }
}
