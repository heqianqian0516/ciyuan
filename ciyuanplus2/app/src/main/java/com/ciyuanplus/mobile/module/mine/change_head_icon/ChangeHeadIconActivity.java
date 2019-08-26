package com.ciyuanplus.mobile.module.mine.change_head_icon;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.ciyuanplus.mobile.widget.MyZoomImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alen on 2017/8/31.
 * <p>
 * 选择头像的页面
 */

public class ChangeHeadIconActivity extends MyBaseActivity implements ChangeHeadIconContract.View, EventCenterManager.OnHandleEventListener {

    @BindView(R.id.m_select_squre_image_back_image)
    ImageView mSelectSqureImageBackImage;
    @BindView(R.id.m_select_squre_image_top_lp)
    RelativeLayout mSelectSqureImageTopLp;
    @BindView(R.id.m_select_squre_head_change_btn)
    Button mSelectSqureHeadChangeBtn;
    @BindView(R.id.m_select_squre_head_image)
    MyZoomImageView mSelectSqureHeadImage;
    @Inject

    ChangeHeadIconPresenter mPresenter;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_change_head_icon);
        this.initView();
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
    }

    private void initView() {
        ButterKnife.bind(this);
        DaggerChangeHeadIconPresenterComponent.builder()
                .changeHeadIconPresenterModule(new ChangeHeadIconPresenterModule(this)).build().inject(this);

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this);// 如果用户信息有变化  需要更新下界面

        updateView();
    }

    private void updateView() {
        mSelectSqureHeadImage.reSetState();
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.imgfail)
                .dontAnimate()
                .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL);

        Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + UserInfoData.getInstance().getUserInfoItem().photo)
                .apply(options).into(mSelectSqureHeadImage);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE) {
            updateView();
        }
    }

    @OnClick({R.id.m_select_squre_image_top_lp, R.id.m_select_squre_head_change_btn})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_select_squre_image_top_lp:
                onBackPressed();
                break;
            case R.id.m_select_squre_head_change_btn:
                Intent intent = new Intent(ChangeHeadIconActivity.this, SelectImagePopActivity.class);
                ChangeHeadIconActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);
                break;
            default:
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
