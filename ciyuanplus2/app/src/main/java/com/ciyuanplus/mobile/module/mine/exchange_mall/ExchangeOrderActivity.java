package com.ciyuanplus.mobile.module.mine.exchange_mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.ShopProdDetailApiParameter;
import com.ciyuanplus.mobile.net.parameter.UserScoredApiParameter;
import com.ciyuanplus.mobile.net.response.ShopProdDetailItemRes;
import com.ciyuanplus.mobile.net.response.UserScoredResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.SquareImageView;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.kris.baselibrary.util.NumberUtil;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;

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
    private int prodId;
     ArrayList<ShopProdDetailItemRes.CommodityItem> mList =new ArrayList<>();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
        initSpecifications();
    }

    private void initView() {
        titleBar.setTitle("详情");
        titleBar.setOnBackListener(v -> onBackPressed());
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        prodId = intent.getIntExtra("prodId", 0);
        int price = intent.getIntExtra("price", 0);
        name = intent.getStringExtra("name");
        tvTitle.setText(title);
        tvPrice.setText("￥"+ NumberUtil.getAmountValue(price / 100));
       // tvSpecification.setText("规格:"+name);
    }
    private void initSpecifications() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.SELECT_PROD_DETAIL_BYPRODID);
        postRequest.setMethod(HttpMethods.Post);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
          postRequest.setHttpBody(new ShopProdDetailApiParameter(prodId+"").getRequestBody());
        postRequest.addHeader("authToken", sessionKey);
        Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("ttt", s + "______" + response);
                ShopProdDetailItemRes response1 = new ShopProdDetailItemRes(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                   if (response1.communityListItem.getSpecList()!=null){
                       mList.addAll(response1.communityListItem.getSpecList());
                       String[] split = response1.communityListItem.getImg().split(",");
                       RequestOptions options =new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail).dontAnimate().centerCrop();
                       Glide.with(getApplicationContext()).load(Constants.IMAGE_LOAD_HEADER + split[0]).apply(options).into(sivOrderImage);
                       tvSpecification.setText("规格："+response1.communityListItem.getSpecList().get(0).getName());
                   }

                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance("操作失败").show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
