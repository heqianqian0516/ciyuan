package com.ciyuanplus.mobile.module.video;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;
import com.kris.baselibrary.base.LazyLoadBaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 视频推荐页
 */
public class RecommendFragment extends LazyLoadBaseFragment implements IVideoContract.IVideoView {

    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;
    @BindView(R.id.refresh_layout_recommend_video)
    SmartRefreshLayout refreshLayout;
    private Unbinder mUnbinder;
    private RecommendDataAdapter recommendDataAdapter;
    private VideoPresenter mPresenter;
    private int page = 1;
    private int pageSize = 10;
    private List<RecommendData.DataBean.ListBean> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        requestData();
        rvRecommend.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(3);
                outRect.left = Utils.dip2px(3);
            }
        });
        initView();
        return view;
    }

    /**
     * 请求网络 获取数据
     */
    private void requestData() {
        mPresenter = new VideoPresenter(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("userUuid", UserInfoData.getInstance().getUserInfoItem().uuid);
        params.put("pager", page + "");
        params.put("pageSize", pageSize + "");
        mPresenter.recommendVideo(params);
    }

    private void initView() {

        //初始化的时候默认没有数据，显示空布局
        getData(1);
        refreshView();
        smartRefreshView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void lazyLoad() {

    }

    /**
     * 刷新消息列表
     */
    private void refreshView() {
        //1,加载空布局文件，便于第五步适配器在没有数据的时候加载
        View emptyView = View.inflate(getActivity(), R.layout.list_item_recommend_empty_layout, null);
        //2，设置GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rvRecommend.setLayoutManager(gridLayoutManager);
        //3，初始化一个无数据的适配器
        recommendDataAdapter = new RecommendDataAdapter();
        //4，绑定recyclerView和适配器
        rvRecommend.setAdapter(recommendDataAdapter);
        //5，给recyclerView设置空布局
        recommendDataAdapter.setEmptyView(emptyView);
        //6，给recyclerView的每一个子列表添加点击事件
        rvRecommend.addOnItemTouchListener(new OnItemChildClickListener() {
            private int isLike;
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                int id = view.getId();
                if (list != null && list.size() > 0) {
                    RecommendData.DataBean.ListBean bean = list.get(position);
                    isLike = bean.getIsLike();
                }
                TextView tvGiveALike = view.findViewById(R.id.tv_give_a_like);
                switch (id) {
                    case R.id.iv_cover_recommend:
                        //  TODO 跳转大图

                        break;
                    case R.id.tv_give_a_like:
                        Drawable whiteHeart = getActivity().getResources().getDrawable(R.drawable.icon_white_hearts);
                        Drawable redHeart = getActivity().getResources().getDrawable(R.drawable.icon_red_hearts);
                        if (isLike == 0) {
                            whiteHeart.setBounds(0, 0, whiteHeart.getMinimumWidth(), whiteHeart.getMinimumHeight());
                            tvGiveALike.setCompoundDrawables(whiteHeart, null, null, null);
                        }
                        if (isLike == 1) {
                            redHeart.setBounds(0, 0, redHeart.getMinimumWidth(), redHeart.getMinimumHeight());
                            tvGiveALike.setCompoundDrawables(redHeart, null, null, null);
                        }
                        break;
                }

            }
        });
    }

    /**
     * 增加下拉刷新和上拉加载的监听方法
     */
    private void smartRefreshView() {
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            //下拉刷新,一般添加调用接口获取数据的方法
            getData(2);
            refreshLayout.finishRefresh(500);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            getData(3);
            refreshLayout.finishLoadMore(500);
        });
    }


    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void success(String result) {
        Gson gson = new Gson();
        RecommendData recommendData = gson.fromJson(result, RecommendData.class);
        RecommendData.DataBean data = recommendData.getData();
        list = data.getList();
        if (list == null || list.size() == 0) {
            return;
        }
        if (recommendDataAdapter == null || page == 1) {
            recommendDataAdapter = new RecommendDataAdapter(list, getActivity());
            recommendDataAdapter.addData(list);
            rvRecommend.setAdapter(recommendDataAdapter);
            rvRecommend.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        } else {
            recommendDataAdapter.addData(list);
            recommendDataAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void failure(String msg) {
    }

    /**
     * 获取数据的方法
     *
     * @param mode 模式：1为刚开始进来加载数据 空数据 2为下拉刷新 3为上拉加载
     */
    private void getData(int mode) {
        //添加临时数据，一般直接从接口获取
        switch (mode) {
            case 1:

                break;
            case 2:
                //更新数据
                page = 1;
                requestData();
                break;
            case 3:
                page++;
                requestData();
                break;
        }
    }
}
