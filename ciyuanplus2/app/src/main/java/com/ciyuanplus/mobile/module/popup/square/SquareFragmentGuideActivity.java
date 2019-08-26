package com.ciyuanplus.mobile.module.popup.square;

import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SquareFragmentGuideActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_fragment_guide);

        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        initView();

        // v1.0.1 添加该行代码， 只有一个新手引导页面，所以这里需要设置已预览
        SharedPreferencesManager.putBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_MINE_CENTER_HAS_GUIDE, true);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }

    @OnClick(R.id.m_mine_center_guide_ok)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_mine_center_guide_ok) {
            finish();
        }
    }
}
