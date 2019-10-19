package com.ciyuanplus.mobile.module.mine.sign_tasks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.ciyuanplus.mobile.zwcalendar.ZWSignCalendarView;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;
/**
 * 每日签到页面
 * */
public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.tv_calendar_show)
    TextView tvCalendarShow;
    @BindView(R.id.calendarView)
    ZWSignCalendarView calendarView;
    @BindView(R.id.immediately_sign_in)
    ImageView immediatelySignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
    }

    private void initView() {
        titleBar.setTitle("每日签到");
        titleBar.setOnBackListener(v -> onBackPressed());

        titleBar.registerRightImage(R.drawable.icon_list_share, v -> {

            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            }
        });
        calendarView.setDateListener(new ZWSignCalendarView.DateListener() {
            @Override
            public void change(int year, int month) {
                tvCalendarShow.setText(String.format("%s 年 %s 月", year, month));
            }
        });
        tvCalendarShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.selectMonth(2017, 9);
            }
        });
        HashSet<String> sign2 = new HashSet<>();
        sign2.add("2019-9-09");
        calendarView.setSignRecords(sign2);
    }
}
