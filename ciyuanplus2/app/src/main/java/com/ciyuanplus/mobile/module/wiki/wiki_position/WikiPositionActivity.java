package com.ciyuanplus.mobile.module.wiki.wiki_position;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps2d.MapView;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/11/6.
 * <p>
 * 地址详细页面， 使用地图 标记处地址所在位置
 */

public class WikiPositionActivity extends MyBaseActivity implements WikiPositionContract.View {
    @BindView(R.id.m_wiki_position_map)
    MapView mWikiPositionMap;
    @BindView(R.id.m_wiki_position_back)
    ImageView mWikiPositionBack;

    @Inject

    WikiPositionPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_wiki_positioin);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView(savedInstanceState);
        mPresenter.initData(getIntent());// 初始化数据
    }

    private void initView(Bundle savedInstanceState) {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerWikiPositionPresenterComponent.builder()
                .wikiPositionPresenterModule(new WikiPositionPresenterModule(this)).build().inject(this);
        mWikiPositionMap.onCreate(savedInstanceState);// 此方法必须调用
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mWikiPositionMap.onResume();// 此方法必须调用，否则会泄漏
    }

    @Override
    public MapView getMapView() {
        return mWikiPositionMap;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWikiPositionMap.onPause();// 此方法必须调用，否则会泄漏
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWikiPositionMap.onDestroy();// 此方法必须调用，否则会泄漏
    }

    @OnClick(R.id.m_wiki_position_back)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_wiki_position_back) {
            onBackPressed();
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
