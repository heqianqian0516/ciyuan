package com.ciyuanplus.mobile.module.register.select_location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.SelectPosiAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.popup.select_location_guide.SelectLocationGuideActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.google.gson.Gson;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/6.
 * <p>
 * 选择小区页面, 主要负责： 1.跟高德交互  2.创建自定义小区   3. 搜索小区列表
 */

public class SelectLocationActivity extends MyBaseActivity implements SelectLocationContract.View {

    @Inject
    public SelectLocationPresenter mPresenter;
    @BindView(R.id.m_select_location_map)
    MapView mSelectLocationMap;
    @BindView(R.id.m_select_location_back_image)
    ImageView mSelectLocationBackImage;
    @BindView(R.id.m_select_location_complete_image)
    ImageView mSelectLocationCompleteImage;
    @BindView(R.id.m_select_location_search_edit)
    LinearLayout mSelectLocationSearchEdit;
    @BindView(R.id.m_select_location_top_lp)
    RelativeLayout mSelectLocationTopLp;
    @BindView(R.id.m_select_location_list_title)
    TextView mSelectLocationListTitle;
    @BindView(R.id.m_select_location_list)
    ListView mSelectLocationList;
    @BindView(R.id.m_select_location_list_lp)
    LinearLayout mSelectLocationListLp;
    @BindView(R.id.m_select_location_found)
    ImageView mSelectLocationFound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_location);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        if (!SharedPreferencesManager.getBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_HAS_GUIDE, false)) {
            Intent intent = new Intent(this, SelectLocationGuideActivity.class);
            startActivity(intent);
        }

        this.initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerSelectLocationPresenterComponent.builder()
                .selectLocationPresenterModule(new SelectLocationPresenterModule(this)).build().inject(this);

        mSelectLocationList.setOnItemClickListener((adapterView, view, i, l) -> {
            if (l == -1) {
                return;
            }
            int postion = (int) l;
            mPresenter.locationListSelected(postion);
        });
        mSelectLocationMap.onCreate(savedInstanceState);// 此方法必须重写

        mPresenter.initData(getIntent().getStringExtra(Constants.INTENT_ACTIVITY_TYPE), mSelectLocationMap);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSelectLocationMap.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectLocationMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSelectLocationMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
        mSelectLocationMap.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == Constants.REQUEST_CODE_SEARCH_COMMUNITY) {
            PoiItem item = new Gson().fromJson(data.getStringExtra(Constants.INTENT_COMMUNITY_ITEM), PoiItem.class);
            mPresenter.doSearchQuery(new LatLonPoint(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude()));
            mPresenter.moveCamera(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());

        }
    }

    @OnClick({R.id.m_select_location_back_image, R.id.m_select_location_complete_image, R.id.m_select_location_search_edit})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_select_location_back_image:
                onBackPressed();
                break;
            case R.id.m_select_location_complete_image:
                mPresenter.addCommunity();// 先写死
                break;
            case R.id.m_select_location_search_edit:
                mPresenter.goSearchActivity();
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void setLocationListAdapter(SelectPosiAdapter adapter) {
        mSelectLocationList.setAdapter(adapter);
    }
}
