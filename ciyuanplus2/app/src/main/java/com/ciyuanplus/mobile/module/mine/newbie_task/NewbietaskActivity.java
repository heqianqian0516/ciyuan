package com.ciyuanplus.mobile.module.mine.newbie_task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.mine.MyProfileActivity;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.release.ReleasePostActivity;
import com.ciyuanplus.mobile.module.settings.address.AddAddressActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.NewTaskBean;
import com.ciyuanplus.mobile.net.parameter.UserScoredApiParameter;
import com.ciyuanplus.mobile.net.response.NewTaskResponse;
import com.ciyuanplus.mobile.net.response.TaskNumberResponse;
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
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;

/*
 * 新手任务页面
 * */
public class NewbietaskActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.image_check_in)
    ImageView imageCheckIn;
    @BindView(R.id.tv_head_icon)
    TextView tvHeadIcon;
    @BindView(R.id.bt_head_icon)
    Button btHeadIcon;
    @BindView(R.id.rl_check_in)
    RelativeLayout rlCheckIn;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.bt_nick_name)
    Button btNickName;
    @BindView(R.id.rl_posts)
    RelativeLayout rlPosts;
    @BindView(R.id.tv_birthy)
    TextView tvBirthy;
    @BindView(R.id.bt_birthy)
    Button btBirthy;
    @BindView(R.id.rl_review)
    RelativeLayout rlReview;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.bt_address)
    Button btAddress;
    @BindView(R.id.rl_compleme)
    RelativeLayout rlCompleme;
    @BindView(R.id.tv_first_post)
    TextView tvFirstPost;
    @BindView(R.id.tv_postings)
    TextView tvPostings;
    @BindView(R.id.bt_postings)
    Button btPostings;
    @BindView(R.id.rl_mall)
    RelativeLayout rlMall;
    @BindView(R.id.bt_receive_awards)
    Button btReceiveAwards;
    @BindView(R.id.rl_reward)
    RelativeLayout rlReward;
    @BindView(R.id.tv_a1)
    TextView tvA1;
    @BindView(R.id.rl_daily)
    RelativeLayout rlDaily;
     ArrayList<NewTaskBean> newTaskBeanList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbietask);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
        newUsertask();
        queryNewTask();

    }



    private void initView() {
        titleBar.setTitle("新手任务");
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
    private void newUsertask() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.GET_NEWTASK_DATA_DETAILS + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
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
                NewTaskResponse response1 = new NewTaskResponse(s);
               if (response1.newTaskListBean.data!=null){
                    Collections.addAll(newTaskBeanList,response1.newTaskListBean.data);
                    if (response1.newTaskListBean.data.birthday!=null){
                        tvBirthy.setText("完成1/1");
                        btBirthy.setEnabled(false);
                        btBirthy.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        btBirthy.setText("已完成");
                        btBirthy.setTextColor(Color.GRAY);
                    }
                    if (response1.newTaskListBean.data.address!=null){
                        tvAddress.setText("完成1/1");
                        btAddress.setEnabled(false);
                        btAddress.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        btAddress.setText("已完成");
                        btAddress.setTextColor(Color.GRAY);
                    }
                    if (response1.newTaskListBean.data.postNum!=0){
                        tvPostings.setText("完成1/1");
                        btPostings.setEnabled(false);
                        btPostings.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        btPostings.setText("已完成");
                        btPostings.setTextColor(Color.GRAY);
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
    //查询任务完成情况
    private void queryNewTask() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.GET_USER_NEWTASK_STATUS + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
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
                UserScoredResponse response1 = new UserScoredResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else{
                   // CommonToast.getInstance(response1.mMsg).show();
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
    //完成任务领取奖励
    private void receiveAwards() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.NEW_TASK_INTEGRAL);
        postRequest.setMethod(HttpMethods.Post);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        postRequest.setHttpBody(new UserScoredApiParameter().getRequestBody());
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
                } else{
                     CommonToast.getInstance("hahhaha").show();
                    if (response1.data1!=null){
                        btReceiveAwards.setEnabled(false);
                        btReceiveAwards.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        btReceiveAwards.setText("已完成");
                        btReceiveAwards.setTextColor(Color.GRAY);
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

    @OnClick({R.id.bt_head_icon, R.id.bt_nick_name, R.id.bt_birthy, R.id.bt_address, R.id.bt_postings, R.id.bt_receive_awards})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_head_icon:
                Intent intent =new Intent(NewbietaskActivity.this, MyProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_nick_name:
                Intent intentTwo =new Intent(NewbietaskActivity.this, MyProfileActivity.class);
                startActivity(intentTwo);
                break;
            case R.id.bt_birthy:
                Intent intentThress =new Intent(NewbietaskActivity.this, MyProfileActivity.class);
                startActivity(intentThress);
                break;
            case R.id.bt_address:
                Intent intentFour =new Intent(NewbietaskActivity.this, AddAddressActivity.class);
                startActivity(intentFour);
                break;
            case R.id.bt_postings:
                Intent intentFive =new Intent(NewbietaskActivity.this, ReleasePostActivity.class);
                startActivity(intentFive);
                break;
            case R.id.bt_receive_awards:
                receiveAwards();
                break;
        }
    }
}
