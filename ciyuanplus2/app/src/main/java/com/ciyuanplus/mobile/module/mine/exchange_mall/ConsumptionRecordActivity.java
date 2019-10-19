package com.ciyuanplus.mobile.module.mine.exchange_mall;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.ConsumptionRecordAdapter;
import com.ciyuanplus.mobile.widget.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;
/**
 *次元币消费记录页面
 * */
public class ConsumptionRecordActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.rl_consumption)
    RecyclerView rlConsumption;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_record);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        intiView();
    }

    private void intiView() {
        titleBar.setTitle("次元币消费记录");
        titleBar.setOnBackListener(v -> onBackPressed());

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rlConsumption.setLayoutManager(linearLayoutManager);
        ConsumptionRecordAdapter consumptionRecordAdapter=new ConsumptionRecordAdapter();
        rlConsumption.setAdapter(consumptionRecordAdapter);
    }
}
