package com.ciyuanplus.mobile.module.mine.sign_tasks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.UserSignDataBean;
import com.ciyuanplus.mobile.net.bean.UserSignDataListBean;
import com.ciyuanplus.mobile.net.parameter.SignedSupplementApiParameter;
import com.ciyuanplus.mobile.net.parameter.UserScoredApiParameter;
import com.ciyuanplus.mobile.net.response.UserScoredResponse;
import com.ciyuanplus.mobile.net.response.UserSignDateResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.ciyuanplus.mobile.zwcalendar.ZWCalendarView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;

/**
 * 每日签到页面
 */
public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.tv_calendar_show)
    TextView tvCalendarShow;
    @BindView(R.id.calendarView)
    ZWCalendarView calendarView;
    @BindView(R.id.immediately_sign_in)
    ImageView immediatelySignIn;
    @BindView(R.id.tv_supplementary)
    TextView tvSupplementary;
    @BindView(R.id.tv_check_days)
    TextView tvCheckDays;
    private String selectDate;
    private int selectDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();

        selectUserSignDate();
        selectUserRetroactiveNum();
        selectUserContinuousSign();
        //signedSupplement();
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
        calendarView.setSelectListener(new ZWCalendarView.SelectListener() {
            @Override
            public void change(int year, int month) {
                tvCalendarShow.setText(String.format("%s 年 %s 月", year, month));
            }

            @Override
            public void select(int year, int month, int day, int week) {
               /* Toast.makeText(getApplicationContext(),
                        String.format("%s 年 %s 月 %s日，周%s", year, month, day, week),
                        Toast.LENGTH_SHORT).show();*/
                //拼接成 2019-10-21 的格式
                selectDate = year+"-"+month+"-"+(day<10?"0"+day : day);
                //定义一个变量接收选中的某天
                selectDay = day;

            }
        });
        immediatelySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSign();
                //按钮点击事件里
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                String isCurrentDate = currentYear+"-"+currentMonth+"-"+(currentDay<10?"0"+currentDay:currentDay);
                //如果相等说明是签到今天的
                if (isCurrentDate.equals(selectDate)){
                    //调用签到接口
                    userSign();
                }else{
                    //如果不是则判断是之前天数还是之后天数
                    if (selectDay<currentDay){
                        //调用补签接口
                        signedSupplement();
                    }else{
                        //提示用户不能签到今天之后的日期
                    }
                }
            }
        });
    }

    //用户签到
    private void userSign() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.USER_SIGN);
        postRequest.setMethod(HttpMethods.Post);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        postRequest.setHttpBody(new UserScoredApiParameter().getRequestBody());
        postRequest.addHeader("authToken", sessionKey);
        // Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("bbb", s + "______" + response);
                //t.setText(s+"________"+response);
                UserScoredResponse response1 = new UserScoredResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
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

    //查询今天是否签到
    private void isSign() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.GET_IS_SIGN + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
        postRequest.setMethod(HttpMethods.Get);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        //  postRequest.setHttpBody(new UserScoredApiParameter().getRequestBody());
        postRequest.addHeader("authToken", sessionKey);
        Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("ttt", s + "______" + response);
                UserScoredResponse response1 = new UserScoredResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    if (Integer.parseInt(response1.mCode) == 1) {
                        userSign();
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

    //查询用户签到日期
    private void selectUserSignDate() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.SELECT_USER_SIGN_DATE + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
        postRequest.setMethod(HttpMethods.Get);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        //  postRequest.setHttpBody(new UserScoredApiParameter().getRequestBody());
        postRequest.addHeader("authToken", sessionKey);
        Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("ttt", s + "______" + response);
                //UserSignDateResponse
                UserSignDateResponse response1 = new UserSignDateResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    calendarView.setSignRecords(response1.userSignDataBean.getDataX());
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

    //查询本月补签次数
    private void selectUserRetroactiveNum() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.SELECT_USER_RETROACTIVE_NUM + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
        postRequest.setMethod(HttpMethods.Get);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        //  postRequest.setHttpBody(new UserScoredApiParameter().getRequestBody());
        postRequest.addHeader("authToken", sessionKey);
        Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("ttt", s + "______" + response);
                UserScoredResponse response1 = new UserScoredResponse(s);
                    if (Integer.parseInt(response1.data1)<3){
                        tvSupplementary.setText("补签次数" + response1.data1 + "/3");
                    }else{
                        tvSupplementary.setText("补签次数0/3");
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
    //查询用户连续签到天数
    private void selectUserContinuousSign() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.SELECT_USER_CONTINUOUS_SIGN + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
        postRequest.setMethod(HttpMethods.Get);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        //  postRequest.setHttpBody(new UserScoredApiParameter().getRequestBody());
        postRequest.addHeader("authToken", sessionKey);
        Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("ttt", s + "______" + response);
                UserScoredResponse response1 = new UserScoredResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    tvCheckDays.setText("连续签到" + response1.data1 + "天啦~");
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
    //补签
    private void signedSupplement() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.SIGNED_SUPPLEMENT);
        postRequest.setMethod(HttpMethods.Post);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
          postRequest.setHttpBody(new SignedSupplementApiParameter(selectDate).getRequestBody());
        postRequest.addHeader("authToken", sessionKey);
        Log.i("ttt", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                Log.i("ttt", s + "______" + response);
                UserScoredResponse response1 = new UserScoredResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance(response1.mMsg).show();
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
