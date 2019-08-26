package com.ciyuanplus.mobile.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyFragment;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.AroundStuffAdapter;
import com.ciyuanplus.mobile.adapter.AroundWikiAdapter;
import com.ciyuanplus.mobile.adapter.LiveHoodAdapter;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.module.found.market.MarketActivity;
import com.ciyuanplus.mobile.module.live_hood.LiveHoodActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.module.wiki.around_wiki.AroundWikiActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.BannerItem;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.LiveHoodItem;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.net.parameter.RequestAroundStuffApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestAroundWikiApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestBannerApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestLiveHoodApiParameter;
import com.ciyuanplus.mobile.net.response.RequestAroundWikiResponse;
import com.ciyuanplus.mobile.net.response.RequestBannerListResponse;
import com.ciyuanplus.mobile.net.response.RequestLiveHoodResponse;
import com.ciyuanplus.mobile.net.response.RequestStuffListResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.ImageCycleView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/11.
 * 发现
 */

public class AroundFragment extends MyFragment implements EventCenterManager.OnHandleEventListener {
    @BindView(R.id.m_main_around_top_banner)
    ImageCycleView mMainAroundTopBanner;
    @BindView(R.id.m_main_around_image_head)
    ImageView mMainAroundImageHead;
    @BindView(R.id.m_main_around_live_hood_text1)
    TextView mMainAroundLiveHoodText1;
    @BindView(R.id.m_main_around_live_hood_lp)
    RelativeLayout mMainAroundLiveHoodLp;
    @BindView(R.id.m_main_around_live_hood_grid)
    RecyclerView mMainAroundLiveHoodGrid;
    @BindView(R.id.m_main_around_shop_text1)
    TextView mMainAroundShopText1;
    @BindView(R.id.m_main_around_shop_lp)
    RelativeLayout mMainAroundShopLp;
    @BindView(R.id.m_main_around_shop_grid)
    RecyclerView mMainAroundShopGrid;
    @BindView(R.id.m_main_around_baike_text1)
    TextView mMainAroundBaikeText1;
    @BindView(R.id.m_main_around_baike_grid)
    RecyclerView mMainAroundBaikeGrid;
    @BindView(R.id.m_main_around_baike_lp)
    LinearLayout mMainAroundBaikeLp;
    @BindView(R.id.m_main_around_welfare_text1)
    TextView mMainAroundWelfareText1;
    @BindView(R.id.m_main_around_welfare_image)
    ImageView mMainAroundWelfareImage;
    @BindView(R.id.m_main_around_welfare_lp)
    LinearLayout mMainAroundWelfareLp;
    private final ArrayList<BannerItem> mTopList = new ArrayList<>();

    private BannerItem mWelfareItem;
    private final ArrayList<LiveHoodItem> mLiveHoodList = new ArrayList<>();
    private final ArrayList<FreshNewItem> mStuffList = new ArrayList<>();
    private final ArrayList<WikiItem> mWikiList = new ArrayList<>();
    private AroundStuffAdapter mStuffAdapter;
    private AroundWikiAdapter mWikiAdapter;
    private LiveHoodAdapter mLiveHoodAdapter;
    private Unbinder mBind;
    private final RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
            .dontAnimate();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        mBind = ButterKnife.bind(this, view);

        mMainAroundTopBanner.setOnPageClickListener((imageView, imageInfo) -> {
            if (imageInfo.type == 0) return;
            if (imageInfo.allowedUserState == 1 && !LoginStateManager.isLogin()) {
                SharedPreferencesManager.putBoolean("Mainlayout", "main", false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                return;
            }
            if (imageInfo.type == 1) {// 点击跳转网页
                Intent intent = new Intent(getActivity(), JsWebViewActivity.class);
                intent.putExtra(Constants.INTENT_OPEN_URL, imageInfo.param);
                startActivity(intent);
            } else if (imageInfo.type == 2) {// 进入banner详情
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, imageInfo.bizUuid);
                intent.putExtra(Constants.INTENT_IS_BANNER, true);
                startActivity(intent);
            }
        });
        int screenWidth = Utils.getScreenWidth();// 获取屏幕宽度  
        ViewGroup.LayoutParams lp = mMainAroundImageHead.getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenWidth / 75 * 30;
        mMainAroundImageHead.setLayoutParams(lp);
        mMainAroundTopBanner.setHeight(30);

        ViewGroup.LayoutParams lp1 = mMainAroundWelfareImage.getLayoutParams();
        lp1.height = screenWidth / 75 * 30;
        mMainAroundWelfareImage.setLayoutParams(lp1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());//   LinearLayoutManager不能共用 真是坑爹
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMainAroundShopGrid.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMainAroundBaikeGrid.setLayoutManager(linearLayoutManager1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mMainAroundLiveHoodGrid.setLayoutManager(gridLayoutManager);

        mStuffAdapter = new AroundStuffAdapter(getActivity(), mStuffList, (v) -> {
            int position = mMainAroundShopGrid.getChildAdapterPosition(v);
            Intent intent = new Intent(getActivity(), StuffDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mStuffAdapter.getItem(position).postUuid);
            startActivity(intent);
        });
        mWikiAdapter = new AroundWikiAdapter(getActivity(), mWikiList, (v) -> {
            Intent intent = new Intent(getActivity(), AroundWikiActivity.class);
            startActivity(intent);
        });
        mLiveHoodAdapter = new LiveHoodAdapter(getActivity(), mLiveHoodList, (v) -> {
            int position = mMainAroundLiveHoodGrid.getChildAdapterPosition(v);
            if (mLiveHoodAdapter.getItem(position).allowedUserState == 1 && !LoginStateManager.isLogin()) {
                SharedPreferencesManager.putBoolean("Mainlayout", "main", false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                return;
            }
            Intent intent = new Intent(getActivity(), JsWebViewActivity.class);
            intent.putExtra(Constants.INTENT_OPEN_URL, mLiveHoodAdapter.getItem(position).url);
            startActivity(intent);
        });
        mMainAroundShopGrid.setAdapter(mStuffAdapter);
        mMainAroundBaikeGrid.setAdapter(mWikiAdapter);
        mMainAroundLiveHoodGrid.setAdapter(mLiveHoodAdapter);

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH, this);

        requestTopBanner();
        requestBottomBanner();
        requestStuffList();
        requestWikiList();
        requestLiveHoodList();
        return view;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestTopBanner();
            requestBottomBanner();
            requestStuffList();
            requestWikiList();
            requestLiveHoodList();
        }
    }


    private void requestTopBanner() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_BANNER_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestBannerApiParameter("1").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestBannerListResponse response1 = new RequestBannerListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    // 存到本地里面
//                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
//                            Constants.SHARED_BANNER_LIST_CONTENT, response1.result);
                    mTopList.clear();
                    Collections.addAll(mTopList, response1.bannerListItem.list);

                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
                updateTopView();
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                updateTopView();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void requestBottomBanner() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_BANNER_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestBannerApiParameter("3").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestBannerListResponse response1 = new RequestBannerListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    // 存到本地里面
//                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
//                            Constants.SHARED_BANNER_LIST_CONTENT, response1.result);
                    if (!(response1.bannerListItem != null && response1.bannerListItem.list.length > 0)) {
                        return;
                    }
                    mWelfareItem = response1.bannerListItem.list[0];
                }  //CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();

                updateWelfareView();
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                updateTopView();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    /**
     * 刷新 顶部的数据
     * 图片需要异步加载和本地缓存
     */
    private void updateTopView() {


        if (mTopList.size() == 0) {// 设置默认的
            mMainAroundImageHead.setVisibility(View.VISIBLE);
            mMainAroundTopBanner.setVisibility(View.GONE);
            mMainAroundTopBanner.setOnClickListener(null);
        } else if (mTopList.size() == 1) {
            mMainAroundImageHead.setVisibility(View.VISIBLE);
            mMainAroundTopBanner.setVisibility(View.GONE);
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mTopList.get(0).img).apply(options).into(mMainAroundImageHead);
            mMainAroundImageHead.setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    if (mTopList.get(0).type == 0) return;
                    if (mTopList.get(0).allowedUserState == 1 && !LoginStateManager.isLogin()) {
                        SharedPreferencesManager.putBoolean("Mainlayout", "main", false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    } else if (mTopList.get(0).type == 1) {// 点击跳转网页
                        Intent intent = new Intent(getActivity(), JsWebViewActivity.class);
                        intent.putExtra(Constants.INTENT_OPEN_URL, mTopList.get(0).param);
                        startActivity(intent);
                    } else if (mTopList.get(0).type == 2) {// 进入banner详情
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mTopList.get(0).bizUuid);
                        intent.putExtra(Constants.INTENT_IS_BANNER, true);
                        startActivity(intent);
                    }
                }
            });
//            ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + mTopList.get(0).img, mMainAroundImageHead);
        } else {
            mMainAroundImageHead.setVisibility(View.GONE);
            mMainAroundTopBanner.setVisibility(View.VISIBLE);
            mMainAroundTopBanner.loadData(mTopList, (imageInfo) -> {
                ImageView imageView = new ImageView(getActivity());
                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + imageInfo.img).apply(options).into(imageView);
//                    ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + imageInfo.img,imageView);
                return imageView;
            });
        }

    }

    private void updateWelfareView() {
        if (mWelfareItem == null) mMainAroundWelfareLp.setVisibility(View.GONE);
        else {
            mMainAroundWelfareLp.setVisibility(View.VISIBLE);
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mWelfareItem.img)
                    .apply(options).into(mMainAroundWelfareImage);
            mMainAroundWelfareLp.setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    if (mWelfareItem.type == 0) return;
                    if (mWelfareItem.allowedUserState == 1 && !LoginStateManager.isLogin()) {
                        SharedPreferencesManager.putBoolean("Mainlayout", "main", false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    } else if (mWelfareItem.type == 1) {// 点击跳转网页
                        Intent intent = new Intent(getActivity(), JsWebViewActivity.class);
                        intent.putExtra(Constants.INTENT_OPEN_URL, mWelfareItem.param);
                        startActivity(intent);
                    } else if (mWelfareItem.type == 2) {// 进入banner详情
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mWelfareItem.bizUuid);
                        intent.putExtra(Constants.INTENT_IS_BANNER, true);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void requestWikiList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_COMMUNITY_WIKI_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestAroundWikiApiParameter("0", "3", "0").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestAroundWikiResponse response1 = new RequestAroundWikiResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    if (response1.wikiListItem.list != null) {
                        mWikiList.clear();
                        Collections.addAll(mWikiList, response1.wikiListItem.list);
                        mWikiAdapter.notifyDataSetChanged();
                    }
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 请求民生   4个
    private void requestLiveHoodList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_LIVE_HOOD_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestLiveHoodApiParameter("0", "4").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestLiveHoodResponse response1 = new RequestLiveHoodResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    //民生一定是大于4个的  这里跟后台确认了
                    if (response1.liveHoodListItem.list != null) {
                        mLiveHoodList.clear();
                        Collections.addAll(mLiveHoodList, response1.liveHoodListItem.list);
                        mLiveHoodAdapter.notifyDataSetChanged();
                    }
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 请求最近的物品列表，只请求3个
    private void requestStuffList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_AROUND_BANNER_STUFF_LIST);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestAroundStuffApiParameter("0", "6").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestStuffListResponse response1 = new RequestStuffListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    //物品一定是大于3个的  这里跟后台确认了
                    if (response1.stuffListItem.list != null) {
                        mStuffList.clear();
                        Collections.addAll(mStuffList, response1.stuffListItem.list);
                        mStuffAdapter.notifyDataSetChanged();
                    }
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST) {
            requestStuffList();
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH) {
            requestWikiList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();

    }

    @OnClick({R.id.m_main_around_live_hood_lp, R.id.m_main_around_shop_lp, R.id.m_main_around_baike_lp, R.id.m_main_around_welfare_lp})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_main_around_live_hood_lp:
                Intent intent = new Intent(getActivity(), LiveHoodActivity.class);
                startActivity(intent);
                break;
            case R.id.m_main_around_shop_lp:
                Intent intent1 = new Intent(getActivity(), MarketActivity.class);
                startActivity(intent1);
                break;
            case R.id.m_main_around_baike_lp:
                Intent intent2 = new Intent(getActivity(), AroundWikiActivity.class);
                startActivity(intent2);
                break;
            case R.id.m_main_around_welfare_lp:
                break;
        }
    }
}
