package com.ciyuanplus.mobile.module.mine.exchange_mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.ExchangeMallAdapter;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.TotalScoreBean;
import com.ciyuanplus.mobile.net.response.NewTaskResponse;
import com.ciyuanplus.mobile.net.response.TotalScoreResponse;
import com.ciyuanplus.mobile.net.response.UserScoredResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;

/**
 * 次元币商城页面
 */
public class ExchangeActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.ll_consumption_record)
    LinearLayout llConsumptionRecord;
    @BindView(R.id.rl_mall_view)
    RecyclerView rlMallView;
    @BindView(R.id.tv_total_score)
    TextView tvTotalScore;
    private GridLayoutManager layoutManager;
    private ExchangeMallAdapter exchangeMallAdapter;
    private ArrayList<TotalScoreBean> totalScoreBeanList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
        totalScore();
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
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rlMallView.setLayoutManager(layoutManager);
        exchangeMallAdapter = new ExchangeMallAdapter();
        rlMallView.setAdapter(exchangeMallAdapter);
        exchangeMallAdapter.setClickCallBack(new ExchangeMallAdapter.ClickCallBack() {
            @Override
            public void callBack() {
                Intent intent = new Intent(ExchangeActivity.this, ExchangeMallDetailActivity.class);
                startActivity(intent);
            }
        });
    }
    private void totalScore() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.GET_INTEGRAL_DETAILS + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
        postRequest.setMethod(HttpMethods.Get);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        postRequest.addHeader("authToken", sessionKey);
        Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("ttt", s + "______" + response);
                TotalScoreResponse response1 = new TotalScoreResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.totalScoreListBean.data!=null){
                    Collections.addAll(totalScoreBeanList,response1.totalScoreListBean.data);
                    tvTotalScore.setText(response1.totalScoreListBean.data.userIntegral+"");

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


