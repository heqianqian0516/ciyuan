package com.ciyuanplus.mobile.module.mine.my_order_detail;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.SquareImageView;
import com.ciyuanplus.mobile.widget.TitleBarView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyOrderDetailActivity extends MyBaseActivity implements MyOrderDetailContract.View {

    public static final String ORDER_UUID = "orderUUID";
    @BindView(R.id.title_bar)
    TitleBarView m_js_common_title;
    @BindView(R.id.ll_order_cancel)
    LinearLayout mCancelLayout;
    @BindView(R.id.ll_order_status)
    LinearLayout mOrderStatusLayout;
    @BindView(R.id.tv_delivery_label)
    TextView mDeliveryLabel;
    @BindView(R.id.tv_delivery_method)
    TextView mDeliveryMethod;
    @BindView(R.id.tv_delivery_logistics)
    TextView mDeliveryLogistitcs; //物流
    @BindView(R.id.et_logistics_info)
    EditText mEditLogistticsInfo;
    @BindView(R.id.tv_notice)
    TextView mNoiteText;
    @BindView(R.id.iv_location)
    ImageView mLocationImage;
    @BindView(R.id.tv_buyer)
    TextView mBuyer;
    @BindView(R.id.tv_buyer_phone_num)
    TextView mBuyerPhone;
    @BindView(R.id.tv_buyer_address)
    TextView mBuyerAddress;
    @BindView(R.id.siv_order_image)
    SquareImageView mProductImage;
    @BindView(R.id.tv_price)
    TextView mProductPrice;
    @BindView(R.id.tv_title)
    TextView mProductTitle;
    @BindView(R.id.tv_buyCount)
    TextView mBuyCount;
    @BindView(R.id.tv_order_price)
    TextView mOrderPrice;
    @BindView(R.id.tv_delivery_method_bottom)
    TextView mDeliveryMethodBottom;
    @BindView(R.id.tv_order_id)
    TextView mOrderID;
    @BindView(R.id.tv_order_time)
    TextView mOrderTime;
    @BindView(R.id.btn_cancel_order)
    ImageView mCancelOrderButton;
    @BindView(R.id.scrollView_container)
    ScrollView mContainer;
    @BindView(R.id.btn_orders)
    ImageView mOrderButton;

    @Inject

    MyOrderDetailPresenter myOrderDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_detail);

        initView();
//        myOrderDetailPresenter.initData(getIntent());

    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerMyOrderDetailPresenterComponent.builder().myOrderDetailPresenterModule(new MyOrderDetailPresenterModule(this)).build().inject(this);
        m_js_common_title.setTitle("订单详情");
        m_js_common_title.setOnBackListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });

    }

    @OnClick({R.id.btn_orders, R.id.btn_cancel_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_orders:
                myOrderDetailPresenter.showAccptDialog();
                break;
            case R.id.btn_cancel_order:
                myOrderDetailPresenter.showCancelDialog();
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void updatePage(boolean show) {

        mContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setBuyer(String buyer) {
        mBuyer.setText(buyer);
    }

    @Override
    public void setBuyerPhone(String phone) {
        mBuyerPhone.setText(phone);
    }

    @Override
    public void setBuyerAddress(String address) {
        mBuyerAddress.setText(address);
    }

    @Override
    public void setProductImage(String imageUrl) {
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + imageUrl)
                .apply(new RequestOptions().dontAnimate()).into(mProductImage);
    }

    @Override
    public void setProductPrice(String productPrice) {
        mProductPrice.setText(productPrice);
    }

    @Override
    public void setProductTitle(String productTitle) {
        mProductTitle.setText(productTitle);
    }

    @Override
    public void setBuyCount(String buyCount) {
        mBuyCount.setText(buyCount);
    }

    @Override
    public void setOrderPrice(String orderPrice) {
        mOrderPrice.setText(orderPrice);
    }

    @Override
    public void setDeliveryMethod(String deliveryMethod) {
        mDeliveryMethod.setText(deliveryMethod);
    }

    @Override
    public void setDeliveryMethodBottom(String deliveryMethodBottom) {
        mDeliveryMethodBottom.setText(deliveryMethodBottom);
    }

    @Override
    public void setOrderID(String orderID) {

        mOrderID.setText(orderID);
    }

    @Override
    public void setOrderTime(String orderTime) {
        mOrderTime.setText(orderTime);
    }

    @Override
    public void showCancelOrderLayout(boolean show) {
        mCancelLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showOrderStatusLayout(boolean show) {

        mOrderStatusLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showCancelOrderButton(boolean show) {
        mCancelOrderButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showOrderButton(boolean show) {
        mOrderButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showmEditLogistticsInfo(boolean show) {
        mEditLogistticsInfo.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoticeText(boolean show) {
        mNoiteText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showmDeliveryLogistitcs(boolean show) {

        mDeliveryLogistitcs.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setDeliveryLogistitcs(String deliveryLogistitcs) {
        mDeliveryLogistitcs.setText(deliveryLogistitcs);
    }

    @Override
    public EditText getLogisticsInfoView() {
        return mEditLogistticsInfo;
    }

    @Override
    public String getLogistticsInfo() {
        return mEditLogistticsInfo.getText().toString().trim();
    }

    @Override
    public void setNoticeText(String notice) {
        mNoiteText.setText(notice);
    }
}
