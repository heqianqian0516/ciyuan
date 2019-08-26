package com.ciyuanplus.mobile.module.start_forum.add_position;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.MapView;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.widget.CommonTitleBar;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/2/5.
 *
 * // 先放一下  方案未定
 */

public class AddPositionActivity extends MyBaseActivity implements AddPositionContract.View{
    @BindView(R.id.m_add_position_common_title)
    CommonTitleBar m_js_common_title;
    @BindView(R.id.m_add_position_map)
    MapView mAddPositionMap;
    @BindView(R.id.m_add_position_back_image)
    ImageView mAddPositionBackImage;
    @BindView(R.id.m_add_position_complete_image)
    ImageView mAddPositionCompleteImage;
    @BindView(R.id.m_add_position_step_1_lp)
    RelativeLayout mAddPositionStep1Lp;
    @BindView(R.id.m_add_position_name_edit)
    EditText mAddPositionNameEdit;
    @BindView(R.id.m_add_position_zone_text)
    TextView mAddPositionZoneText;
    @BindView(R.id.m_add_position_detail_edit)
    EditText mAddPositionDetailEdit;
    @BindView(R.id.m_add_position_step_2_lp)
    LinearLayout mAddPositionStep2Lp;

    @Inject
 AddPositionPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_position);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title));
        this.initView();

        mAddPositionMap.onCreate(savedInstanceState);

        mPresenter.initData(getIntent());
    }

    private void initView() {
        //
        Unbinder unbinder = ButterKnife.bind(this);

        m_js_common_title.setCenterText("创建位置");
        m_js_common_title.setRightImage(R.mipmap.nav_icon_save);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                // 完成  提交数据并且 通过 setResult 返回结果
            }
        });
    }

    @Override
    public MapView getMapView() {
        return mAddPositionMap;
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mAddPositionMap.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddPositionMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAddPositionMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null)mPresenter.detachView();
        mAddPositionMap.onDestroy();
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
