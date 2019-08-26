package com.ciyuanplus.mobile.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.forum_detail.forum_detail.ForumDetailPresenter;
import com.ciyuanplus.mobile.module.release.ReleasePostActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.ParticipationItemList;
import com.ciyuanplus.mobile.net.parameter.RequestParticipationDetailApiParameter;
import com.ciyuanplus.mobile.net.response.RequestParticipationDetailResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.BigView;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * 参加活动详情页面
 * */
public class ParticipationDetailsActivity extends MyBaseActivity implements EventCenterManager.OnHandleEventListener {


    @BindView(R.id.participate_activity)
    Button participateActivity;
   /* @BindView(R.id.activity_img)
    BigView activityImg;
    @BindView(R.id.activity_imgtwo)
    BigView activityImgTwo;*/
    @BindView(R.id.rank_back)
    ImageView rankBack;
    @BindView(R.id.rank_share)
    ImageView rankShare;
    @BindView(R.id.lin_layout)
    LinearLayout linLayout;
    private String activityUuid;
    private LinearLayoutManager linearLayoutManager;
    private final ArrayList<ParticipationItemList> participationItemLists = new ArrayList<>();
    InputStream is = null;
    @Inject
    public ForumDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_details);
        ButterKnife.bind(this);

      /*  UltimateBar.Companion.with(this)
                .statusDark(false)                  // 状态栏灰色模式(Android 6.0+)，默认 flase
                .applyNavigation(true)              // 应用到导航栏，默认 flase
                .navigationDark(false)              // 导航栏灰色模式(Android 8.0+)，默认 false
                .create()
                .immersionBar();*/
        Intent intent = getIntent();
        activityUuid = intent.getStringExtra("activityUuid");
        initView();
        participateActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParticipationDetailsActivity.this, ReleasePostActivity.class);
                intent.putExtra("activityUuid", activityUuid);
                intent.putExtra(Constants.INTENT_OPEN_TYPE, 1);
                startActivity(intent);
            }
        });

        rankBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rankShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshNewItem freshNewItem = new FreshNewItem();
                freshNewItem.bizType = 2;
                freshNewItem.title = "次元PLUS";
                freshNewItem.imgs = "a.img,b.img";
                Intent intent = new Intent(ParticipationDetailsActivity.this, ShareNewsPopupActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, freshNewItem);
                startActivity(intent);

            }
        });
    }


    private void initView() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_EVENT_DETAILS);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestParticipationDetailApiParameter(activityUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestParticipationDetailResponse response1 = new RequestParticipationDetailResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.participationItembean.data != null) {
                    Collections.addAll(participationItemLists, response1.participationItembean.data);
                    ParticipationItemList item = participationItemLists.get(0);
                    String[] img = item.contentImg.split("\\,");
                    /*Log.d("aaaaaaa"," "+img[0]);
                    Log.d("bbbbb"," "+img[1]);*/
                    //下载图片保存到本地2

                    for (int i = 0; i < img.length; i++) {
                        Glide.with(getApplicationContext()).load(Constants.IMAGE_LOAD_HEADER + img[i]).downloadOnly(new SimpleTarget<File>() {

                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                BigView bgVIew =  new BigView(ParticipationDetailsActivity.this);
                                bgVIew.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(1.0F, new PointF(0, 0), 0));
                                linLayout.addView(bgVIew);
                            }
                        });
                    }
                   /* Glide.with(getApplicationContext()).load(Constants.IMAGE_LOAD_HEADER + img[0]).downloadOnly(new SimpleTarget<File>() {

                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            activityImg.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(1.0F, new PointF(0, 0), 0));
                        }
                    });*/
                   /* Glide.with(getApplicationContext()).load(Constants.IMAGE_LOAD_HEADER + img[1]).downloadOnly(new SimpleTarget<File>() {

                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            activityImgTwo.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(1.0F, new PointF(0, 0), 0));
                        }
                    });*/
                }

            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getStri0ng(R.string.string_my_profile_get_help_fail_alert)).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
