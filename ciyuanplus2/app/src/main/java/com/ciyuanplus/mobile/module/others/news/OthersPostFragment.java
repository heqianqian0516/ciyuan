package com.ciyuanplus.mobile.module.others.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity;
import com.ciyuanplus.mobile.adapter.MyPostListAdapter;
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.DeleteCount;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestFreshNewsApiParameter;
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.kris.baselibrary.base.LazyLoadBaseFragment;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/26.
 */
@SuppressLint("ValidFragment")
public class OthersPostFragment extends LazyLoadBaseFragment implements EventCenterManager.OnHandleEventListener {


    private MyPostListAdapter mPostAdapter;
    @BindView(R.id.rcl_post_list)
    RecyclerView mPostRecycleView;
    @BindView(R.id.m_others_post_null_lp)
    LinearLayout mOthersPostNullLp;
    private final ArrayList<FreshNewItem> mPostList = new ArrayList<>();
    private int mPostNextPage;
    private Unbinder mUnBinder;
    private LoadMoreStatusInterface mLoadMoreStatusInterface;
    private boolean isLoadMoreEnable;
    private LinearLayoutManager linearLayoutManager;

    /**
     * 传入需要的参数，设置给arguments
     */
    public static OthersPostFragment newInstance() {
        Bundle bundle = new Bundle();
        OthersPostFragment contentFragment = new OthersPostFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_others_post;
    }
    @Override
    public void onFragmentPause() {
        super.onFragmentPause();

        GSYVideoManager.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUnBinder = ButterKnife.bind(this, view);

        mPostAdapter = new MyPostListAdapter(mPostList);
        mPostRecycleView.setAdapter(mPostAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mPostRecycleView.setLayoutManager(linearLayoutManager);

        mPostRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        if(!GSYVideoManager.isFullState(getActivity())) {
                            GSYVideoManager.releaseAllVideos();
                            mPostAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });


        mPostAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (position == -1) {
                return;
            }

            FreshNewItem item = (FreshNewItem) adapter.getItem(position);
            Intent intent = new Intent(getActivity(), TwitterDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
            getActivity().startActivity(intent);
        });
        mPostAdapter.setOnItemChildClickListener((adapter, view12, position) -> {

            FreshNewItem item = (FreshNewItem) adapter.getItem(position);

            if (view12.getId() == R.id.riv_head_image) {

                Intent intent = new Intent(getContext(), OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.userUuid);
                startActivity(intent);

            } else if (view12.getId() == R.id.ll_share) {
                Intent intent = new Intent(getContext(), ShareNewsPopupActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, item);
                startActivity(intent);
            } else if (view12.getId() == R.id.ll_like) {
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if (item != null) {
                    if (item.isLike == 0) {
                        likePost(item);
                    } else {

                        cancelLikePost(item);
                    }
                }
            }
        });



        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);

        requestPost(true);
    }

    public void requestPost(boolean reset) {
        if (reset) {
            mPostNextPage = 0;
            mPostList.clear();
        }

        requestOtherPost();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    @Override
    public void lazyLoad() {

    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT) {
            String uuid;
            int isLike;
            int likeCount;
            if (eventMessage.mObject instanceof FreshNewItem) {
                FreshNewItem item = (FreshNewItem) eventMessage.mObject;
                uuid = item.postUuid;
                isLike = item.isLike;
                likeCount = item.likeCount;
            } else {
                FreshNewItem item = (FreshNewItem) eventMessage.mObject;
                uuid = item.postUuid;
                isLike = item.isLike;
                likeCount = item.likeCount;
            }
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(uuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).isLike = isLike;
                    mPostList.get(i).likeCount = likeCount;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.remove(i);
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).browseCount++;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).commentCount++;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).commentCount--;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }


            DeleteCount deleteCount = (DeleteCount) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(deleteCount.postUUID, mPostList.get(i).postUuid)) {
                    mPostList.get(i).commentCount -= deleteCount.deleteCount;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }

        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).isRated = 1;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).isRated = 0;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).isDislike = 1;
                    mPostList.get(i).dislikeCount++;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mPostList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mPostList.get(i).postUuid)) {
                    mPostList.get(i).isDislike = 0;
                    mPostList.get(i).dislikeCount--;
                    mPostAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    private void requestOtherPost() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_OTHER_PUBLISH_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestFreshNewsApiParameter(mPostNextPage + "", PAGE_SIZE + "", ((OthersActivity) getActivity()).getUserUuid()).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                // 必须要加的
//
                if (mLoadMoreStatusInterface != null) {
                    mLoadMoreStatusInterface.onFinishLoadMore(isLoadMoreEnable);
                }


                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    setLoadMoreEnable(isLoadMoreEnable);
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                    if (mPostNextPage == 0) mPostList.clear();
                    Collections.addAll(mPostList, response1.freshNewsListItem.list);
                    setLoadMoreEnable(response1.freshNewsListItem.list.length > PAGE_SIZE);
                    mPostAdapter.notifyDataSetChanged();
                    mPostNextPage++;
                }


                updateNullView(mPostList.size());
            }


            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
//                ((OthersActivity) getActivity()).stopLoadMoreAndRefresh();
                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();


                if (mLoadMoreStatusInterface != null) {
                    mLoadMoreStatusInterface.onLoadMoreError();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void updateNullView(int size) {
        if (size > 0) {
            this.mOthersPostNullLp.setVisibility(View.GONE);
            mPostRecycleView.setVisibility(View.VISIBLE);
        } else {
            this.mOthersPostNullLp.setVisibility(View.VISIBLE);
            mPostRecycleView.setVisibility(View.GONE);
        }
    }

    public void setLoadMoreStatusInterface(LoadMoreStatusInterface loadMoreStatusInterface) {
        mLoadMoreStatusInterface = loadMoreStatusInterface;
    }

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        isLoadMoreEnable = loadMoreEnable;
    }


    // 赞新鲜事
    private void likePost(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_zan);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new LikeOperaApiParameter(item.bizType + "", item.postUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    item.isLike = 1;
                    item.likeCount++;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, item));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance("操作失败").show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 取消赞新鲜事
    private void cancelLikePost(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_LIKE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ItemOperaApiParameter(item.postUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    item.isLike = 0;
                    if (item.likeCount > 0) item.likeCount--;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, item));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance("操作失败").show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    public void refresh() {
        requestPost(true);
    }

    public void loadMore() {
        requestPost(false);
    }

}
