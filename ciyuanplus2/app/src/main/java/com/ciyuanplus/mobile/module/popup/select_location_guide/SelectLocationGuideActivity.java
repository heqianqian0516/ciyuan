package com.ciyuanplus.mobile.module.popup.select_location_guide;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/7/20.
 * <p>
 * 新手引导  - 选择地址、小区
 */

public class SelectLocationGuideActivity extends MyBaseActivity {
    @BindView(R.id.m_select_location_guide_1)
    ImageView mSelectLocationGuide1;
    @BindView(R.id.m_select_location_guide_2)
    ImageView mSelectLocationGuide2;
    @BindView(R.id.m_select_location_guide_ok)
    ImageView mSelectLocationGuideOk;
    @BindView(R.id.m_select_location_guide_lp)
    RelativeLayout mSelectLocationGuideLp;
    private int step = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_location_guide);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();

        // v1.0.1 添加该行代码， 只有一个新手引导页面，所以这里需要设置已预览
        SharedPreferencesManager.putBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_HAS_GUIDE, true);

    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        //goNext();
    }

    private void goNext() {
        step++;
        switch (step) {
            case 1:
                mSelectLocationGuide1.setVisibility(View.VISIBLE);
                mSelectLocationGuide2.setVisibility(View.GONE);
                break;
            case 2:
                mSelectLocationGuide1.setVisibility(View.GONE);
                mSelectLocationGuide2.setVisibility(View.VISIBLE);
                break;
            case 3:
                mSelectLocationGuide1.setVisibility(View.GONE);
                mSelectLocationGuide2.setVisibility(View.GONE);
                mSelectLocationGuideOk.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }

    @OnClick({R.id.m_select_location_guide_ok, R.id.m_select_location_guide_lp})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_select_location_guide_ok:
                finish();
                break;
            case R.id.m_select_location_guide_lp:
                finish();
                break;
        }
    }
}
