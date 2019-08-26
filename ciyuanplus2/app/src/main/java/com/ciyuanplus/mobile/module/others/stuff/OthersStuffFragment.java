package com.ciyuanplus.mobile.module.others.stuff;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyFragment;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.StuffAdapter;
import com.ciyuanplus.mobile.manager.AMapLocationManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.RequestMineStuffListApiParameter;
import com.ciyuanplus.mobile.net.response.RequestStuffListResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/26.
 */

public class OthersStuffFragment extends MyFragment implements EventCenterManager.OnHandleEventListener {

    private StuffAdapter mAdapter;
    @BindView(R.id.m_others_stuff_list)
    RecyclerView mOthersStuffList;
    @BindView(R.id.m_others_stuff_null_lp)
    LinearLayout mOthersStuffNullLp;
    private final ArrayList<FreshNewItem> mStuffList = new ArrayList<>();
    private int mStuffNextPage;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_stuff, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mOthersStuffList.setLayoutManager(gridLayoutManager);
        mOthersStuffList.setNestedScrollingEnabled(false);//解决ScrollView嵌套RecyclerView导致滑动不流畅的问题 
        mOthersStuffList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
                outRect.right = Utils.dip2px(10);
            }
        });
        mAdapter = new StuffAdapter(getActivity(), mStuffList, (v) -> {
            int postion = mOthersStuffList.getChildLayoutPosition(v);
            FreshNewItem item = mAdapter.getItem(postion);
            Intent intent = new Intent(getActivity(), StuffDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            startActivity(intent);
        });
        mOthersStuffList.setAdapter(mAdapter);
        return view;
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
        mUnbinder.unbind();
    }

    public void requestStuffList(boolean reset) {
        if (reset) mStuffNextPage = 0;
        requestStuffList();
    }

    private void requestStuffList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_USER_STUFF_LIST);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestMineStuffListApiParameter(((OthersActivity) getActivity()).getUserUuid(), AMapLocationManager.getInstance().getLongitude(),
                AMapLocationManager.getInstance().getLatitude(), mStuffNextPage + "", PAGE_SIZE + "", UserInfoData.getInstance().getUserInfoItem().uuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ((OthersActivity) getActivity()).stopLoadMoreAndRefresh();
                RequestStuffListResponse response1 = new RequestStuffListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    if (mStuffNextPage == 0) mStuffList.clear();
                    if (response1.stuffListItem.list != null && response1.stuffListItem.list.length > 0) {
                        Collections.addAll(mStuffList, response1.stuffListItem.list);
                        mStuffNextPage++;
                        mAdapter.notifyDataSetChanged();
                    }
                    updateNullView(mStuffList.size());
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                ((OthersActivity) getActivity()).stopLoadMoreAndRefresh();
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
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);

        super.onDestroy();
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
}
