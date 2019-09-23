package com.ciyuanplus.mobile.module.mine.exchange_mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.ExchangeMallAdapter;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.widget.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;

public class ExchangeActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.ll_consumption_record)
    LinearLayout llConsumptionRecord;
    @BindView(R.id.rl_mall_view)
    RecyclerView rlMallView;
    private GridLayoutManager layoutManager;
    private ExchangeMallAdapter exchangeMallAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
    }

    private void initView() {
        titleBar.setTitle("次元币商城");
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
        llConsumptionRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeActivity.this, ConsumptionRecordActivity.class);
                startActivity(intent);
            }
        });
       //纵向线性布局
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        rlMallView.setLayoutManager(layoutManager);
        exchangeMallAdapter = new ExchangeMallAdapter();
        rlMallView.setAdapter(exchangeMallAdapter);
        exchangeMallAdapter.setClickCallBack(new ExchangeMallAdapter.ClickCallBack() {
            @Override
            public void callBack() {
                Intent intent=new Intent(ExchangeActivity.this,ExchangeMallDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
