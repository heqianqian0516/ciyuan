package com.ciyuanplus.mobile.module.start_forum.start_stuff;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.module.popup.select_stuff_community.SelectStuffCommunityActivity;
import com.ciyuanplus.mobile.module.popup.start_stuff_guide.StartStuffGuideActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.ciyuanplus.mobile.widget.PriceEditText;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.StringUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/8/17.
 * 宝贝杂货铺
 */

public class StartStuffActivity extends MyBaseActivity implements StartStuffContract.View{
    @BindView(R.id.m_start_stuff_target_pay_check)
    CheckBox mStartStuffTargetPayCheck;
    @BindView(R.id.m_start_stuff_price_edit)
    PriceEditText mStartStuffPriceEdit;
    @BindView(R.id.m_start_stuff_target_free_check)
    CheckBox mStartStuffTargetFreeCheck;
    @BindView(R.id.m_start_stuff_add_image)
    ImageView mStartStuffAddImage;
    @BindView(R.id.m_start_stuff_bottom_lp)
    LinearLayout mStartStuffBottomLp;
    @BindView(R.id.m_start_stuff_stuff_zone_name)
    TextView mStartStuffStuffZoneName;
    @BindView(R.id.m_start_stuff_title)
    EditText mStartStuffTitle;
    @BindView(R.id.m_start_stuff_stuff_title)
    CommonTitleBar commonTitleBar;
    @BindView(R.id.m_start_stuff_content)
    RichTextEditor mStartStuffContent;

    private Dialog mLoadingDialog;

    @Inject
 StartStuffPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_stuff);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title));
        this.initView();

        mPresenter.initData(getIntent(), savedInstanceState);

//        if(isEdit){
//            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_EDIT_NEWS, StatisticsConstant.OP_EDIT_NEWS_PAGE_LOAD);
//        } else {
//            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_START_NEWS, StatisticsConstant.OP_START_NEWS_PAGE_LOAD);
//        }

        if (!SharedPreferencesManager.getBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_START_STUFF_HAS_GUIDE, false)) {
            Intent intent = new Intent(this, StartStuffGuideActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPresenter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerStartStuffPresenterComponent.builder().
                startStuffPresenterModule(new StartStuffPresenterModule(this)).build().inject(this);
        commonTitleBar.setLeftImage(R.mipmap.nav_icon_back);
        commonTitleBar.setRightImage(R.mipmap.nav_icon_save);
        commonTitleBar.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                onBackPressed();
            }
        });
        commonTitleBar.setCenterText("发布宝贝");
        commonTitleBar.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    CommonToast.getInstance("正在发布，请勿重复点击").show();
                    return;
                }

                if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();

                mPresenter.submmit();
            }
        });

        mStartStuffTitle.setFilters(new InputFilter[]{new LengthFilter(50)});

        mStartStuffContent.setmImagePreViewListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                int pos = mStartStuffContent.getImageViewIndex(view);
                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, mPresenter.getEditImages());
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, pos);
                StartStuffActivity.this.startActivity(intent);
            }
        });
        mStartStuffContent.setmOnDeleteImageListener((image) -> mPresenter.removeImage(image));
        //mStartStuffContent.setMaxLength(1800);
        mStartStuffContent.setAlertText("请描述您的宝贝，图文越多，越受关注哦~");
        //mStartStuffContent.setMainViewEnable(true);

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);


        mStartStuffTargetFreeCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mStartStuffTargetPayCheck.setChecked(false);
                mStartStuffTargetFreeCheck.setEnabled(false);
            } else {
                mStartStuffTargetFreeCheck.setEnabled(true);
            }
        });
        mStartStuffTargetPayCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mStartStuffTargetFreeCheck.setChecked(false);
                mStartStuffPriceEdit.setEnabled(true);
                mStartStuffTargetPayCheck.setEnabled(false);
            } else {
                mStartStuffPriceEdit.setEnabled(false);
                mStartStuffTargetPayCheck.setEnabled(true);
            }
        });

        mStartStuffTitle.requestFocus();
    }

    @Override
    public void onBackPressed() {
        String title = mStartStuffTitle.getText().toString();
        String content = mPresenter.getEditData();// 需要先上传完成之后才能搞
        if (Utils.isStringEmpty(title) && Utils.isStringEmpty(content)) {
            finish();
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(StartStuffActivity.this);
            builder.setMessage("是否放弃发布？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            CustomDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStartStuffContent.clearAllLayout();
        Constant.imageList.clear();// 清掉当前的图片，防止下次进来还有
    }

    @Override
    public void showEditData(final String content) {
        mStartStuffContent.post(() -> {
            mStartStuffContent.clearAllLayout();
            List<String> textList = StringUtils.cutStringByImgTag(content);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    String imagePath = StringUtils.getImgSrc(text);
//                        int width = Utils.getScreenWidth();
//                        int height = Utils.getScreenHeight();
                    mStartStuffContent.measure(0, 0);
//                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
//                        if (bitmap != null){
//                            mStartStuffContent.addImageViewAtIndex(mStartStuffContent.getLastIndex(), imagePath);
//                        } else {
//                            mStartStuffContent.addEditTextAtIndex(mStartStuffContent.getLastIndex(), text);
//                        }
                    mStartStuffContent.addImageViewAtIndex(mStartStuffContent.getLastIndex(), Constants.IMAGE_LOAD_HEADER + imagePath, "");

                } else {
                    mStartStuffContent.addEditTextAtIndex(mStartStuffContent.getLastIndex(), text);
                }
            }
        });

    }

    @OnClick({R.id.m_start_stuff_add_image, R.id.m_start_stuff_stuff_zone_name})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_start_stuff_add_image:
                if (mPresenter.mImagepathList.size() >= StartStuffPresenter.MAX_IMAGE_SIZE) {
                    CommonToast.getInstance("最多上传" + StartStuffPresenter.MAX_IMAGE_SIZE + "张图片").show();
                    return;
                }
                Intent intent = new Intent(StartStuffActivity.this, SelectImagePopActivity.class);
                StartStuffActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);

                break;
            case R.id.m_start_stuff_stuff_zone_name:
                if (mPresenter.isEdit) return;
                if (CommunityManager.getInstance().getmCommunityItems().length <= 1) {
                    CommonToast.getInstance("当前只有一个小区，不能进行切换").show();
                    return;
                }
                Intent intent2 = new Intent(StartStuffActivity.this, SelectStuffCommunityActivity.class);
                intent2.putExtra(Constants.INTENT_COMMUNITY_ID, mPresenter.mSelectedCommunityId);
                StartStuffActivity.this.startActivityForResult(intent2, Constants.REQUEST_CODE_SELECT_STUFF_COMMUNITY);

                break;
        }
    }

    @Override
    public String getTitleString() {
        return mStartStuffTitle.getText().toString();
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
    }

    @Override
    public RichTextEditor getContextView() {
        return mStartStuffContent;
    }

    @Override
    public boolean getPayCheckState() {
        return mStartStuffTargetPayCheck.isChecked();
    }

    @Override
    public double getPrice() {
        return mStartStuffPriceEdit.getDoublePrice();
    }

    @Override
    public void updateView() {
        if(mPresenter.isEdit){
            if (mPresenter.mFreshNewItem.price > 0.0f) {
                mStartStuffTargetPayCheck.setChecked(true);
                mStartStuffTargetFreeCheck.setChecked(false);
                mStartStuffPriceEdit.setPrice(mPresenter.mFreshNewItem.price);
            } else {
                mStartStuffTargetPayCheck.setChecked(false);
                mStartStuffTargetFreeCheck.setChecked(true);
            }
            mStartStuffTitle.setText(mPresenter.mFreshNewItem.title);
//            mStartStuffStuffZoneName.setText(mPresenter.mFreshNewItem.currentCommunityName);
        } else {// 默认发布到当前小区
            mStartStuffStuffZoneName.setText(UserInfoData.getInstance().getUserInfoItem().currentCommunityName);

        }
    }

    @Override
    public void setZoneName(String commName) {
        mStartStuffStuffZoneName.setText(commName);
    }

    @Override
    public String getPriceString() {
        return mStartStuffTargetFreeCheck.isChecked() ? "0" : mStartStuffPriceEdit.getStringPrice();
    }
}
