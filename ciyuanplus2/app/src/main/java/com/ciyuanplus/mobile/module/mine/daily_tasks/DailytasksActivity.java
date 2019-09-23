package com.ciyuanplus.mobile.module.mine.daily_tasks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.widget.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;

public class DailytasksActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailytasks);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
    }

    private void initView() {
        titleBar.setTitle("日常任务");
        titleBar.setOnBackListener(v -> onBackPressed());

        titleBar.registerRightImage(R.drawable.icon_list_share, v -> {

            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            }
           /* if (mPresenter.isMine) {
                showNewsOperaActivity(0);
            } else {
                showNewsOperaActivity(1);
            }*/
        });
    }

}
