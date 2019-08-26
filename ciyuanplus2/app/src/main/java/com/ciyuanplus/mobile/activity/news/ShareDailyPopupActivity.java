package com.ciyuanplus.mobile.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.DailyLabelInfo;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Alen on 2017/10/31.
 */

public class ShareDailyPopupActivity extends MyBaseActivity implements UMShareListener {

    private DailyLabelInfo mDailyLabelInfo;
    private String mFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_share_popup);

        mDailyLabelInfo = (DailyLabelInfo) getIntent().getSerializableExtra(Constants.INTENT_DAILY_LABEL);
        mFilePath = getIntent().getStringExtra(Constants.INTENT_FILE_PATH);
        this.initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        this.runOnUiThread(() -> CommonToast.getInstance(getResources().getString(R.string.string_share_success_alert), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        this.runOnUiThread(() -> CommonToast.getInstance(getResources().getString(R.string.string_share_fail_alert), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        this.runOnUiThread(() -> CommonToast.getInstance(getResources().getString(R.string.string_share_cancel_alert), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.m_share_pop_up_wei_circle, R.id.m_share_pop_up_wei_chat,
            R.id.m_share_pop_up_qq, R.id.m_share_pop_up_weibo, R.id.m_share_pop_up_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        File file = new File(mFilePath);
        if (!file.exists()) return;
        UMImage image = new UMImage(this, file);

        switch (view.getId()) {
            case R.id.m_share_pop_up_wei_circle:
                new ShareAction(ShareDailyPopupActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withText(mDailyLabelInfo.content)
                        .withMedia(image)
                        .setCallback(ShareDailyPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_wei_chat:
                new ShareAction(ShareDailyPopupActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withText(mDailyLabelInfo.content)
                        .withMedia(image)
                        .setCallback(ShareDailyPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_qq:
                new ShareAction(ShareDailyPopupActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withText(mDailyLabelInfo.content)
                        .withMedia(image)
                        .setCallback(ShareDailyPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_weibo:
                new ShareAction(ShareDailyPopupActivity.this).setPlatform(SHARE_MEDIA.SINA)
                        .withText(mDailyLabelInfo.content)
                        .withMedia(image)
                        .setCallback(ShareDailyPopupActivity.this)
                        .share();
                break;
            case R.id.m_share_pop_up_cancel:
                ShareDailyPopupActivity.this.finish();
                break;
        }
    }
}
