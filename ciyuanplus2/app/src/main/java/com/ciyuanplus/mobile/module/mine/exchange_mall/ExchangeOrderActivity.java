package com.ciyuanplus.mobile.module.mine.exchange_mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.widget.SquareImageView;
import com.ciyuanplus.mobile.widget.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;

/***
 * 兑换商品详情页面
 * **/
public class ExchangeOrderActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.ll_order_cancel)
    LinearLayout llOrderCancel;
    @BindView(R.id.iv_delivery)
    ImageView ivDelivery;
    @BindView(R.id.tv_delivery_label)
    TextView tvDeliveryLabel;
    @BindView(R.id.tv_delivery_logistics)
    TextView tvDeliveryLogistics;
    @BindView(R.id.et_logistics_info)
    EditText etLogisticsInfo;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.ll_order_status)
    LinearLayout llOrderStatus;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_buyer_phone_num)
    TextView tvBuyerPhoneNum;
    @BindView(R.id.tv_buyer)
    TextView tvBuyer;
    @BindView(R.id.tv_buyer_address)
    TextView tvBuyerAddress;
    @BindView(R.id.siv_order_image)
    SquareImageView sivOrderImage;
    @BindView(R.id.minusButton)
    ImageView minusButton;
    @BindView(R.id.prdCount)
    TextView prdCount;
    @BindView(R.id.addButton)
    ImageView addButton;
    @BindView(R.id.tv_status)
    RelativeLayout tvStatus;
    @BindView(R.id.image_to)
    ImageView imageTo;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_specification)
    TextView tvSpecification;
    @BindView(R.id.scrollView_container)
    ScrollView scrollViewContainer;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.image_ciyuanbi)
    ImageView imageCiyuanbi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
    }

    private void initView() {
        titleBar.setTitle("详情");
        titleBar.setOnBackListener(v -> onBackPressed());
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        intent.getIntExtra("prodId", 0);
        tvTitle.setText(title);
    }
}
