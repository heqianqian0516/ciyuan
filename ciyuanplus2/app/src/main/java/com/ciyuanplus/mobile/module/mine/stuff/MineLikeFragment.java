package com.ciyuanplus.mobile.module.mine.stuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity;
import com.ciyuanplus.mobile.adapter.HomeFragmentAdapter;
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface;
import com.ciyuanplus.mobile.manager.AMapLocationManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.mine.mine.MineFragmentNew;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter;
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestMineStuffListApiParameter;
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse;
import com.ciyuanplus.mobile.net.response.RequestStuffListResponse;
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
import java.util.List;

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

public class MineLikeFragment extends LazyLoadBaseFragment implements EventCenterManager.OnHandleEventListener {

    private HomeFragmentAdapter mAdapter;
    @BindView(R.id.m_others_stuff_list)
    RecyclerView mOthersStuffList;
    @BindView(R.id.m_others_stuff_null_lp)
    LinearLayout mOthersStuffNullLp;
    private final ArrayList<FreshNewItem> mStuffList = new ArrayList<>();
    private int mStuffNextPage;
    private Unbinder mUnbinder;
    private LoadMoreStatusInterface mLoadMoreStatusInterface;
    private boolean isLoadMoreEnable;
    private LinearLayoutManager linearLayoutManager;

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        isLoadMoreEnable = loadMoreEnable;
    }

    public static MineLikeFragment newInstance() {
        Bundle bundle = new Bundle();
        MineLikeFragment contentFragment = new MineLikeFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_like, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mOthersStuffList.setLayoutManager(linearLayoutManager);
        mOthersStuffList.setNestedScrollingEnabled(true);//解决ScrollView嵌套RecyclerView导致滑动不流畅的问题
        mOthersStuffList.getRecycledViewPool().setMaxRecycledViews(0,10);
        mOthersStuffList.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        mAdapter = new HomeFragmentAdapter(mStuffList);

        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (position == -1) {
                return;
            }

            FreshNewItem item = (FreshNewItem) adapter.getItem(position);
            Intent intent = new Intent(MineLikeFragment.this.getActivity(), TwitterDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
            MineLikeFragment.this.getActivity().startActivity(intent);
        });
        mAdapter.setOnItemChildClickListener((adapter, view12, position) -> {

            FreshNewItem item = (FreshNewItem) adapter.getItem(position);

            if (view12.getId() == R.id.riv_head_image) {

                Intent intent = new Intent(getContext(), OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.userUuid);
                startActivity(intent);

            } else if (view12.getId() == R.id.tv_add) {
                requestFollowUser(item);
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

        mOthersStuffList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_other_stuff;
    }

    private void requestFollowUser(final FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_FOLLOW_USER_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new FollowOtherApiParameter(item.userUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestOtherInfoResponse response1 = new RequestOtherInfoResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("关注成功").show();
                    // 更新状态
                    FriendsItem friendsItem = new FriendsItem();
                    friendsItem.uuid = item.userUuid;
                    friendsItem.followType = 1;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance(getActivity().getResources().getString(R.string.string_get_fresh_news_fail_alert),
                        Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
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
                    // 更新状态
                    List<FreshNewItem> data = mAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (Utils.isStringEquals(item.postUuid, data.get(i).postUuid)) {

                            mAdapter.notifyItemChanged(i);
                        }
                    }
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
                    // 更新状态
                    List<FreshNewItem> data = mAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (Utils.isStringEquals(item.postUuid, data.get(i).postUuid)) {

                            mAdapter.notifyItemChanged(i);
                        }
                    }
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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        requestStuffList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void lazyLoad() {

    }

    public void requestStuffList(boolean reset) {
        if (reset) {
            mStuffNextPage = 0;
            mStuffList.clear();
        }
        requestStuffList();
    }

    private void requestStuffList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_USER_LIKE_LIST);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestMineStuffListApiParameter(UserInfoData.getInstance().getUserInfoItem().uuid, AMapLocationManager.getInstance().getLongitude(),
                AMapLocationManager.getInstance().getLatitude(), mStuffNextPage + ""
                , PAGE_SIZE + "", UserInfoData.getInstance().getUserInfoItem().uuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                MineFragmentNew parentFragment = (MineFragmentNew) getParentFragment();
                parentFragment.finishLoadMoreAndRefresh();

                RequestStuffListResponse response1 = new RequestStuffListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    if (mStuffNextPage == 0) mStuffList.clear();
                    if (response1.stuffListItem.list != null && response1.stuffListItem.list.length > 0) {
                        Collections.addAll(mStuffList, response1.stuffListItem.list);

                        mStuffNextPage++;
                        mAdapter.notifyDataSetChanged();

                        setLoadMoreEnable(response1.stuffListItem.list.length >= PAGE_SIZE);

                    }
                    updateNullView(mStuffList.size());
                } else {
                    setLoadMoreEnable(isLoadMoreEnable);

                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }

                if (mLoadMoreStatusInterface != null) {

                    mLoadMoreStatusInterface.onFinishLoadMore(isLoadMoreEnable);
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);

                MineFragmentNew parentFragment = (MineFragmentNew) getParentFragment();
                parentFragment.finishLoadMoreAndRefresh();
                setLoadMoreEnable(true);
                if (mLoadMoreStatusInterface != null) {

                    mLoadMoreStatusInterface.onFinishLoadMore(isLoadMoreEnable);
                }

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void updateNullView(int size) {
        if (size > 0) {
            this.mOthersStuffNullLp.setVisibility(View.GONE);
            mOthersStuffList.setVisibility(View.VISIBLE);
        } else {
            this.mOthersStuffNullLp.setVisibility(View.VISIBLE);
            mOthersStuffList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {

        GSYVideoManager.onPause();

        mUnbinder.unbind();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);

        super.onDestroy();
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause();

        GSYVideoManager.onPause();
    }



    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mStuffList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mStuffList.get(i).postUuid)) {
                    mStuffList.remove(i);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM) {
            FreshNewItem item = (FreshNewItem) eventMessage.mObject;
            for (int i = 0; i < mStuffList.size(); i++) {
                if (Utils.isStringEquals(item.postUuid, mStuffList.get(i).postUuid)) {
                    mStuffList.set(i, item);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mStuffList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mStuffList.get(i).postUuid)) {
                    mStuffList.get(i).browseCount++;
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void setLoadMoreStatusInterface(LoadMoreStatusInterface loadMoreStatusInterface) {
        mLoadMoreStatusInterface = loadMoreStatusInterface;
    }
}
