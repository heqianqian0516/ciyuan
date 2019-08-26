package com.ciyuanplus.mobile.module.popup.start_post_guide;

import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/7/20.
 * <p>
 * 新手引导  - 发布长文引导页面
 */

public class StartPostGuideActivity extends MyBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_post_guide);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();

        // v1.0.1 添加该行代码， 只有一个新手引导页面，所以这里需要设置已预览
        SharedPreferencesManager.putBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_START_POST_HAS_GUIDE, true);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
    }


    @Override
    public void onBackPressed() {
        //禁用返回键
    }

    @OnClick(R.id.m_start_post_guide_ok)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_start_post_guide_ok) {
            finish();
        }
    }
}
