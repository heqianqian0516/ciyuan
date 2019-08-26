package com.ciyuanplus.mobile.module.news.news;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyFragment;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.NewsSearchActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.news.select_show_type.SelectShowTypeActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.ImageCycleView;
import com.ciyuanplus.mobile.widget.LoadingDialog;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/11.
 */

public class NewsFragment extends MyFragment implements NewsFragmentContract.View, EventCenterManager.OnHandleEventListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.m_main_news_search_image)
    ImageView mMainNewsSearchImage;
    @BindView(R.id.m_main_news_list)
    IRecyclerView mMainNewsList;
    @BindView(R.id.m_main_news_null_lp)
    LinearLayout mMainNewsNullLp;
    @BindView(R.id.m_main_news_selected_name)
    TextView mMainNewsSelectedName;
    @BindView(R.id.m_main_news_select_triangle)
    ImageView mMainNewsSelectTriangle;

    private View mMainNewsTopLp;
    private ImageCycleView mMainNewsTopBanner;
    private ImageView mMainNewsImageHead;
    private RecyclerView mMainNewsQuickEnter;
    @Inject

    NewsFragmentPresenter mPresenter;
    private final RotateAnimation rotateAnim = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private final RotateAnimation resetAnim = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private LoadMoreFooterView loadMoreFooterView;
    private Dialog mLoadingDialog;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mUnbinder = ButterKnife.bind(this, view);


        loadMoreFooterView = (LoadMoreFooterView) mMainNewsList.getLoadMoreFooterView();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity()) {//解决显示不全的问题
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
        mMainNewsList.setLayoutManager(linearLayoutManager1);
        mMainNewsList.setOnRefreshListener(this);
        mMainNewsList.setOnLoadMoreListener(this);

        // 绑定上面的View
        mMainNewsTopLp = inflater.inflate(R.layout.fragment_news_top_layout, null);
        mMainNewsList.addHeaderView(mMainNewsTopLp);

        mMainNewsTopBanner = mMainNewsTopLp.findViewById(R.id.m_main_news_top_banner);
        mMainNewsImageHead = mMainNewsTopLp.findViewById(R.id.m_main_news_image_head);
        mMainNewsQuickEnter = mMainNewsTopLp.findViewById(R.id.m_main_news_quick_enter);

        mMainNewsTopBanner.setOnPageClickListener((imageView, imageInfo) -> {
            if (imageInfo.type == 0) return;
            if (imageInfo.allowedUserState == 1 && !LoginStateManager.isLogin()) {
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
        ViewGroup.LayoutParams lp = mMainNewsImageHead.getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenWidth / 75 * 25;
        mMainNewsImageHead.setLayoutParams(lp);
        mMainNewsTopBanner.setHeight(25);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());//   LinearLayoutManager不能共用 真是坑爹
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMainNewsQuickEnter.setLayoutManager(linearLayoutManager);

        LoadingDialog.Builder builder = new LoadingDialog.Builder(getActivity());
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);

        DaggerNewsFragmentPresenterComponent.builder().newsFragmentPresenterModule(new NewsFragmentPresenterModule(this)).build().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this);// 添加小区列表变化的监听事件
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);

    }

    @Override
    public void setSelectedName(String s) {
        mMainNewsSelectedName.setText(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);

    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        mPresenter.handleEventCenterEvent(eventMessage);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.m_main_news_search_image, R.id.m_main_news_selected_name, R.id.m_main_news_select_triangle})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_main_news_search_image://
                Intent intent = new Intent(getActivity(), NewsSearchActivity.class);
                intent.putExtra(Constants.INTENT_SEARCH_NEWS_TYPE, 0);
                startActivity(intent);
                break;
            case R.id.m_main_news_selected_name://
            case R.id.m_main_news_select_triangle:
                mPresenter.goSelectTypeActivity();

                // 执行动画
                rotateAnim.setFillAfter(true);
                rotateAnim.setDuration(200);
                rotateAnim.setRepeatCount(0);
                mMainNewsSelectTriangle.startAnimation(rotateAnim);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SELECT_SHOW_TYPE) {
            //先回退动画
            resetAnim.setFillAfter(true);
            resetAnim.setDuration(200);
            resetAnim.setRepeatCount(0);
            mMainNewsSelectTriangle.startAnimation(resetAnim);
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(SelectShowTypeActivity.SELECTED, 0);
                mPresenter.updateShowType(type);
            }
        }
    }

    @Override
    public void stopRereshAndLoad() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mMainNewsList.setRefreshing(false);
    }

    @Override
    public void updateListView(boolean showList) {
        if (showList) {
            mMainNewsList.setVisibility(View.VISIBLE);
            mMainNewsNullLp.setVisibility(View.GONE);
        } else {
            mMainNewsList.setVisibility(View.GONE);
            mMainNewsNullLp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    public void updateTopView() {
        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate();

        if (mPresenter.getTopList().size() == 0) {// 设置默认的 mMainNewsImageHead  mMainNewsTopBanner
            mMainNewsImageHead.setVisibility(View.VISIBLE);
            mMainNewsTopBanner.setVisibility(View.GONE);
            mMainNewsTopBanner.setOnClickListener(null);
        } else if (mPresenter.getTopList().size() == 1) {

            mMainNewsImageHead.setVisibility(View.VISIBLE);
            mMainNewsTopBanner.setVisibility(View.GONE);
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.getTopList().get(0).img)
                    .apply(options).into(mMainNewsImageHead);
            mMainNewsImageHead.setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    if (mPresenter.getTopList().get(0).type == 0) return;
                    if (mPresenter.getTopList().get(0).allowedUserState == 1 && !LoginStateManager.isLogin()) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                        return;
                    }
                    if (mPresenter.getTopList().get(0).type == 1) {// 点击跳转网页
                        Intent intent = new Intent(getActivity(), JsWebViewActivity.class);
                        intent.putExtra(Constants.INTENT_OPEN_URL, mPresenter.getTopList().get(0).param);
                        startActivity(intent);
                    } else if (mPresenter.getTopList().get(0).type == 2) {// 进入banner详情
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPresenter.getTopList().get(0).bizUuid);
                        intent.putExtra(Constants.INTENT_IS_BANNER, true);
                        startActivity(intent);
                    }
                }
            });
//            ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + mTopList.get(0).img, mMainNewsImageHead);
        } else {
            mMainNewsImageHead.setVisibility(View.GONE);
            mMainNewsTopBanner.setVisibility(View.VISIBLE);
            mMainNewsTopBanner.loadData(mPresenter.getTopList(), (imageInfo) -> {
                ImageView imageView = new ImageView(getActivity());

                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + imageInfo.img).apply(options).into(imageView);
//                    ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + imageInfo.img,imageView);
                return imageView;
            });
        }

    }

    @Override
    public void changeTopBarVisible(boolean b) {
        mMainNewsTopLp.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public IRecyclerView getListView() {
        return mMainNewsList;
    }

    @Override
    public RecyclerView getQuickEnterListView() {
        return mMainNewsQuickEnter;
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mPresenter.doRequest(true);
    }

    @Override
    public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            mPresenter.doRequest(false);
        }
    }

    @Override
    public Context getDefaultContext() {
        return getActivity();
    }
}
