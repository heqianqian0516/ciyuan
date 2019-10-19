package com.ciyuanplus.mobile.module.mine.daily_tasks;

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
import com.ciyuanplus.mobile.activity.MainActivityNew;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.home.HomeFragment;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.mine.sign_tasks.SignInActivity;
import com.ciyuanplus.mobile.module.release.ReleasePostActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.TaskNumberBean;
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

import static com.ciyuanplus.mobile.R.mipmap.daliy_completed;

/**
 * 日常任务页面
 */
public class DailytasksActivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    TitleBarView titleBar;
    @BindView(R.id.tv_exchange_gift)
    TextView tvExchangeGift;
    @BindView(R.id.tv_ciyuan_num)
    TextView tvCiyuanNum;
    @BindView(R.id.image_check_in)
    ImageView imageCheckIn;
    @BindView(R.id.rl_check_in)
    RelativeLayout rlCheckIn;
    @BindView(R.id.rl_posts)
    RelativeLayout rlPosts;
    @BindView(R.id.rl_review)
    RelativeLayout rlReview;
    @BindView(R.id.rl_compleme)
    RelativeLayout rlCompleme;
    @BindView(R.id.rl_mall)
    RelativeLayout rlMall;
    @BindView(R.id.rl_daily)
    RelativeLayout rlDaily;
    @BindView(R.id.tv_a1)
    TextView tvA1;
    @BindView(R.id.tv_check_in)
    TextView tvCheckIn;
    @BindView(R.id.bu_check_in)
    Button buCheckIn;
    @BindView(R.id.tv_posting)
    TextView tvPosting;
    @BindView(R.id.bu_posting)
    Button buPosting;
    @BindView(R.id.tv_daily_review)
    TextView tvDailyReview;
    @BindView(R.id.bu_daily_review)
    Button buDailyReview;
    @BindView(R.id.tv_daily_compliments)
    TextView tvDailyCompliments;
    @BindView(R.id.bu_daily_compliments)
    Button buDailyCompliments;
    @BindView(R.id.tv_daily_mall)
    TextView tvDailyMall;
    @BindView(R.id.bu_daily_mall)
    Button buDailyMall;
    @BindView(R.id.tv_daily_sharing)
    TextView tvDailySharing;
    @BindView(R.id.bu_daily_sharing)
    Button buDailySharing;
    ArrayList<TaskNumberBean> taskNumberBeanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailytasks);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        initView();
        usersscored();
        taskNumber();
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
   //用户今日获得积分
    private void usersscored() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.GET_USER_INTEGRALBY_TODAY + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
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
                    tvCiyuanNum.setText("今日获得" + response1.data1 + "次元币");
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
    //用户完成任务次数
    private void taskNumber() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.GET_USER_TODAY_TASKNUM + "?userUuid=" + UserInfoData.getInstance().getUserInfoItem().uuid);
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
                TaskNumberResponse response1 = new TaskNumberResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.taskNumberListBean.data!=null){
                    Collections.addAll(taskNumberBeanList,response1.taskNumberListBean.data);
                    if(response1.taskNumberListBean.data.cpostNum<3){
                        tvPosting.setText("次元币+5  完成"+ response1.taskNumberListBean.data.cpostNum + "/3");
                    }else{
                        tvPosting.setText("次元币+5  完成3/3");
                        buPosting.setEnabled(false);
                        buPosting.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        buPosting.setText("已完成");
                        buPosting.setTextColor(Color.GRAY);
                    }
                     if (response1.taskNumberListBean.data.signNum<1){
                         tvCheckIn.setText("次元币+5  完成"+ response1.taskNumberListBean.data.signNum + "/1");
                     }else{
                         tvCheckIn.setText("次元币+5  完成1/1");
                         buCheckIn.setEnabled(false);
                         buCheckIn.setBackgroundResource(R.drawable.daily_button_bg_complant);
                         buCheckIn.setText("已完成");
                         buCheckIn.setTextColor(Color.GRAY);
                     }
                     if(response1.taskNumberListBean.data.commentNum<5){
                         tvDailyReview.setText("次元币+2  完成"+ response1.taskNumberListBean.data.commentNum + "/5");
                     }else{
                         tvDailyReview.setText("次元币+2  完成5/5");
                         buDailyReview.setEnabled(false);
                         buDailyReview.setBackgroundResource(R.drawable.daily_button_bg_complant);
                         buDailyReview.setText("已完成");
                         buDailyReview.setTextColor(Color.GRAY);
                     }

                    if (response1.taskNumberListBean.data.likeNum<10){
                        tvDailyCompliments.setText("次元币+1  完成"+ response1.taskNumberListBean.data.likeNum+ "/10");
                    }else{
                        tvDailyCompliments.setText("次元币+1  完成10/10");
                        buDailyCompliments.setEnabled(false);
                        buDailyCompliments.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        buDailyCompliments.setText("已完成");
                        buDailyCompliments.setTextColor(Color.GRAY);
                    }
                    if (response1.taskNumberListBean.data.shopNum<5){
                        tvDailyMall.setText("次元币+2  完成"+ response1.taskNumberListBean.data.shopNum+ "/5");
                    }else{
                        tvDailyMall.setText("次元币+2  完成5/5");
                        buDailyMall.setEnabled(false);
                        buDailyMall.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        buDailyMall.setText("已完成");
                        buDailyMall.setTextColor(Color.GRAY);
                    }
                    if (response1.taskNumberListBean.data.shareNum<1){
                        tvDailySharing.setText("次元币+5  完成"+ response1.taskNumberListBean.data.shareNum+ "/1");
                    }else{
                        tvDailySharing.setText("次元币+5  完成1/1");
                        buDailySharing.setEnabled(false);
                        buDailySharing.setBackgroundResource(R.drawable.daily_button_bg_complant);
                        buDailySharing.setText("已完成");
                        buDailySharing.setTextColor(Color.GRAY);
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
    @OnClick({R.id.tv_exchange_gift, R.id.bu_check_in, R.id.bu_posting, R.id.bu_daily_review, R.id.bu_daily_compliments, R.id.bu_daily_mall, R.id.bu_daily_sharing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exchange_gift:
                break;
            case R.id.bu_check_in:
                Intent intentFirst=new Intent(DailytasksActivity.this, SignInActivity.class);
                startActivity(intentFirst);
                break;
            case R.id.bu_posting:
                Intent intent=new Intent(DailytasksActivity.this, ReleasePostActivity.class);
                startActivity(intent);
                break;
            case R.id.bu_daily_review:
                Intent intent1=new Intent(DailytasksActivity.this, MainActivityNew.class);
                intent1.putExtra(Constants.INTENT_ACTIVITY_TYPE,0);
                startActivity(intent1);
                break;
            case R.id.bu_daily_compliments:
                Intent intent2=new Intent(DailytasksActivity.this, MainActivityNew.class);
                intent2.putExtra(Constants.INTENT_ACTIVITY_TYPE,0);
                startActivity(intent2);
                break;
            case R.id.bu_daily_mall:
                Intent intent4=new Intent(DailytasksActivity.this, MainActivityNew.class);
                intent4.putExtra(Constants.INTENT_ACTIVITY_TYPE,1);
                startActivity(intent4);
                break;
            case R.id.bu_daily_sharing:
                Intent intent5=new Intent(DailytasksActivity.this, MainActivityNew.class);
                intent5.putExtra(Constants.INTENT_ACTIVITY_TYPE,0);
                startActivity(intent5);
                break;
        }
    }
}
